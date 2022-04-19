package com.exchange_proxy;

import java.net.URI;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.exchange_proxy.handler.Response;
import com.exchange_proxy.models.AuthPayload;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Client extends WebSocketClient {
    private static final Logger Log = LoggerFactory.getLogger(Client.class);

    AuthPayload authentication;

    public Client(URI serverUri, Draft draft) {
        super(serverUri, draft);
    }
    public Client(URI serverURI,AuthPayload authentication){
        super(serverURI);
        this.authentication = authentication;
    }
    public Client(URI serverURI) {
        super(serverURI);
    }

    public Client(URI serverUri, Map<String, String> httpHeaders) {
        super(serverUri, httpHeaders);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.debug("opened connection");
    }

    @Override
    public void onMessage(String message) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ObjectReader reader = mapper.readerForListOf(Response.class);
        try {
            List<Response> responses = reader.readValue(message);
            Response res = responses.get(0);
            Log.debug(res.toJsonString());
            if (res.getMsg().equals("connected")) {
                String payload = authentication.toJsonString();
                Log.debug("sending authentication payload: {}", payload);
                send(payload);
            }
            

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        // The close codes are documented in class org.java_websocket.framing.CloseFrame
        Log.warn(
                "Connection closed by " + (remote ? "remote peer" : "us") + " Code: " + code + " Reason: "
                        + reason);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
        // if the error is fatal then onClose will be called additionally
    }
}
