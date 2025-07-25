package com.adit.groww_sip_tracker.config;

import java.io.*;
import java.util.Properties;

public class ConfigLoader {
    private static final String CONFIG_FILE = "C:\\Users\\aditj\\projects\\groww-sip-tracker\\src\\main\\resources\\application.properties";

    public static String get(String key) throws IOException {
        Properties props = new Properties();
        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            props.load(input);
        }
        String value = props.getProperty(key);
        if (value == null) throw new IllegalArgumentException("Missing key: " + key);
        return value;
    }
}
