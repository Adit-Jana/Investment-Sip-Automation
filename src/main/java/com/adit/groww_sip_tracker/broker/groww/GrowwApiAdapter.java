package com.adit.groww_sip_tracker.broker.groww;

import com.adit.groww_sip_tracker.auth.GrowwAuthenticator;
import com.adit.groww_sip_tracker.broker.ApiClient;
import com.adit.groww_sip_tracker.broker.groww.api.GrowwApiClient;
import com.adit.groww_sip_tracker.logic.MarketCalendar;
import okhttp3.Response;

public class GrowwApiAdapter implements ApiClient {

    @Override
    public boolean isMarketOpen() {
        return MarketCalendar.isMarketOpenToday(); // already exists
    }

    @Override
    public boolean hasSufficientBalance(String fundId, int amount) {
        // Optional: make a balance-check call or skip
        return true; // Assume yes for now
    }

    @Override
    public boolean placeSipOrder(String fundId, int amount) {
        try {
            String token = GrowwAuthenticator.getAccessToken();
            String payload = String.format("{\"amount\":%d, \"fundId\":\"%s\"}", amount, fundId);
            try (Response res = GrowwApiClient.call("/v1/order/sip", payload, token)) {
                return res.isSuccessful();
            }
        } catch (Exception e) {
            System.err.println("Groww SIP order failed: " + e.getMessage());
            return false;
        }
    }

}
