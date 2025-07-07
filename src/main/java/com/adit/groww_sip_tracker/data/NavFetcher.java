package com.adit.groww_sip_tracker.data;

import okhttp3.*;
import com.fasterxml.jackson.databind.*;

public class NavFetcher {
    private static final OkHttpClient client = new OkHttpClient();
    private static final String NAV_URL = "https://api.mfapi.in/mf/119551"; // Replace with your fund

    public static double getLatestNAV() throws Exception {
        Request request = new Request.Builder().url(NAV_URL).build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new RuntimeException("NAV fetch failed");

            ObjectMapper mapper = new ObjectMapper();
            String json = response.body().string();
            JsonNode data = mapper.readTree(json).get("data").get(0);
            return Double.parseDouble(data.get("nav").asText());
        }
    }
}
