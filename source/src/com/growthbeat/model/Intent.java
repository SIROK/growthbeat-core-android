package com.growthbeat.model;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.growthbeat.model.type.IntentType;
import com.growthbeat.utils.DateUtils;
import com.growthbeat.utils.JSONObjectUtils;

public class Intent extends Model {

	private int id;
	private String applicationId;
	private String name;
	private IntentType type;
	private Date created;

	public Intent() {
		super();
	}

	public Intent(JSONObject jsonObject) {
		this();
		setJsonObject(jsonObject);
	}

	public static Intent initialize(JSONObject jsonObject) {

		if (!JSONObjectUtils.hasAndIsNotNull(jsonObject, "type"))
			return null;

		IntentType type = null;
		try {
			type = IntentType.valueOf(jsonObject.getString("type"));
		} catch (JSONException e) {
		}

		switch (type) {
		case noop:
			return new NoopIntent(jsonObject);
		case custom:
			return new CustomIntent(jsonObject);
		case url:
			return new UrlIntent(jsonObject);
		default:
			break;
		}

		return new Intent(jsonObject);

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public IntentType getType() {
		return type;
	}

	public void setType(IntentType type) {
		this.type = type;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@Override
	public JSONObject getJsonObject() {

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("id", getId());
			jsonObject.put("applicationId", getApplicationId());
			jsonObject.put("name", getName());
			jsonObject.put("type", getType().toString());
			jsonObject.put("created", getCreated());
		} catch (JSONException e) {
			throw new IllegalArgumentException("Failed to get JSON.");
		}
		return jsonObject;
	}

	@Override
	public void setJsonObject(JSONObject jsonObject) {

		if (jsonObject == null)
			return;

		try {
			if (JSONObjectUtils.hasAndIsNotNull(jsonObject, "id"))
				setId(jsonObject.getInt("id"));
			if (JSONObjectUtils.hasAndIsNotNull(jsonObject, "applicationId"))
				setApplicationId(jsonObject.getString("applicationId"));
			if (JSONObjectUtils.hasAndIsNotNull(jsonObject, "name"))
				setName(jsonObject.getString("name"));
			if (JSONObjectUtils.hasAndIsNotNull(jsonObject, "type"))
				setType(IntentType.valueOf(jsonObject.getString("type")));
			if (JSONObjectUtils.hasAndIsNotNull(jsonObject, "created"))
				setCreated(DateUtils.parseFromDateTimeString(jsonObject.getString("created")));
		} catch (JSONException e) {
			throw new IllegalArgumentException("Failed to parse JSON.");
		}

	}

}
