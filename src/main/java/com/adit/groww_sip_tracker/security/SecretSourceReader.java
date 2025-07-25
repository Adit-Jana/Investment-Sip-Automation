package com.adit.groww_sip_tracker.security;

import com.adit.groww_sip_tracker.config.ConfigLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class SecretSourceReader {

    public static Optional<String> readFromFile(String path) {
        try {
            return Optional.of(Files.readString(Path.of(path)).trim());
        } catch (Exception e) {
            System.err.println("❌ Failed to read secret from file: " + e.getMessage());
            return Optional.empty();
        }
    }

    public static Optional<String> readFromVault(String key) {
        // Placeholder for future vault integration
        // e.g., HashiCorp Vault, Azure Key Vault, etc.
        return Optional.empty();
    }

    public static Optional<String> getRawTotpSecret() throws IOException {
        String source = ConfigLoader.get("TOTP_SECRET_SOURCE");
        if (source.equalsIgnoreCase("file")) {
            String path = ConfigLoader.get("TOTP_SECRET_FILE");
            return readFromFile(path);
        } else if (source.equalsIgnoreCase("vault")) {
            String key = ConfigLoader.get("TOTP_SECRET_VAULT_KEY");
            return readFromVault(key);
        }
        return Optional.empty();
    }

}
