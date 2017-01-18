package com.parentoff.rest.config;


public class ConfigHelper {

	public static String getSpecificValue(String key) {
		Config config = new Config();
        String val = config.getSpecificValue(key);
        return val;
    }
	
	public static String getSpecificValue(String key, String defaultValue) {
        String val = getSpecificValue(key);
        if(val == null) return defaultValue;
        return val;
    }
}
