package com.adit.groww_sip_tracker.order;

import com.adit.groww_sip_tracker.config.ConfigLoader;
import okhttp3.*;

public class GrowwOrderPlacer {

    private static final OkHttpClient client = new OkHttpClient();

    public static void placeOrder(double amount, String token, String fundId) {
        try {
            //String token = ConfigLoader.get("GROWW_API_TOKEN");
            //String fundId = ConfigLoader.get("GROWW_FUND_ID");
            String payload = "{\"instrument_id\": \"" + fundId + "\", \"quantity\": " + amount + "}";

            Request request = new Request.Builder()
                    .url("https://api.groww.in/v1/orders")
                    .addHeader("Authorization", "Bearer " + token)
                    .addHeader("Content-Type", "application/json")
                    .post(RequestBody.create(payload, MediaType.parse("application/json")))
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    System.err.println("🚨 Order failed: " + response.code() + " - " + response.message());
                } else {
                    System.out.println("✅ Order placed successfully at low NAV!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
