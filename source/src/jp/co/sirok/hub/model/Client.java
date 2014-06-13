package jp.co.sirok.hub.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jp.co.sirok.hub.utils.DateUtils;
import jp.co.sirok.hub.utils.JSONObjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class Client extends Model {

	private String id;
	private String applicationId;
	private Date modified;
	private Date created;

	public Client() {
		super();
	}

	public Client(JSONObject jsonObject) {
		super(jsonObject);
	}

	public Client create(String applicationId, String secret) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("applicationId", applicationId);
		params.put("secret", secret);
		JSONObject jsonObject = post("1/clients", params);
		if (jsonObject != null)
			setJsonObject(jsonObject);

		return this;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public JSONObject getJsonObject() {

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("id", getId());
			jsonObject.put("applicationId", getApplicationId());
			jsonObject.put("modified", DateUtils.formatToDateTimeString(getModified()));
			jsonObject.put("created", DateUtils.formatToDateTimeString(getCreated()));
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
			if (JSONObjectUtils.hasAndIsNotNull(jsonObject, "applicationId"))
				setApplicationId(jsonObject.getString("applicationId"));
			if (JSONObjectUtils.hasAndIsNotNull(jsonObject, "modified"))
				setModified(DateUtils.parseFromDateTimeString(jsonObject.getString("modified")));
			if (JSONObjectUtils.hasAndIsNotNull(jsonObject, "created"))
				setCreated(DateUtils.parseFromDateTimeString(jsonObject.getString("created")));
		} catch (JSONException e) {
			throw new IllegalArgumentException("Failed to parse JSON.");
		}

	}

}
