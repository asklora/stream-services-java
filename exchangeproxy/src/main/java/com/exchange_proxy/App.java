package com.exchange_proxy;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;



public final class App {
    private App() {
    }

    

    public static void main(String[] args) throws Exception {
        BasicConfigurator.configure(); // log4j
        Logger.getRootLogger().setLevel(Level.DEBUG);

        Provider provider = new Provider(false);
        Client client = provider.getIexClient();
        client.connect();

    }
}