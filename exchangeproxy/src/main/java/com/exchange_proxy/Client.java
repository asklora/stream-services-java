package com.exchange_proxy;
import java.net.MalformedURLException;
import java.net.URI;
import java.text.MessageFormat;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.net.URL;

import com.exchange_proxy.handler.Response;
import com.exchange_proxy.models.AuthPayload;
import com.exchange_proxy.models.TradesPayload;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.timroes.axmlrpc.XMLRPCClient;
import de.timroes.axmlrpc.XMLRPCException;
import de.timroes.axmlrpc.XMLRPCServerException;
import io.github.cdimascio.dotenv.Dotenv;
import io.lettuce.core.RedisClient;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands;

public class Client extends WebSocketClient {
    private static final Logger Log = LoggerFactory.getLogger(Client.class);
    private static final String channel_prefix = "asgi__group__";
    public String redis_uri;
    public  RedisClient redisClient;
    public  StatefulRedisPubSubConnection<String, String> connection;
    public RedisPubSubAsyncCommands<String, String> publisher;

    AuthPayload authentication;

    public Client(URI serverUri, Draft draft) {
        super(serverUri, draft);
    }
    public Client(URI serverURI,AuthPayload authentication, String redis_uri){
        super(serverURI);
        Log.info(serverURI.toString());
        this.authentication = authentication;
        this.redis_uri = redis_uri;
        this.redisClient = RedisClient.create(redis_uri);
        this.connection = redisClient.connectPubSub();

    }
    public Client(URI serverURI) {
        super(serverURI);
    }

    public Client(URI serverUri, Map<String, String> httpHeaders) {
        super(serverUri, httpHeaders);
    }
    private Object[] getTicker(String currency)
            throws XMLRPCException, XMLRPCServerException, MalformedURLException, Exception {
        Dotenv dotenv = Dotenv.load();
        String rpcUrl = dotenv.get("RPC_URL");
        // Log.info(rpcUrl);
        XMLRPCClient client = new XMLRPCClient(new URL(rpcUrl));
        Object[] ticker = (Object[]) client.call("get_listed_ticker", currency);
        return ticker;
    }
    

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.info("opened connection");
        this.publisher = this.connection.async();
    }

    public void subscribe(){
        try {
            Object[] ticker = getTicker("USD");
            TradesPayload payload = new TradesPayload(ticker);
            String stringpayload = payload.toJsonString();
            Log.debug("subscribe: " + stringpayload);
            send(stringpayload);
        } catch (Exception e) {
            Log.error("error", e);
        }
    }

    public void publish(Response msg){
        try{
            String channel = MessageFormat.format("{0}{1}", channel_prefix, msg.getSymbol());
            msg.setType("market_event");
            String data =  msg.toJsonString();
            this.publisher.publish(channel, data);
            // Log.info(msg.getType());
        }catch(JsonProcessingException e){
            Log.error("error", e);
        }
    }

    public void successHandler(Response res) throws JsonProcessingException {
        String message = res.getMsg();
        Log.info(MessageFormat.format("messsage : {0}",message));
        switch (message){
            case "connected" -> sendAuth();
            case "authenticated" -> subscribe();
        }
    }

    public void errorHandler(Response res){
        Log.error(MessageFormat.format("error : {0}",res.getMsg()));
    }
    @Override
    public void onMessage(String message) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ObjectReader reader = mapper.readerForListOf(Response.class);
        try {
            List<Response> responses = reader.readValue(message);
            Response res = responses.get(0);
            String msg_type = res.getT();
            Log.debug(MessageFormat.format("Type messages: {0}",msg_type));
            switch (msg_type){
                case "success" -> successHandler(res);
                case "error" -> errorHandler(res);
                case "t" -> publish(res);
                case "subscription" -> Log.info("subscribed");

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
    private void sendAuth() throws JsonProcessingException {
        String payload = authentication.toJsonString();
        Log.info("sending authentication payload");
        // Log.info(payload);
        send(payload);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
        // if the error is fatal then onClose will be called additionally
    }
}
