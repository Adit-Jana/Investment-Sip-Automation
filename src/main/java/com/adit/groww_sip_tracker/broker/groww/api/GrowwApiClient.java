package com.adit.groww_sip_tracker.broker.groww.api;

import okhttp3.*;
import java.util.Map;

public class GrowwApiClient {

    private static final OkHttpClient client = new OkHttpClient();
    private static final String BASE_URL = "https://api.groww.in";

    /**
     * Sends a POST request to the Groww API with the given path and JSON payload.
     *
     * @param path    The API path after base URL (e.g. "/v1/order/sip")
     * @param json    The request body as a JSON string
     * @param token   The Bearer token from GrowwAuthenticator
     * @return        The HTTP response object
     * @throws Exception if the request fails
     */
    public static Response call(String path, String json, String token) throws Exception {
        RequestBody body = RequestBody.create(
                json, MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(BASE_URL + path)
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        return client.newCall(request).execute();
    }

    /**
     * (Optional) Sends a GET request with headers (e.g., for profile or holdings).
     */
    public static Response get(String path, Map<String, String> headers) throws Exception {
        Request.Builder builder = new Request.Builder().url(BASE_URL + path);

        headers.forEach(builder::addHeader);

        Request request = builder.get().build();
        return client.newCall(request).execute();
    }


}
