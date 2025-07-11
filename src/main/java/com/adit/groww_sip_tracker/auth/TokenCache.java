package com.adit.groww_sip_tracker.auth;

import com.adit.groww_sip_tracker.config.ConfigLoader;

import java.io.File;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class TokenCache {

    private static String cachedToken = null;
    private static Instant expiryTime = null;


    private static final Logger logger = Logger.getLogger("TokenLogger");

    static {
        try {

            String logDirPath = ConfigLoader.get("LOG_DIR"); // default fallback
            File logDir = new File(logDirPath);
            if (!logDir.exists()) logDir.mkdirs();

            String date = LocalDate.now().toString();

            FileHandler fh = new FileHandler(logDirPath + File.separator + "token-" + date + ".log", true); // append mode
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
            logger.setUseParentHandlers(false); // disable console output
        } catch (Exception e) {
            System.err.println("⚠️ Failed to initialize token logger: " + e.getMessage());
        }
    }



    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    .withZone(ZoneId.systemDefault());

    public static String getToken() {
        if (cachedToken != null && expiryTime != null && Instant.now().isBefore(expiryTime)) {
            Instant now = Instant.now();
            long secondsLeft = expiryTime.getEpochSecond() - now.getEpochSecond();

            if (secondsLeft <= 300 && secondsLeft > 0) {
                String expiryFormatted = formatter.format(expiryTime);
                System.out.println("⚠️ Token will expire soon at: " + expiryFormatted);
                logger.warning("Token nearing expiry: " + expiryFormatted + " (" + secondsLeft + "s left)");
            }

            if (now.isBefore(expiryTime)) {
                String expiryFormatted = formatter.format(expiryTime);
                System.out.println("🔁 Using cached token. Expires at: " + expiryFormatted);
                logger.info("Reusing cached token. Expires at: " + expiryFormatted);
                return cachedToken;
            }
        }
        return null;
    }

    public static void storeToken(String token, int expiresInSeconds) {
        cachedToken = token;
        expiryTime = Instant.now().plusSeconds(expiresInSeconds - 30);
        String expiryFormatted = formatter.format(expiryTime);

        System.out.println("💾 Token cached. Will expire at: " + expiryFormatted);
        logger.info("New token cached. Expires at: " + expiryFormatted);

    }

    public static void clear() {
        cachedToken = null;
        expiryTime = null;
    }




}
