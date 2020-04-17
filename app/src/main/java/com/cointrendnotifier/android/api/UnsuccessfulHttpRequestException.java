package com.cointrendnotifier.android.api;

import android.annotation.SuppressLint;

import okhttp3.Response;

public class UnsuccessfulHttpRequestException extends RuntimeException {
    private Response response;

    @SuppressLint("DefaultLocale")
    public UnsuccessfulHttpRequestException(Response response) {
        super(String.format("The response was unsuccessful - code %d", response.code()));
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }
}
