package com.adit.groww_sip_tracker.order;

import okhttp3.*;

public class Notifier {

    public static void sendTelegramMessage(String message, String botToken, String chatId) {
        OkHttpClient client = new OkHttpClient();
        String encodedMsg = message.replace(" ", "%20");
        String url = String.format("https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s", botToken, chatId, encodedMsg);

        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) System.out.println("❗ Telegram alert failed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
