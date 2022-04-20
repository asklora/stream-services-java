package com.exchange_proxy.models;

public class QuotePayload extends BasePayload {
    public Object[] quotes;
    public QuotePayload(Object[] symbols) {
        super("subscribe");
        this.quotes = symbols;
    }
}
