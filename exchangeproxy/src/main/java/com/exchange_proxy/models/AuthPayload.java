package com.exchange_proxy.models;

public class AuthPayload extends BasePayload {
    public String key,secret;
    
    public AuthPayload(String Key,String secret) {
        super("auth");
        this.key = Key;
        this.secret =secret;
    }
}
