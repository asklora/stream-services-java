package com.exchange_proxy;
import io.github.cdimascio.dotenv.Dotenv;

public class Credentials {
    public final String API_KEY;
    public final String API_SECRET;
    public final String REDIS_HOST;
    public final String REDIS_PORT;

    public Credentials() {
        Dotenv dotenv = Dotenv.load();
        this.API_KEY = dotenv.get("BROKER_KEY");
        this.API_SECRET = dotenv.get("BROKER_SECRET");
        this.REDIS_HOST = dotenv.get("REDIS_HOST");
        this.REDIS_PORT = dotenv.get("REDIS_PORT");
    }
    public String getRedisUri(){
        return String.format("redis://%s:%s/",this.REDIS_HOST,this.REDIS_PORT);
    }
}
