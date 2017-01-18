package com.parentoff.client.rest.outcaller;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;

public class HttpClientService {

	private static HttpClient client = null;
	
	private HttpClientService(){
	}
	
	public static HttpClient getHttpClient(){
		if(client==null){
			synchronized(HttpClientService.class){
				if(client == null){
					MultiThreadedHttpConnectionManager connManag =  new MultiThreadedHttpConnectionManager();
					HttpConnectionManagerParams managParams = connManag.getParams();
		
					managParams.setConnectionTimeout(10000); // 1
					managParams.setSoTimeout(10000); //2
		
					client = new HttpClient(connManag);
				}
			}
		}
		return client;
	}
}
