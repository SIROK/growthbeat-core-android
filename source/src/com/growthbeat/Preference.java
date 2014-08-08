package com.growthbeat;

import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.growthbeat.utils.IOUtils;

public class Preference {

	private Context context = null;
	private String fileName = null;
	private JSONObject preferences = null;

	public Preference() {
		super();
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public JSONObject get(String key) {

		try {
			return getPreferences().getJSONObject(key);
		} catch (JSONException e) {
			return null;
		}

	}

	public void save(String key, JSONObject jsonObject) {

		JSONObject preferences = getPreferences();
		try {
			preferences.put(key, jsonObject);
		} catch (JSONException e) {
			return;
		}
		saveFile(preferences);

	}

	public void remove(String key) {

		JSONObject preferences = getPreferences();
		preferences.remove(key);
		saveFile(preferences);

	}

	public void removeAll() {
		context.deleteFile(fileName);
	}

	private void saveFile(JSONObject jsonObject) {

		synchronized (this) {
			try {
				FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
				fileOutputStream.write(preferences.toString().getBytes());
				fileOutputStream.flush();
			} catch (IOException e) {
			}
		}

	}

	private synchronized JSONObject getPreferences() {

		if (context == null)
			throw new IllegalStateException("Context is null.");

		if (this.preferences == null) {
			try {
				String json = IOUtils.toString(context.openFileInput(fileName));
				this.preferences = new JSONObject(json);
			} catch (IOException e) {
			} catch (JSONException e) {
			}
		}

		if (this.preferences == null)
			this.preferences = new JSONObject();

		return preferences;

	}

}
