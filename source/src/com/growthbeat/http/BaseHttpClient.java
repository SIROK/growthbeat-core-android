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

	public HttpResponse request(HttpRequest httpRequest) {

		if (httpRequest.getMethod() != null && httpRequest.getMethod().equalsIgnoreCase("GET")) {
			String query = URLEncodedUtils.format(HttpUtils.makeNameValuePairs(httpRequest.getParameters()), "UTF-8");
			HttpGet httpGet = new HttpGet(String.format("%s%s%s", baseUrl, httpRequest.getPath(), (query.length() == 0 ? "" : "?" + query)));
			for (Map.Entry<String, String> entry : httpRequest.getHeaders().entrySet())
				httpGet.setHeader(entry.getKey(), entry.getValue());
			return request(httpGet);
		} else {
			HttpEntityEnclosingRequest httpEntityEnclosingRequest = new HttpEntityEnclosingRequest(String.format("%s%s", baseUrl,
					httpRequest.getPath()));
			httpEntityEnclosingRequest.setMethod(httpRequest.getMethod());
			for (Map.Entry<String, String> entry : httpRequest.getHeaders().entrySet())
				httpEntityEnclosingRequest.setHeader(entry.getKey(), entry.getValue());
			try {
				httpEntityEnclosingRequest.setEntity(new UrlEncodedFormEntity(HttpUtils.makeNameValuePairs(httpRequest.getParameters()),
						HTTP.UTF_8));
			} catch (UnsupportedEncodingException e) {
			}
			return request(httpEntityEnclosingRequest);
		}

	}

	protected HttpResponse request(HttpUriRequest httpUriRequest) {

		org.apache.http.HttpResponse httpResponse = null;
		try {
			httpResponse = httpClient.execute(httpUriRequest);
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
