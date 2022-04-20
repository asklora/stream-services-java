package com.exchange_proxy.handler;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;



public class Response {
    private String T;
    @JsonInclude(JsonInclude.Include.NON_NULL) 
    private String msg;
    @JsonInclude(JsonInclude.Include.NON_NULL) 
    private String code,S,bx,ax,t;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private float bp,ap,bs,as,cls;
    private ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();

    public String toJsonString() throws JsonProcessingException {
        return writer.writeValueAsString(this);
    }
    // @JsonGetter("p")
    public float getCls() {
        return (bp + ap) /2;
    }

    public String getBx() {
        return bx;
    }
    public String setBx(String bx) {
        return this.bx = bx;
    }

    public String getAx() {
        return ax;
    }
    public String setAx(String ax) {
        return this.ax = ax;
    }
    public float getBp() {
        return bp;
    }
    public float setBp(float bp) {
        return this.bp = bp;
    }

    public float getAp() {
        return ap;
    }

    public float setAp(float ap) {
        return this.ap = ap;
    }

    public float getBs() {
        return bs;
    }

    public float setBs(float bs) {
        return this.bs = bs;
    }

    public float getAs() {
        return as;
    }

    public float setAs(float as) {
        return this.as = as;
    }

    @JsonGetter("t")
    public String getTime() {
        return t;
    }
    @JsonSetter("t")
    public void setTime(String time) {
        this.t = time;
    }
    @JsonGetter("S")
    public String getSymbol() {
        return S;
    }
    @JsonSetter("S")
    public void setSymbol(String time) {
        this.S = time;
    }
    @JsonGetter("T")
    public String getT() {
        return this.T;
    }
    @JsonSetter("T")
    public void setT(String T) {
        this.T = T;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }


    public static enum Type{
        SUCCESS("success"), 
        ERROR("error"), 
        SUBSCRIBTION("subscription"),
        QUOTE("q");
 
    private String values;
 
    Type(String values) {
        this.values = values;
    }
 
    public String getValues() {
        return values;
    }

    }

    public static enum  ConMessages{
        connected,
        authenticated,
    }

    public static enum StatusCode{
        INVALID(400),
        UNAUTHORIZED(401),
        AUTHFAILED(402),
        AUTHENTICATED(403),
        TIMEOUT(404),
        SYMBOLLIMIT(405),
        CONLIMIT(406),
        SLOWCLIENT(407),
        FAILVERSION(408),
        INSUFFICIENTCON(409),
        INTERNALERROR(500);
        private int values;
 
        StatusCode(int values) {
        this.values = values;
    }
 
    public int getValues() {
        return values;
    }
    }
}
