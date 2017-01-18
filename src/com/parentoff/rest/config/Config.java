package com.parentoff.rest.config;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

public class Config {
	private static PropertiesConfiguration config;
	
	static {
        if (config == null) {
            try {
            	config = new PropertiesConfiguration();
            	config.setDelimiterParsingDisabled(true);
            	config.setFileName("/home/ec2-user/login-config/config.properties");
            	config.load();
            	config.setReloadingStrategy(new FileChangedReloadingStrategy());            	
            	config.setAutoSave(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
	
	public String getSpecificValue(String key) {
        String val = config.getString(key);
        //System.out.println(key + " = " + val);
        return val;
    }
}
