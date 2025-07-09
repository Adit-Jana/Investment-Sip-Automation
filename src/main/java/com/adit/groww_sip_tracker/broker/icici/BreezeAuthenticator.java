package com.adit.groww_sip_tracker.broker.icici;

import com.adit.groww_sip_tracker.config.ConfigLoader;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;

public class BreezeAuthenticator {

    private static final OkHttpClient client = new OkHttpClient();
    private static String cachedSessionToken = null;


    public static String getSessionToken(String appKey, String secretKey) {
        try {
            if (cachedSessionToken != null) return cachedSessionToken;

            // Step 1: Get API_Session manually from browser login
            String apiSession = ConfigLoader.get("BREEZE_API_SESSION");

            // Step 2: Call CustomerDetails API to get session token
            String url = "https://api.icicidirect.com/breezeapi/rest/customer/getcustomerdetails";

            JSONObject payload = new JSONObject();
            payload.put("API_Session", apiSession);

            RequestBody body = RequestBody.create(
                    payload.toString(), MediaType.parse("application/json")
            );

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("X-AppKey", appKey)
                    .addHeader("X-APIKey", appKey)
                    .addHeader("X-APISession", apiSession)
                    .addHeader("Content-Type", "application/json")
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) throw new RuntimeException("❌ Failed to get session token");

                String json = response.body().string();
                JSONObject obj = new JSONObject(json);
                cachedSessionToken = obj.getString("session_token");
                return cachedSessionToken;
            }
        } catch (RuntimeException | IOException e) {
            throw new RuntimeException(e);
        }
    }


}
