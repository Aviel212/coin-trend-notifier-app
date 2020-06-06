package com.cointrendnotifier.android.api;

import com.cointrendnotifier.android.api.dtos.SetPreferenceDto;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Preferences {
    private static final String baseUrl = Api.URL + "/preferences";

    public static void setPreference(String baseAssetName, String quoteAssetName, double probability) throws JSONException, IOException {
        // create client
        OkHttpClient client = new OkHttpClient();

        // set body
        final JSONObject bodyJson = new JSONObject();
        bodyJson.put("baseAssetName", baseAssetName);
        bodyJson.put("quoteAssetName", quoteAssetName);
        bodyJson.put("probability", probability);
        RequestBody body = RequestBody.create(bodyJson.toString(), Api.JSON);

        // execute request
        Request request = new Request.Builder()
                .url(baseUrl)
                .addHeader("Authorization", "Bearer " + Api.getJwt())
                .post(body)
                .build();
        Response response = client.newCall(request).execute();

        // make sure the response is successful
        if (!response.isSuccessful()) throw new UnsuccessfulHttpRequestException(response);
    }

    public static void deletePreference(String baseAssetName, String quoteAssetName) throws IOException {
        // create client
        OkHttpClient client = new OkHttpClient();

        // execute request
        Request request = new Request.Builder()
                .url(String.format("%s?baseAssetName=%s&quoteAssetName=%s", baseUrl, baseAssetName, quoteAssetName))
                .addHeader("Authorization", "Bearer " + Api.getJwt())
                .delete()
                .build();
        Response response = client.newCall(request).execute();

        // make sure the response is successful
        if (!response.isSuccessful()) throw new UnsuccessfulHttpRequestException(response);
    }

    @NotNull
    public static List<SetPreferenceDto> getPreferences() throws IOException, JSONException {
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
        List<SetPreferenceDto> preferences = new ArrayList<>(responseBody.length());
        for (int i = 0; i < responseBody.length(); i++) {
            // build dto
            JSONObject current = responseBody.getJSONObject(i);
            preferences.add(new SetPreferenceDto(
                    current.getString("baseAssetName"),
                    current.getString("quoteAssetName"),
                    current.getDouble("probability")));
        }

        // return result
        return preferences;
    }
}
