package com.growthbeat.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.growthbeat.utils.JSONObjectUtils;

public class CustomIntent extends Intent {

	private Map<String, Object> extra = new HashMap<String, Object>();

	public CustomIntent(JSONObject jsonObject) {
		super(jsonObject);
		setJsonObject(jsonObject);
	}

	public Map<String, Object> getExtra() {
		return extra;
	}

	public void setExtra(Map<String, Object> extra) {
		this.extra = extra;
	}

	@Override
	public JSONObject getJsonObject() {
		JSONObject jsonObject = super.getJsonObject();
		try {
			jsonObject.put("extra", new JSONObject(getExtra()).toString());
		} catch (JSONException e) {
		}

		return jsonObject;
	}

	@Override
	public void setJsonObject(JSONObject jsonObject) {

		if (JSONObjectUtils.hasAndIsNotNull(jsonObject, "extra")) {

			try {
				JSONObject attribute = jsonObject.getJSONObject("extra");
				Map<String, Object> extra = new HashMap<String, Object>();
				Iterator<String> keys = attribute.keys();
				while (keys.hasNext()) {
					String key = keys.next();
					extra.put(key, attribute.get(key));
				}

				setExtra(extra);
			} catch (JSONException e) {
			}

		}

	}

}
