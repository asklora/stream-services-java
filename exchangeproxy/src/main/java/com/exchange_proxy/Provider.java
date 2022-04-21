package com.exchange_proxy;

import java.net.URI;
import java.net.URISyntaxException;

import com.exchange_proxy.models.AuthPayload;

public class Provider {
    final static String base_url = "stream.data.alpaca.markets";
    final static String demo_base_url = "stream.data.sandbox.alpaca.markets";
    final static String api_ver = "v2";
    final static String protocol = "wss";
    final static String[] source = { "iex", "sip" };
    final Credentials credentials = new Credentials();
    final AuthPayload authentication = new AuthPayload(credentials.API_KEY, credentials.API_SECRET);

    boolean demo;

    public Provider(boolean demo) {
        this.demo = demo;
    }

    private String __getUrl() {
        if (demo) {
            return String.format("%s://%s/%s/", protocol, demo_base_url, api_ver);
        } else {
            return String.format("%s://%s/%s/", protocol, base_url, api_ver);
        }
    };

    public Client getIexClient() throws URISyntaxException {
        URI url = new URI(__getUrl() + source[0]);
        System.out.println(url);
        Client client = new Client(url, authentication, this.credentials.getRedisUri());
        return client;
    }

    public Client getSipClient() throws URISyntaxException {
        URI url = new URI(__getUrl() + source[1]);
        Client client = new Client(url, authentication,this.credentials.getRedisUri());
        return client;
    }

}
