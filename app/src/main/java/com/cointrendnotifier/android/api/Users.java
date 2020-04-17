package com.cointrendnotifier.android.api;

import com.cointrendnotifier.android.api.dtos.RegisteredUserDto;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Users {
    private static final String baseUrl = Api.URL + "/users";

    @NotNull
    public static RegisteredUserDto signUp(String email, String username, String password, int alertLimit, String phoneNumber) throws JSONException, IOException {
        // create client
        OkHttpClient client = new OkHttpClient();

        // set body
        final JSONObject bodyJson = new JSONObject();
        bodyJson.put("email", email);
        bodyJson.put("username", username);
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

    @NotNull
    public static RegisteredUserDto signUp(String email, String username, String password, int alertLimit) throws JSONException, IOException {
        return signUp(email, username, password, alertLimit, null);
    }

    @NotNull
    public static RegisteredUserDto updateUser(String email, String username, int alertLimit, String phoneNumber) throws JSONException, IOException {
        // create client
        OkHttpClient client = new OkHttpClient();

        // set body
        final JSONObject bodyJson = new JSONObject();
        bodyJson.put("email", email);
        bodyJson.put("username", username);
        bodyJson.put("alertLimit", alertLimit);
        if (phoneNumber != null) bodyJson.put("phoneNumber", phoneNumber);
        RequestBody body = RequestBody.create(bodyJson.toString(), Api.JSON);

        // execute request
        Request request = new Request.Builder()
                .url(baseUrl)
                .addHeader("Authorization", "Bearer " + Api.getJwt())
                .put(body)
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
        if (responseBody.has("notifiedAt")) {
            registeredUserDto.setNotifiedAt(responseBody.getInt("notifiedAt"));
        }

        // return result
        return registeredUserDto;
    }

    @NotNull
    public static RegisteredUserDto updateUser(String email, String username, int alertLimit) throws JSONException, IOException {
        return updateUser(email, username, alertLimit, null);
    }

    @NotNull
    public static RegisteredUserDto getUser() throws JSONException, IOException {
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
        if (responseBody.has("notifiedAt")) {
            registeredUserDto.setNotifiedAt(responseBody.getInt("notifiedAt"));
        }

        // return result
        return registeredUserDto;
    }

    public static void login(String email, String password) throws JSONException, IOException {
        // create client
        OkHttpClient client = new OkHttpClient();

        // set body
        final JSONObject bodyJson = new JSONObject();
        bodyJson.put("email", email);
        bodyJson.put("password", password);
        RequestBody body = RequestBody.create(bodyJson.toString(), Api.JSON);

        // execute request
        Request request = new Request.Builder()
                .url(baseUrl)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();

        // make sure the response is successful
        if (!response.isSuccessful()) throw new UnsuccessfulHttpRequestException(response);

        // save jwt
        JSONObject responseBody = new JSONObject(response.body().string());
        Api.setJwt(responseBody.getString("jwt"));
    }

    public static void logout() {
        Api.setJwt(null);
    }

    public static void changePassword(String oldPassword, String newPassword) throws JSONException, IOException {
        // create client
        OkHttpClient client = new OkHttpClient();

        // set body
        final JSONObject bodyJson = new JSONObject();
        bodyJson.put("oldPassword", oldPassword);
        bodyJson.put("newPassword", newPassword);
        RequestBody body = RequestBody.create(bodyJson.toString(), Api.JSON);

        // execute request
        Request request = new Request.Builder()
                .url(baseUrl)
                .addHeader("Authorization", "Bearer " + Api.getJwt())
                .patch(body)
                .build();
        Response response = client.newCall(request).execute();

        // make sure the response is successful
        if (!response.isSuccessful()) throw new UnsuccessfulHttpRequestException(response);
    }
}
