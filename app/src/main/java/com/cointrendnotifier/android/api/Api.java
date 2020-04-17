package com.cointrendnotifier.android.api;

import okhttp3.MediaType;

public class Api {
    public static final String URL = "https://coin-trend-notifier-api.herokuapp.com/api/";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static String jwt;

    public static String getJwt() {
        return jwt;
    }

    public static void setJwt(String jwt) {
        Api.jwt = jwt;
    }

    public static boolean isLoggedIn() {
        return jwt != null;
    }
}
