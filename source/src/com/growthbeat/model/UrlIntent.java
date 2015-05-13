package com.growthbeat.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.growthbeat.utils.JSONObjectUtils;

public class UrlIntent extends Intent {

	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public UrlIntent(JSONObject jsonObject) {
		super(jsonObject);

	}

	@Override
	public JSONObject getJsonObject() {
		JSONObject jsonObject = super.getJsonObject();
		try {
			jsonObject.put("url", getUrl());
		} catch (JSONException e) {
		}

		return jsonObject;
	}

	@Override
	public void setJsonObject(JSONObject jsonObject) {

		try {
			if (JSONObjectUtils.hasAndIsNotNull(jsonObject, "url"))
				setUrl(jsonObject.getString("url"));
		} catch (JSONException e) {
		}

	}

}
