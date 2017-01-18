package com.parentoff.rest.logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.parentoff.rest.config.ConfigHelper;

public class FileTracker {
	private static Map<String, ExecutorService> poolMap = new HashMap<String, ExecutorService>();
	private static Map<String, FileWriter> fileWriterPool = new HashMap<String, FileWriter>();

	private Map<String, Object> data = new TreeMap<String, Object>();

	private String trackType = null;

	public FileTracker(String trackType) {
		this.trackType = trackType;
	}

	public void writeInfile() {
		String dir = ConfigHelper.getSpecificValue("logLocation");
		if (dir == null)
			dir = "logs";
		File dirf = new File(dir);
		if (!dirf.exists())
			dirf.mkdir();
		if (dir.endsWith("/"))
			dir = dir.substring(0, dir.length() - 1);

		String mnth = (Calendar.getInstance().get(Calendar.MONTH) + 1) + "";
		String dt = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "";

		if (mnth.length() < 2)
			mnth = "0" + mnth;
		if (dt.length() < 2)
			dt = "0" + dt;

		String fileName = dir + "/" + trackType + "_"
				+ Calendar.getInstance().get(Calendar.YEAR) + "_" + mnth + "_"
				+ dt + ".log";

		ExecutorService pool = poolMap.get(fileName);
		FileWriter fw = fileWriterPool.get(fileName);
		if (pool == null || fw == null) {
			// Removing pool for yesterday and adding new pool for the new file
			synchronized (poolMap) {
				Iterator<String> it = poolMap.keySet().iterator();
				ArrayList<String> removalList = new ArrayList<String>();
				String key;
				while (it.hasNext()) {
					key = it.next();
					if (key.startsWith(dir)
							&& key.contains(trackType.toString())
							&& key.length() == fileName.length()) {
						removalList.add(key);
					}
				}
				for (String s : removalList) {
					poolMap.remove(s);
					FileWriter fw_remove = fileWriterPool.get(s);
					if (fw_remove != null) {
						try {
							fw_remove.flush();
							fw_remove.close();
						} catch (IOException e) {
							System.err.println("Could not close writer pool: " + e);
						}
					}
					fileWriterPool.remove(s);
				}
			}
			File f = new File(fileName);
			try {
				fw = new FileWriter(f, true);
				pool = Executors.newSingleThreadExecutor();
				poolMap.put(fileName, pool);
				fileWriterPool.put(fileName, fw);
			} catch (Exception e) {
				System.err.println("Could not write to file: " + e);
			}
		}
		if (fw != null)
			pool.execute(new LogWriter(fw, data));
	}

	public Map<String, Object> createMap(String key, Object value) {
		data.put(key, value);
		return data;
	}

	public Map<String, Object> createMap(Map<String, Object> mp) {
		data.putAll(mp);
		return data;
	}

	public Map<String, Object> createMap_String(Map<String, String> mp) {
		data.putAll(mp);
		return data;
	}

	private class LogWriter implements Runnable {

		FileWriter fw;
		Map<String, Object> data;

		public LogWriter(FileWriter fileName, Map<String, Object> data) {
			this.fw = fileName;
			this.data = data;
		}

		@Override
		public void run() {
			try {
				StringBuffer toWrite = new StringBuffer();
				Iterator<String> it = data.keySet().iterator();
				while (it.hasNext()) {
					String key = (String) it.next();
					toWrite.append(key + ":" + data.get(key) + "   ");
				}
				fw.append("### " + Calendar.getInstance().getTime() + " "
						+ toWrite.toString() + "\n");
				fw.flush();
			} catch (Exception e) {
				System.err.println("Could not write to file: " + e);
			}
		}
	}
}