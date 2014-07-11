package com.growthbeat.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import com.growthbeat.GrowthbeatException;
import com.growthbeat.model.Error;
import com.growthbeat.utils.IOUtils;

public class HttpClient {

	private static final HttpClient instance = new HttpClient();

	private final DefaultHttpClient apacheHttpClient = new DefaultHttpClient();
	private final int TIMEOUT = 10 * 60 * 1000;
	private String baseUrl = null;
	private Encrypt encrypt = null;

	private HttpClient() {
		HttpConnectionParams.setConnectionTimeout(apacheHttpClient.getParams(), TIMEOUT);
		HttpConnectionParams.setSoTimeout(apacheHttpClient.getParams(), TIMEOUT);
	}

	public static final HttpClient getInstance() {
		return instance;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public void setEncript(String publicKey) {
		this.encrypt = new Encrypt(publicKey);
	}

	public JSONObject get(final String api, Map<String, Object> params) {
		// TODO implemenet
		return null;
	}

	public JSONObject post(final String api, Map<String, Object> params) {

		List<NameValuePair> parameters = this.convertFromParameter(params);

		HttpPost post = new HttpPost(baseUrl + api);
		post.setHeader("Accept", "application/json");
		post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		try {
			post.setEntity(new UrlEncodedFormEntity(parameters, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
		}

		return request(post);

	}

	public JSONObject put(final String api, Map<String, Object> params) {

		List<NameValuePair> parameters = this.convertFromParameter(params);

		HttpPut put = new HttpPut(baseUrl + api);
		put.setHeader("Accept", "application/json");
		put.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		try {
			put.setEntity(new UrlEncodedFormEntity(parameters, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
		}

		return request(put);

	}

	public JSONObject delete(final String api, Map<String, Object> params) {
		// TODO implemenet
		return null;
	}

	private List<NameValuePair> convertFromParameter(Map<String, Object> params) {

		List<NameValuePair> parameters = new ArrayList<NameValuePair>();

		if (this.encrypt == null) {
			for (Map.Entry<String, Object> entry : params.entrySet())
				parameters.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
		} else {

			String query = "";
			for (Map.Entry<String, Object> entry : params.entrySet())
				query = query + "&" + entry.getKey() + "=" + entry.getValue();

			parameters.add(new BasicNameValuePair("publicKey", this.encrypt.getPublicKey()));
			parameters.add(new BasicNameValuePair("data", query));

		}

		return parameters;

	}

	private JSONObject request(final HttpUriRequest httpRequest) {

		HttpResponse httpResponse = null;
		try {
			httpResponse = apacheHttpClient.execute(httpRequest);
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
