package com.exchange_proxy.models;

import com.fasterxml.jackson.core.JsonProcessingException;

abstract class AbstractPyaload  {

    public abstract String toJsonString() throws JsonProcessingException;
}
