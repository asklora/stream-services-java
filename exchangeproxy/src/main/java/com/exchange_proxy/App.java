package com.exchange_proxy;
import org.apache.log4j.BasicConfigurator;

public final class App {
    private App() {
    }

    public static void main(String[] args) throws Exception {
        BasicConfigurator.configure(); // log4j
        Provider provider = new Provider(false);
        Client client = provider.getIexClient();
        client.connect();

    }
}