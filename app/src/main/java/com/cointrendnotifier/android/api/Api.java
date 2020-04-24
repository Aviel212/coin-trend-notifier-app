package com.cointrendnotifier.android.api;

import okhttp3.MediaType;

public class Api {
    static final String URL = "https://coin-trend-notifier-api.herokuapp.com/api";
    static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static String jwt;

    static String getJwt() {
        return jwt;
    }

    static void setJwt(String jwt) {
        Api.jwt = jwt;
    }

    public static boolean isLoggedIn() {
        return jwt != null;
    }
}
