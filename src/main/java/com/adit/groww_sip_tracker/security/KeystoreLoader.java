package com.adit.groww_sip_tracker.security;

import javax.crypto.SecretKey;
import java.io.FileInputStream;
import java.security.KeyStore;

public class KeystoreLoader {
    public static SecretKey loadKey(String keystorePath, String keystorePassword, String alias) {
        SecretKey ae = null;

        try (FileInputStream fis = new FileInputStream(keystorePath)) {
            KeyStore ks = KeyStore.getInstance("JCEKS");
            ks.load(fis, keystorePassword.toCharArray());
            ae = (SecretKey) ks.getKey(alias, keystorePassword.toCharArray());
        } catch (Exception e){
            e.printStackTrace();
        }

        return ae;
    }


}
