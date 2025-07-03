package com.adit.groww_sip_tracker.auth;

import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.time.Instant;
import java.util.Base64;

public class TotpGenerator {
    private final TimeBasedOneTimePasswordGenerator totp;
    private final SecretKey secretKey;

    public TotpGenerator(String base32Secret) throws Exception {
        this.totp = new TimeBasedOneTimePasswordGenerator();
        this.secretKey = decodeBase32Secret(base32Secret);
    }

    private SecretKey decodeBase32Secret(String base32) {
        // Base32 isn't built into Java stdlib, so we treat input as Base64 unless otherwise encoded
        byte[] decoded = Base64.getDecoder().decode(base32);
        return new SecretKeySpec(decoded, "HmacSHA1");
    }


    public String generateCurrentOtp() throws InvalidKeyException {
        int otp = totp.generateOneTimePassword(secretKey, Instant.now());
        return String.format("%06d", otp);
    }


}
