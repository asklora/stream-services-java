package com.exchange_proxy.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class BasePayload extends AbstractPyaload {
    private ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
    public String action;

    public BasePayload(String action) {
        this.action = action;
    }

    public String toJsonString() throws JsonProcessingException {
        return writer.writeValueAsString(this);
    }
}
