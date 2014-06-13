package jp.co.sirok.hub.model;

import java.util.Map;

import jp.co.sirok.hub.http.HttpClient;

import org.json.JSONObject;

public abstract class Model {

	public Model() {
		super();
	}

	public Model(JSONObject jsonObject) {
		this();
		setJsonObject(jsonObject);
	}

	public abstract JSONObject getJsonObject();

	public abstract void setJsonObject(JSONObject jsonObject);

	public JSONObject get(final String api, Map<String, Object> params) {
		return HttpClient.sharedInstance().get(api, params);
	}

	public JSONObject post(final String api, Map<String, Object> params) {
		return HttpClient.sharedInstance().post(api, params);
	}

	public JSONObject put(final String api, Map<String, Object> params) {
		return HttpClient.sharedInstance().put(api, params);

	}

	public JSONObject delete(final String api, Map<String, Object> params) {
		return HttpClient.sharedInstance().delete(api, params);
	}

}
