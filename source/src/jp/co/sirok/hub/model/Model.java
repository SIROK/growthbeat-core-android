package jp.co.sirok.hub.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONObject;

public abstract class Model {

	private final HttpClient httpClient = new DefaultHttpClient();
	private int TIMEOUT = 10 * 60 * 1000;

	public HashMap<HttpUriRequest, JSONObject> results = new HashMap<HttpUriRequest, JSONObject>();

	public Model() {

		super();
		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpClient.getParams(), TIMEOUT);

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
