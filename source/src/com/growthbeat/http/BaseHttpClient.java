package com.growthbeat.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;

import com.growthbeat.GrowthbeatException;
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

	public HttpResponse get(String api, Map<String, String> headers, Map<String, Object> params) {
		String query = URLEncodedUtils.format(HttpUtils.makeNameValuePairs(params), "UTF-8");
		HttpGet httpGet = new HttpGet(String.format("%s%s%s", baseUrl, api, (query.length() == 0 ? "" : "?" + query)));
		httpGet.setHeader("Accept", "application/json");
		for (Map.Entry<String, String> entry : headers.entrySet())
			httpGet.setHeader(entry.getKey(), entry.getValue());
		return request(httpGet);
	}

	public HttpResponse post(String api, Map<String, String> headers, Map<String, Object> params) {
		return request("POST", api, headers, params);
	}

	public HttpResponse put(final String api, Map<String, String> headers, Map<String, Object> params) {
		return request("PUT", api, headers, params);
	}

	public HttpResponse delete(final String api, Map<String, String> headers, Map<String, Object> params) {
		return request("DELETE", api, headers, params);
	}

	protected HttpResponse request(String method, String api, Map<String, String> headers, Map<String, Object> params) {
		HttpEntityEnclosingRequest httpRequest = new HttpEntityEnclosingRequest(String.format("%s%s", baseUrl, api));
		httpRequest.setMethod(method);
		httpRequest.setHeader("Accept", "application/json");
		httpRequest.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		try {
			httpRequest.setEntity(new UrlEncodedFormEntity(HttpUtils.makeNameValuePairs(params), HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
		}
		return request(httpRequest);
	}

	protected HttpResponse request(HttpUriRequest httpRequest) {

		org.apache.http.HttpResponse httpResponse = null;
		try {
			httpResponse = httpClient.execute(httpRequest);
		} catch (IOException e) {
			throw new GrowthbeatException("Feiled to execute HTTP request. " + e.getMessage(), e);
		}

		String body = null;
		try {
			InputStream inputStream = httpResponse.getEntity().getContent();
			body = IOUtils.toString(inputStream);
		} catch (IOException e) {
			throw new GrowthbeatException("Failed to read HTTP response. " + e.getMessage(), e);
		} finally {
			try {
				httpResponse.getEntity().consumeContent();
			} catch (IOException e) {
				throw new GrowthbeatException("Failed to close connection. " + e.getMessage(), e);
			}
		}

		return new HttpResponse(httpResponse.getStatusLine().getStatusCode(), body);

	}

}
