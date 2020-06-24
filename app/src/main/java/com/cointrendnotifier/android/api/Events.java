package com.cointrendnotifier.android.api;

import com.cointrendnotifier.android.api.dtos.EventDto;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Events {
    private static final String baseUrl = Api.URL + "/events";

    @NotNull
    public static List<EventDto> getEvents() throws IOException, JSONException {
        // create client
        OkHttpClient client = new OkHttpClient();

        // execute request
        Request request = new Request.Builder()
                .url(baseUrl)
                .addHeader("Authorization", "Bearer " + Api.getJwt())
                .get()
                .build();
        Response response = client.newCall(request).execute();

        // make sure the response is successful
        if (!response.isSuccessful()) throw new UnsuccessfulHttpRequestException(response);

        // parse response
        JSONArray responseBody = new JSONArray(response.body().string());
        List<EventDto> events = new ArrayList<>(responseBody.length());
        for (int i = 0; i < responseBody.length(); i++) {
            // build dto
            JSONObject current = responseBody.getJSONObject(i);
            events.add(new EventDto(
                    current.getString("baseAssetName"),
                    current.getString("quoteAssetName"),
                    current.getDouble("probability"),
                    current.getLong("firedAt"),
                    current.getString("_id")));
        }

        // return result
        return events;
    }

    @NotNull
    public static List<EventDto> getEvents(int amount) throws IOException, JSONException {
        // create client
        OkHttpClient client = new OkHttpClient();

        // execute request
        Request request = new Request.Builder()
                .url(baseUrl + "?amount=" + amount)
                .addHeader("Authorization", "Bearer " + Api.getJwt())
                .get()
                .build();
        Response response = client.newCall(request).execute();

        // make sure the response is successful
        if (!response.isSuccessful()) throw new UnsuccessfulHttpRequestException(response);

        // parse response
        JSONArray responseBody = new JSONArray(response.body().string());
        List<EventDto> events = new ArrayList<>(responseBody.length());
        for (int i = 0; i < responseBody.length(); i++) {
            // build dto
            JSONObject current = responseBody.getJSONObject(i);
            events.add(new EventDto(
                    current.getString("baseAssetName"),
                    current.getString("quoteAssetName"),
                    current.getDouble("probability"),
                    current.getLong("firedAt"),
                    current.getString("_id")));
        }

        // return result
        return events;
    }

    @NotNull
    public static EventDto getById(@NotNull String _id) throws IOException, JSONException {
        // create client
        OkHttpClient client = new OkHttpClient();

        // execute request
        Request request = new Request.Builder()
                .url(String.format("%s/%s", baseUrl, _id))
                .addHeader("Authorization", "Bearer " + Api.getJwt())
                .get()
                .build();
        Response response = client.newCall(request).execute();

        // make sure the response is successful
        if (!response.isSuccessful()) throw new UnsuccessfulHttpRequestException(response);

        // build dto
        JSONObject responseBody = new JSONObject(response.body().string());
        EventDto eventDto = new EventDto(
                responseBody.getString("baseAssetName"),
                responseBody.getString("quoteAssetName"),
                responseBody.getDouble("probability"),
                responseBody.getLong("firedAt"),
                responseBody.getString("_id"));

        // return result
        return eventDto;
    }
}
