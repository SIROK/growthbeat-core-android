package com.growthbeat.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import com.growthbeat.GrowthbeatException;
import com.growthbeat.model.Error;
import com.growthbeat.utils.HttpUtils;
import com.growthbeat.utils.IOUtils;

public class BaseHttpClient {

	private final DefaultHttpClient httpClient = new DefaultHttpClient();
	private final int TIMEOUT = 10 * 60 * 1000;
	private String baseUrl = null;

	public BaseHttpClient() {
		super();
		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpClient.getParams(), TIMEOUT);
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public JSONObject get(String api, Map<String, String> headers, Map<String, Object> params) {
		String query = URLEncodedUtils.format(HttpUtils.makeNameValuePairs(params), "UTF-8");
		HttpGet httpGet = new HttpGet(String.format("%s%s%s", baseUrl, api, (query.length() == 0 ? "" : "?" + query)));
		httpGet.setHeader("Accept", "application/json");
		for (Map.Entry<String, String> entry : headers.entrySet())
			httpGet.setHeader(entry.getKey(), entry.getValue());
		return request(httpGet);
	}

	public JSONObject post(String api, Map<String, String> headers, Map<String, Object> params) {
		return request("POST", api, headers, params);
	}

	public JSONObject put(final String api, Map<String, String> headers, Map<String, Object> params) {
		return request("PUT", api, headers, params);
	}

	public JSONObject delete(final String api, Map<String, String> headers, Map<String, Object> params) {
		return request("DELETE", api, headers, params);
	}

	protected JSONObject request(String method, String api, Map<String, String> headers, Map<String, Object> params) {
		HttpRequest httpRequest = new HttpRequest(String.format("%s%s", baseUrl, api));
		httpRequest.setMethod(method);
		httpRequest.setHeader("Accept", "application/json");
		httpRequest.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		try {
			httpRequest.setEntity(new UrlEncodedFormEntity(HttpUtils.makeNameValuePairs(params), HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
		}
		return request(httpRequest);
	}

	protected JSONObject request(HttpUriRequest httpRequest) {

		HttpResponse httpResponse = null;
		try {
			httpResponse = httpClient.execute(httpRequest);
		} catch (IOException e) {
			throw new GrowthbeatException("Feiled to execute HTTP request. " + e.getMessage(), e);
		}

		JSONObject jsonObject = null;
		try {
			InputStream inputStream = httpResponse.getEntity().getContent();
			String json = IOUtils.toString(inputStream);
			jsonObject = new JSONObject(json);
		} catch (IOException e) {
			throw new GrowthbeatException("Failed to read HTTP response. " + e.getMessage(), e);
		} catch (JSONException e) {
			throw new GrowthbeatException("Failed to parse response JSON. " + e.getMessage(), e);
		} finally {
			try {
				httpResponse.getEntity().consumeContent();
			} catch (IOException e) {
				throw new GrowthbeatException("Failed to close connection. " + e.getMessage(), e);
			}
		}

		int statusCode = httpResponse.getStatusLine().getStatusCode();
		if (statusCode < 200 || statusCode >= 300) {
			Error error = new Error(jsonObject);
			throw new GrowthbeatException(error.getMessage());
		}

		return jsonObject;

	}

}
