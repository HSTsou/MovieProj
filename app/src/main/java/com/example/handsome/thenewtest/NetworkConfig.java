package com.example.handsome.thenewtest;

public class NetworkConfig {
    public enum Env {
        PROD("https://movingmoviezero.appspot.com/"),
        DEV("https://movingmoviezero.appspot.com/");

        public final String host;
        Env(String host) {
            this.host = host;
        }
    }

    public final static Env env = Env.PROD;

    public static String getBaseUrl() {
        return env.host;
    }
}
