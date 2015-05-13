package com.growthbeat.model;

import org.json.JSONObject;

public class NoopIntent extends Intent {

	public NoopIntent(JSONObject jsonObject) {
		super(jsonObject);
	}

	@Override
	public JSONObject getJsonObject() {
		return super.getJsonObject();
	}

}
