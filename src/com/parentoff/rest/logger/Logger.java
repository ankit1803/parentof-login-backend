package com.parentoff.rest.logger;

import java.util.HashMap;

public class Logger {

	public static void writeLog(String trackType, HashMap<String, Object> mp) {
		try {
			FileTracker fileTracker = new FileTracker(trackType);
			fileTracker.createMap(mp);
			fileTracker.writeInfile();
		} catch (Exception e) {
			System.err.println("Could not log: " + e);
		}
	}
}