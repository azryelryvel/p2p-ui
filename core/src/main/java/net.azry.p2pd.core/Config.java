package net.azry.p2pd.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Config {
    public static Map<String, String> properties = getProperties();

    private static Map<String, String> getProperties() {
        Properties properties = new Properties();
        try {
            properties.load(Config.class.getResourceAsStream("/config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, String> propertiesMap = new HashMap<>();
        for (final String name : properties.stringPropertyNames()) {
            propertiesMap.put(name, properties.getProperty(name));
        }

        return propertiesMap;
    }
}
