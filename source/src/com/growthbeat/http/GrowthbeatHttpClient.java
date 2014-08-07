package com.growthbeat.http;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.growthbeat.GrowthbeatException;
import com.growthbeat.model.Error;

public class GrowthbeatHttpClient extends BaseHttpClient {

	public GrowthbeatHttpClient() {
		super();
	}

	public JSONObject get(String api, Map<String, Object> params) {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Accept", "application/json");
		HttpRequest httpRequest = new HttpRequest().withMethod("GET").withPath(api).withParameters(params).withHeaders(headers);
		HttpResponse httpResponse = super.request(httpRequest);
		return fetchJSONObject(httpResponse);
	}

	public JSONObject post(String api, Map<String, Object> params) {
		return request("POST", api, params);
	}

	public JSONObject put(String api, Map<String, Object> params) {
		return request("PUT", api, params);
	}

	public JSONObject delete(String api, Map<String, Object> params) {
		return request("DELETE", api, params);
	}

	protected JSONObject request(String method, String api, Map<String, Object> params) {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Accept", "application/json");
		headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		HttpRequest httpRequest = new HttpRequest().withMethod(method).withPath(api).withParameters(params).withHeaders(headers);
		HttpResponse httpResponse = super.request(httpRequest);
		return fetchJSONObject(httpResponse);
	}

	private JSONObject fetchJSONObject(HttpResponse httpResponse) {

		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(httpResponse.getBody());
		} catch (JSONException e) {
			throw new GrowthbeatException("Failed to parse response JSON. " + e.getMessage(), e);
		}

		if (httpResponse.getStatus() < 200 || httpResponse.getStatus() >= 300) {
			Error error = new Error(jsonObject);
			throw new GrowthbeatException(error.getMessage());
		}

		return jsonObject;

	}

}
