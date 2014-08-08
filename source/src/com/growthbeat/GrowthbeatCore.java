package com.growthbeat;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;

import com.growthbeat.http.GrowthbeatHttpClient;
import com.growthbeat.model.Client;
import com.growthbeat.observer.ClientObserver;

public class GrowthbeatCore {

	private static final String LOGGER_DEFAULT_TAG = "Growthbeat";
	private static final String HTTP_CLIENT_DEFAULT_BASE_URL = "https://api.growthbeat.com/";
	private static final String PREFERENCE_DEFAULT_FILE_NAME = "growthbeat-preferences";
	private static final String PREFERENCE_CLIENT_KEY = "client";

	private static final GrowthbeatCore instance = new GrowthbeatCore();
	private final Logger logger = new Logger();
	private final GrowthbeatHttpClient httpClient = new GrowthbeatHttpClient();
	private final Preference preference = new Preference();

	private Client client;
	private List<ClientObserver> clientObservers = new ArrayList<ClientObserver>();

	private GrowthbeatCore() {
		super();
		logger.setTag(LOGGER_DEFAULT_TAG);
		httpClient.setBaseUrl(HTTP_CLIENT_DEFAULT_BASE_URL);
		preference.setFileName(PREFERENCE_DEFAULT_FILE_NAME);
	}

	public static GrowthbeatCore getInstance() {
		return instance;
	}

	public static void setHttpClientBaseUrl(String baseUrl) {
		instance.getHttpClient().setBaseUrl(baseUrl);
	}

	public static void setPreferenceFileName(String fileName) {
		instance.getPreference().setFileName(fileName);
	}

	public static void setLoggerSilent(boolean silent) {
		getInstance().getLogger().setSilent(silent);
	}

	public void initialize(final Context context, final String applicationId, final String credentialId) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				logger.info(String.format("Initializing... (applicationId:%s)", applicationId));

				preference.setContext(context.getApplicationContext());

				client = loadClient();
				if (client != null && client.getApplication().getId().equals(applicationId)) {
					logger.info(String.format("Client already exists. (id:%s)", client.getId()));
					update(client);
					return;
				}

				preference.removeAll();

				logger.info(String.format("Creating client... (applicationId:%s)", applicationId));
				client = Client.create(applicationId, credentialId);

				if (client == null) {
					logger.info("Failed to create client.");
					return;
				}

				saveClient(client);
				logger.info(String.format("Client created. (id:%s)", client.getId()));
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

		JSONObject clientJsonObject = preference.get(PREFERENCE_CLIENT_KEY);
		if (clientJsonObject == null)
			return null;

		Client client = new Client();
		client.setJsonObject(clientJsonObject);

		return client;

	}

	public synchronized void saveClient(Client client) {

		if (client == null)
			throw new IllegalArgumentException("Argument client cannot be null.");

		preference.save(PREFERENCE_CLIENT_KEY, client.getJsonObject());

	}

	public Logger getLogger() {
		return logger;
	}

	public GrowthbeatHttpClient getHttpClient() {
		return httpClient;
	}

	public Preference getPreference() {
		return preference;
	}

	private static class Thread extends CatchableThread {

		public Thread(Runnable runnable) {
			super(runnable);
		}

		@Override
		public void uncaughtException(java.lang.Thread thread, Throwable e) {
			String message = "Uncaught Exception: " + e.getClass().getName();
			if (e.getMessage() != null)
				message += "; " + e.getMessage();
			getInstance().getLogger().warning(message);
			e.printStackTrace();
		}

	}

}
