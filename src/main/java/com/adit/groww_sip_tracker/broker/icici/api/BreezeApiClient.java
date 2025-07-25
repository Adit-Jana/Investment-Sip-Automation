package com.adit.groww_sip_tracker.broker.icici.api;

import okhttp3.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;

public class BreezeApiClient {

    private static final OkHttpClient client = new OkHttpClient();
    private static final String BASE_URL = "https://api.icicidirect.com/breezeapi/rest";

    /**
     * Sends a POST request to Breeze API with required headers and checksum.
     *
     * @param path         API endpoint path (e.g. "/order/sip")
     * @param jsonPayload  JSON string body
     * @param appKey       Your Breeze AppKey
     * @param sessionToken Your Breeze SessionToken
     * @param secretKey    Your Breeze SecretKey (used for checksum)
     * @return HTTP response
     * @throws Exception If request fails
     */
    public static Response call(String path, String jsonPayload, String appKey, String sessionToken, String secretKey) throws Exception {
        String timestamp = Instant.now().toString(); // ISO8601 UTC
        String checksum = computeChecksum(timestamp, jsonPayload, secretKey);

        RequestBody body = RequestBody.create(
                jsonPayload, MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(BASE_URL + path)
                .addHeader("X-AppKey", appKey)
                .addHeader("X-SessionToken", sessionToken)
                .addHeader("X-Timestamp", timestamp)
                .addHeader("X-Checksum", checksum)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        return client.newCall(request).execute();
    }

    /**
     * Computes SHA256 checksum: SHA256(timestamp + json + secretKey)
     */
    private static String computeChecksum(String timestamp, String json, String secretKey) throws Exception {
        String data = timestamp + json + secretKey;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }
}

