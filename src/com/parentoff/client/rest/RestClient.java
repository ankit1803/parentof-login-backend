package com.parentoff.client.rest;

import com.parentoff.client.rest.outcaller.HttpClientService;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by pooja on 27/8/15.
 */
public class RestClient {

    static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(RestClient.class);

    public static String get(String url, Map<String, String> queryMap){
        HttpClient client = HttpClientService.getHttpClient();
        GetMethod method = new GetMethod(url);;
        String response = null;
        try {
            List<NameValuePair> queryParamList = new ArrayList<>();
            for(String key:queryMap.keySet()){
                queryParamList.add(new NameValuePair(key, queryMap.get(key)));
            }
            NameValuePair[] queryParams = new NameValuePair[queryParamList.size()];


            method.setQueryString(queryParamList.toArray(queryParams));
            LOGGER.info("sms request params: " + method.getQueryString());

            int statusCode  = client.executeMethod(method);
            if (statusCode != HttpStatus.SC_OK) {
                LOGGER.error("Method failed: ", method.getStatusLine());
            }
            byte[] responseBody = method.getResponseBody();
            response = new String(responseBody);
        }catch (HttpException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }finally {
            method.releaseConnection();
        }
        return response;
    }

    public static void main(String[] args) {
        String json = "{\"requests\":[{\"requestId\":\"16307820\",\"callDate\" : \"1440666058\"},{\"requestId\":\"16390398\",\"callDate\" : \"1440666058\"}]}";
        String url = "http://localhost:7575/recordings/recordingList";
    }
}
