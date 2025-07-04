package com.adit.groww_sip_tracker.auth;

import com.adit.groww_sip_tracker.config.ConfigLoader;
import com.adit.groww_sip_tracker.security.EncryptDecryptWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

public class GrowwAuthenticator {

    public static String getAccessToken() throws Exception {
        // Check cache first
        String cached = TokenCache.getToken();
        if (cached != null) return cached;

        // Load credentials
        String apiKey = ConfigLoader.get("GROWW_API_KEY");
        String apiSecret = ConfigLoader.get("GROWW_API_SECRET");
        //String encryptedSecret = ConfigLoader.get("GROWW_TOTP_SECRET_ENCRYPTED");

        // Build request
        //String encrypted = ConfigLoader.get("GROWW_TOTP_SECRET_ENCRYPTED");
        //String key = ConfigLoader.get("ENCRYPTION_KEY"); // key need to be generated

        // AES  key
        //SecretKey aesKey = KeystoreLoader.loadKey("secure/keystore.jceks", "changeit", "aesKey");

        EncryptDecryptWrapper encryptDecryptWrapper = new EncryptDecryptWrapper();

        // get the encrypted secrets
        String encryptedGrowwSecret = encryptDecryptWrapper.encryptRawSecretWrapper(); //ConfigLoader.get("GROWW_TOTP_SECRET_ENCRYPTED");


        // decrypt the totp secret which is received from groww
        String decryptedSecret = encryptDecryptWrapper.decryptionSecretWrapper(encryptedGrowwSecret);

        //generate OTP
        TotpGenerator generator = new TotpGenerator(decryptedSecret);


        OkHttpClient client = new OkHttpClient();
        ObjectMapper mapper = new ObjectMapper();

        // Generate OTP with retry

        int maxRetries = 3;
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            String otp = generator.generateCurrentOtp();

            String payload = String.format(
                    "{\"api_key\":\"%s\",\"api_secret\":\"%s\",\"totp\":\"%s\"}",
                    apiKey, apiSecret, otp
            );

            Request request = new Request.Builder()
                    .url("https://api.groww.in/v1/login") // Adjust endpoint
                    .addHeader("Content-Type", "application/json")
                    .post(RequestBody.create(payload, MediaType.parse("application/json")))
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonNode json = mapper.readTree(response.body().toString());
                    String token = json.get("access_token").asText();
                    int expiresIn = json.get("expires_in").asInt();
                    TokenCache.storeToken(token, expiresIn);
                    return token;
                } else {
                    System.err.printf("⚠️ Attempt %d failed: %s%n", attempt, response.message());
                    if (attempt < maxRetries) Thread.sleep(3000);
                }
            } catch (Exception e) {
                System.err.printf("❌ Attempt %d errored: %s%n", attempt, e.getMessage());
                if (attempt < maxRetries) Thread.sleep(3000);
            }
        }

        throw new RuntimeException("🚫 All authentication attempts failed.");
    }

}
