package com.growthbeat.http;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class GrowthbeatHttpClient extends BaseHttpClient {

	public GrowthbeatHttpClient() {
		super();
	}

	public JSONObject get(String api, Map<String, Object> params) {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Accept", "application/json");
		return super.get(api, headers, params);
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
		return super.request(method, api, headers, params);
	}

}
