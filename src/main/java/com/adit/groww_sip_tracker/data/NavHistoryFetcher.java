package com.adit.groww_sip_tracker.data;

import java.io.IOException;
import java.util.*;
import okhttp3.*;
import com.fasterxml.jackson.databind.*;

public class NavHistoryFetcher {
    private static final String NAV_URL = "https://api.mfapi.in/mf/119551"; // Example: Nifty 50 ETF
    private static final OkHttpClient client = new OkHttpClient();

    public static List<Double> getLastNDaysNav(int days) throws IOException {
        List<Double> navs = new ArrayList<>();
        Request request = new Request.Builder().url(NAV_URL).build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Failed to fetch NAV history");

            String json = response.body().string();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode data = mapper.readTree(json).get("data");

            for (int i = 0; i < days && i < data.size(); i++) {
                String navStr = data.get(i).get("nav").asText();
                navs.add(Double.parseDouble(navStr));
            }
        }
        return navs;
    }
}
