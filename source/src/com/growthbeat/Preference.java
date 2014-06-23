package com.growthbeat;

import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.growthbeat.model.Client;
import com.growthbeat.utils.IOUtils;

public class Preference {

	private static final String FILE_NAME = "growthbeat-preferences";
	private static final String CLIENT_KEY = "client";

	private static Preference instance = new Preference();

	private Context context = null;
	private JSONObject preferences = null;

	private Preference() {
		super();
	}

	public static Preference getInstance() {
		return instance;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public Client fetchClient() {

		JSONObject clientJsonObject = fetch(CLIENT_KEY);
		if (clientJsonObject == null)
			return null;

		Client client = new Client();
		client.setJsonObject(clientJsonObject);

		return client;

	}

	public synchronized void saveClient(Client client) {

		if (client == null)
			throw new IllegalArgumentException("Argument client cannot be null.");

		save(CLIENT_KEY, client.getJsonObject());

	}

	private JSONObject fetch(String key) {

		try {
			return getPreferences().getJSONObject(key);
		} catch (JSONException e) {
			return null;
		}

	}

	private void save(String key, JSONObject jsonObject) {

		JSONObject preferences = getPreferences();
		try {
			preferences.put(key, jsonObject);
		} catch (JSONException e) {
			return;
		}

		synchronized (this) {
			try {
				FileOutputStream fileOutputStream = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
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
				String json = IOUtils.toString(context.openFileInput(FILE_NAME));
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
