package com.adit.groww_sip_tracker.broker.icici;

import com.adit.groww_sip_tracker.broker.ApiClient;
import com.adit.groww_sip_tracker.broker.icici.api.BreezeApiClient;
import com.adit.groww_sip_tracker.config.ConfigLoader;
import com.adit.groww_sip_tracker.logic.MarketCalendar;
import okhttp3.Response;

import java.io.IOException;

public class BreezeApiAdapter implements ApiClient {
    private final String appKey = ConfigLoader.get("BREEZE_APP_KEY");
    private final String secretKey = ConfigLoader.get("BREEZE_SECRET_KEY");
    private final String sessionToken = BreezeAuthenticator.getSessionToken(appKey, secretKey);

    public BreezeApiAdapter() throws IOException {
    }

    @Override
    public boolean isMarketOpen() {
        return MarketCalendar.isMarketOpenToday(); // You can enhance this with Breeze's exchange status API
    }

    @Override
    public boolean hasSufficientBalance(String fundId, int amount) {
        // Breeze doesn't expose wallet balance directly; assume true or simulate
        return true;
    }

    @Override
    public boolean placeSipOrder(String fundId, int amount) {
        try {
            String payload = String.format("{\"fundId\":\"%s\",\"amount\":%d}", fundId, amount);
            Response response = BreezeApiClient.call("/order/sip", payload, appKey, sessionToken,"");
            return response.isSuccessful();
        } catch (Exception e) {
            System.err.println("❌ Breeze SIP order failed: " + e.getMessage());
            return false;
        }
    }


}
