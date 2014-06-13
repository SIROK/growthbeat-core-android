package jp.co.sirok.hub.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.methods.HttpUriRequest;
import org.json.JSONObject;

public abstract class Model {

	public HashMap<HttpUriRequest, JSONObject> results = new HashMap<HttpUriRequest, JSONObject>();

	public Model() {
		super();
	}

	public Model(JSONObject jsonObject) {

		this();
		setJsonObject(jsonObject);

	}

	public abstract JSONObject getJsonObject();

	public abstract void setJsonObject(JSONObject jsonObject);

	public JSONObject post(final String api, Map<String, Object> params) {
		// TODO implement
		return null;
	}

	public JSONObject put(final String api, Map<String, Object> params) {
		// TODO implement
		return null;

	}

	private JSONObject request(final HttpUriRequest httpRequest) {
		// TODO implement
		return null;
	}

}
