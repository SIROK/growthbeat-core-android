package com.growthbeat.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.growthbeat.http.HttpClient;
import com.growthbeat.utils.DateUtils;
import com.growthbeat.utils.JSONObjectUtils;

public class Client extends Model {

	private String id;
	private Date created;
	private Application application;

	public Client() {
		super();
	}

	public Client(JSONObject jsonObject) {
		super(jsonObject);
	}

	public static Client create(String applicationId, String secret) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("applicationId", applicationId);
		params.put("secret", secret);

		JSONObject jsonObject = HttpClient.getInstance().post("1/clients", params);
		if (jsonObject == null)
			return null;

		return new Client(jsonObject);

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

	public JSONObject getJsonObject() {

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("id", getId());
			jsonObject.put("created", DateUtils.formatToDateTimeString(getCreated()));
			jsonObject.put("application", application.getJsonObject());
		} catch (JSONException e) {
			return null;
		}

		return jsonObject;

	}

	public void setJsonObject(JSONObject jsonObject) {

		if (jsonObject == null)
			return;

		try {
			if (JSONObjectUtils.hasAndIsNotNull(jsonObject, "id"))
				setId(jsonObject.getString("id"));
			if (JSONObjectUtils.hasAndIsNotNull(jsonObject, "created"))
				setCreated(DateUtils.parseFromDateTimeString(jsonObject.getString("created")));
			if (JSONObjectUtils.hasAndIsNotNull(jsonObject, "application"))
				setApplication(new Application(jsonObject.getJSONObject("application")));
		} catch (JSONException e) {
			throw new IllegalArgumentException("Failed to parse JSON.");
		}

	}

}
