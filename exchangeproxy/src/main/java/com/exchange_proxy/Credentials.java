package com.exchange_proxy;
import io.github.cdimascio.dotenv.Dotenv;

public class Credentials {
    public final String API_KEY;
    public final String API_SECRET;

    public Credentials() {
        Dotenv dotenv = Dotenv.load();
        this.API_KEY = dotenv.get("BROKER_KEY");
        this.API_SECRET = dotenv.get("BROKER_SECRET");
    }
}
