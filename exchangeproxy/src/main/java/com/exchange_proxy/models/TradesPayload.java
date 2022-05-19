package com.exchange_proxy.models;

public class TradesPayload extends BasePayload{
    public Object[] trades;
    public Object[] bars;
    public TradesPayload(Object[] symbols) {
        super("subscribe");
        this.trades = symbols;
        this.bars = symbols;
    }
}
