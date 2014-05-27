package com.example.nelsonmerc;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.Header;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class Meli {
    public static String apiUrl = "https://api.mercadolibre.com";
    String response;
    private String accessToken;
    private String refreshToken;
    private Long clientId;
    private String clientSecret;
    private AsyncHttpClient http;
    {
    	http = new AsyncHttpClient();
    } 

    public Meli(Long clientId, String clientSecret) {
	this.clientId = clientId;
	this.clientSecret = clientSecret;
    }

    public Meli(Long clientId, String clientSecret, String accessToken) {
	this.accessToken = accessToken;
	this.clientId = clientId;
	this.clientSecret = clientSecret;
    }

    public Meli(Long clientId, String clientSecret, String accessToken,
	    String refreshToken) {
	this.accessToken = accessToken;
	this.clientId = clientId;
	this.clientSecret = clientSecret;
	this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
	return this.accessToken;
    }

    public String getRefreshToken() {
	return this.refreshToken;
    }


 //   public String get(String path) { //throws MeliException {
   // 	return get(path, new FluentStringsMap());
  // }

    public void get(String path, RequestParams params, final Handler responseHandler) {//throws MeliException {
    	String completeUrl = apiUrl + path;
    	http.get(completeUrl, params, new JsonHttpResponseHandler() {
		    @Override
		    public void onSuccess(JSONObject theResponse){
		    	Message msg = new Message();
		    	msg.obj = theResponse;
		    	msg.what = 0;//success
		    	responseHandler.sendMessage(msg);
		    }
		     @Override
		     public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
		     {
		    	 Message msg = new Message();
		    	 msg.obj = error;
		    	 msg.what = 1;//failure
		    	 responseHandler.sendMessage(msg);
		     }
		     @Override
		     public void onProgress(int bytesWritten, int totalSize) {
		     }
		});
    }
    
    public void post(String path, RequestParams params, final Handler responseHandler){
    	String completeUrl = apiUrl + path;
    	http.post(completeUrl, params, new JsonHttpResponseHandler() {
    		@Override
 		    public void onSuccess(JSONObject theResponse){
 		    	Message msg = new Message();
 		    	msg.obj = theResponse;
 		    	msg.what = 0;//success
 		    	responseHandler.sendMessage(msg);
 		    }
 		    @Override
 		    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error){
 		    	Message msg = new Message();
 		    	msg.obj = error;
 		    	msg.what = 1;//failure
 		    	responseHandler.sendMessage(msg);
 		     }
 		    @Override
 		    public void onProgress(int bytesWritten, int totalSize) {
 		    }
		});
    }

    public void put(String path, RequestParams params, final Handler responseHandler){
    	String completeUrl = apiUrl + path;
    	http.put(completeUrl, params, new JsonHttpResponseHandler() {
    		@Override
 		    public void onSuccess(JSONObject theResponse){
 		    	Message msg = new Message();
 		    	msg.obj = theResponse;
 		    	msg.what = 0;//success
 		    	responseHandler.sendMessage(msg);
 		    }
 		    @Override
 		    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error){
 		    	Message msg = new Message();
 		    	msg.obj = error;
 		    	msg.what = 1;//failure
 		    	responseHandler.sendMessage(msg);
 		    }
 		    @Override
 		    public void onProgress(int bytesWritten, int totalSize) {
 		    }
		});
    }
    /*
    public void delete(String path, RequestParams params, final Handler responseHandler){
    	String completeUrl = apiUrl + path;
    	http.delete(getContext(), completeUrl, null, params, responseHandler);
    }
    */
    
/*
    private void refreshAccessToken() throws AuthorizationFailure {
	FluentStringsMap params = new FluentStringsMap();
	params.add("grant_type", "refresh_token");
	params.add("client_id", String.valueOf(this.clientId));
	params.add("client_secret", this.clientSecret);
	params.add("refresh_token", this.refreshToken);
	BoundRequestBuilder req = preparePost("/oauth/token", params);

	parseToken(req);
    }
*/
    public String getAuthUrl(String callback) {
		try {
		    return "https://auth.mercadolibre.com.ar/authorization?response_type=code&client_id="
			    + clientId
			    + "&redirect_uri="
			    + URLEncoder.encode(callback, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		    return "https://auth.mercadolibre.com.ar/authorization?response_type=code&client_id="
			    + clientId + "&redirect_uri=" + callback;
		}
	}

    public void authorize(String code, String redirectUri){//throw exception
	
	    RequestParams params = new RequestParams();
	
		params.put("grant_type", "authorization_code");
		params.put("client_id", String.valueOf(clientId));
		params.put("client_secret", clientSecret);
		params.put("code", code);
		params.put("redirect_uri", redirectUri);
	
		parseToken("/oauth/token", params);
    }

    private void parseToken(String path, RequestParams params){ //throw  throws AuthorizationFailure 
		response = null;
		String url = apiUrl + path;

		http.post(url, params, new AsyncHttpResponseHandler() {
		    @Override
		    public void onSuccess(String theResponse) {
		        extractAccessToken(theResponse);
		    }

		     @Override
		     public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
		     {
		         //DO SOMETHING ON FAILURE
		     }
		});
    }

	private void extractAccessToken(String response) {
		JsonParser p = new JsonParser();
		JsonObject object;
		object = p.parse(response).getAsJsonObject();
		accessToken = object.get("access_token").getAsString();
		try{
			refreshToken = object.get("refresh_token").getAsString();
		} catch(Exception ex){
			//DO SOMETHING
		}
		
	}
	
    @SuppressLint("NewApi")
	private boolean hasRefreshToken() {
	return this.refreshToken != null && !this.refreshToken.isEmpty();
    }
}