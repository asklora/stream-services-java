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
    private String code;
    private ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();

    public String toJsonString() throws JsonProcessingException {
        return writer.writeValueAsString(this);
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
