package com.parentoff.rest.utils;

import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Map;

import com.parentoff.rest.config.ConfigHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonUtil {
	public static Logger LOGGER = LoggerFactory.getLogger(CommonUtil.class);

	public static <T> String validateFields(Class<T> type,
			Map<String, Object> map) {

		Field[] fields = type.getDeclaredFields();
		Iterator<String> it = map.keySet().iterator();
		while (it.hasNext()) {
			boolean fieldPresent = false;
			String key = it.next();
			for (int i = 0; i < fields.length; i++) {
				if (fields[i].getName().equals(key)) {
					fieldPresent = true;
				}
			}

			if (!fieldPresent) {
				return key;
			}
		}

		return null;
	}

//	public List<String> getCampaign(List<CampaignAgentAssociation> list) {
//		Iterator<CampaignAgentAssociation> it = list.iterator();
//		List<String> campaignIds = new ArrayList<String>();
//		while (it.hasNext()) {
//			campaignIds.add(it.next().getCampaignId());
//		}
//
//		return campaignIds;
//	}

	public static String convertToDate(String timestamp) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(Long.parseLong(timestamp) * 1000);
		return format.format(date).toString();
	}
	
	@SuppressWarnings("deprecation")
	public static String getUnixTimeStamp(String time) {
		java.util.Date date = new java.util.Date(time);
		return Long.toString(new Timestamp(date.getTime()).getTime() / 1000);
	}

	public static String getCurrentDate() {
		String currentTimeStamp = String.valueOf(getCurrentUnixTime());
		String currentDate = convertToDate(currentTimeStamp);
		return currentDate;
	}

	public static long getCurrentUnixTime(){
		long unixTime = System.currentTimeMillis() / 1000L;
		return unixTime;
	}

	public static String getAgencyFromIP(String ip){
		String callServer = null;
		String[] call_centers;
		boolean callCenterMatched = false;

		try{
			call_centers = ConfigHelper.getSpecificValue("call_centres").split(",");
			if(call_centers != null && call_centers.length > 0){

				for (int i = 0; i < call_centers.length; i++) {
					String[] call_center_ips = ConfigHelper.getSpecificValue(call_centers[i]).split(",");

					for(int j=0; j < call_center_ips.length; j++){
						if(ip != null && ip.startsWith(call_center_ips[j])){
							callServer = call_centers[j];
							callCenterMatched = true;
							LOGGER.info("Call Server for IP: " + ip + " is " + callServer);
							break;
						}
					}

					if(callCenterMatched){
						break;
					}
				}
			}
		}catch (Exception e){
			LOGGER.error("getAgencyFromIP() : Exception occurred: " + ip, e);
		}
		return  callServer;
	}

	public static void main(String args[]){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
		for (int i = 0; i < 10; i++) {
			try {
				Thread.sleep(2000);
				String current_time = String.valueOf(getCurrentUnixTime());
				System.out.println(current_time);
				System.out.println(sdf.format(new Date((long)Long.parseLong(current_time)*1000)));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}
}
