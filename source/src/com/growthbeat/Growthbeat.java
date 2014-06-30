package com.growthbeat;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.growthbeat.http.HttpClient;
import com.growthbeat.model.Client;
import com.growthpush.observer.ClientObserver;

public class Growthbeat {

	private static final String HTTP_CLIENT_DEFAULT_BASE_URL = "https://api.growthbeat.com/";
	private static final String PREFERENCE_DEFAULT_FILE_NAME = "growthbeat-preferences";
	private static final String PREFERENCE_CLIENT_KEY = "client";

	private static final Growthbeat instance = new Growthbeat();

	private Client client;
	private List<ClientObserver> clientObservers = new ArrayList<ClientObserver>();

	private Growthbeat() {
		if (HttpClient.getInstance().getBaseUrl() == null)
			HttpClient.getInstance().setBaseUrl(HTTP_CLIENT_DEFAULT_BASE_URL);
		if (Preference.getInstance().getFileName() == null)
			Preference.getInstance().setFileName(PREFERENCE_DEFAULT_FILE_NAME);
	}

	public static Growthbeat getInstance() {
		return instance;
	}

	public static void setHttpClientBaseUrl(String baseUrl) {
		HttpClient.getInstance().setBaseUrl(baseUrl);
	}

	public static void setPreferenceFileName(String fileName) {
		Preference.getInstance().setFileName(fileName);
	}

	public static void setLoggerSilent(boolean silent) {
		Logger.getInstance().setSilent(silent);
	}

	public void initialize(final String applicationId, final String credentialId) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				Logger.getInstance().info(String.format("Initializing... (applicationId:%s)", applicationId));

				client = loadClient();
				if (client != null && client.getApplication().getId().equals(applicationId)) {
					Logger.getInstance().info(String.format("Client already exists. (id:%s)", client.getId()));
					update(client);
					return;
				}

				Preference.getInstance().removeAll();

				Logger.getInstance().info(String.format("Creating client... (applicationId:%s)", applicationId));
				client = Client.create(applicationId, credentialId);

				if (client == null) {
					Logger.getInstance().info("Failed to create client.");
					return;
				}

				saveClient(client);
				Logger.getInstance().info(String.format("lient created. (id:%s)", client.getId()));
				update(client);

			}

		}).start();

	}

	public Client getClient() {
		return client;
	}

	public void addClientObserver(ClientObserver clientObserver) {
		clientObservers.add(clientObserver);
	}

	public void removeClientObserver(ClientObserver clientObserver) {
		clientObservers.remove(clientObserver);
	}

	public void update(Client client) {
		for (ClientObserver clientObserver : clientObservers) {
			clientObserver.update(client);
		}
	}

	public Client loadClient() {

		JSONObject clientJsonObject = Preference.getInstance().get(PREFERENCE_CLIENT_KEY);
		if (clientJsonObject == null)
			return null;

		Client client = new Client();
		client.setJsonObject(clientJsonObject);

		return client;

	}

	public synchronized void saveClient(Client client) {

		if (client == null)
			throw new IllegalArgumentException("Argument client cannot be null.");

		Preference.getInstance().save(PREFERENCE_CLIENT_KEY, client.getJsonObject());

	}

}
