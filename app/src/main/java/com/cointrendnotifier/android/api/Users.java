package com.cointrendnotifier.android.api;

import com.cointrendnotifier.android.api.dtos.RegisteredUserDto;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Users {
    private static final String baseUrl = Api.URL + "/users";

    public static RegisteredUserDto signUp(String username, String email, String password, int alertLimit, String phoneNumber) throws JSONException, IOException {
        // create client
        OkHttpClient client = new OkHttpClient();

        // set body
        final JSONObject bodyJson = new JSONObject();
        bodyJson.put("username", username);
        bodyJson.put("email", email);
        bodyJson.put("password", password);
        bodyJson.put("alertLimit", alertLimit);
        if (phoneNumber != null) bodyJson.put("phoneNumber", phoneNumber);
        RequestBody body = RequestBody.create(bodyJson.toString(), Api.JSON);

        // execute request
        Request request = new Request.Builder()
                .url(baseUrl)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();

        // make sure the response is successful
        if (!response.isSuccessful()) throw new UnsuccessfulHttpRequestException(response);

        // build dto
        JSONObject responseBody = new JSONObject(response.body().string());
        RegisteredUserDto registeredUserDto = new RegisteredUserDto(
                responseBody.getString("email"),
                responseBody.getString("username"),
                responseBody.getInt("alertLimit"),
                responseBody.getString("_id"));
        if (responseBody.has("phoneNumber")) {
            registeredUserDto.setPhoneNumber(responseBody.getString("phoneNumber"));
        }

        // return result
        return registeredUserDto;
    }

    public static RegisteredUserDto signUp(String username, String email, String password, int alertLimit) throws JSONException, IOException {
        return signUp(username, email, password, alertLimit, null);
    }
}
