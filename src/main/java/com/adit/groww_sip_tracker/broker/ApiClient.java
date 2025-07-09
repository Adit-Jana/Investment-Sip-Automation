package com.adit.groww_sip_tracker.broker;

public interface ApiClient {
    boolean isMarketOpen();
    boolean hasSufficientBalance(String fundId, int amount);
    boolean placeSipOrder(String fundId, int amount);

}
