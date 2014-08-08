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
	private static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArKub5NHfMSEAHNpbKZDgoe69gyLd0BYi2HFjcc13vvegAGA1zNqDGqFdn2QHR2xlDAnfAJpiGQz7p3QwC+Ic1VZ6KaYSeWRWPeYAjjjnhVKxFH7lJ47hKdZAaj3P7138r+bljiRuDoKwupXH+jfReC4/WCNcvpzuCLeUJuXRZ/xrABRj3EE4gQItsHPT3YpP3/1uTB1P7Qu0DI0kMPcmsNqJe4U0eU1tySffiVlg2+ORurojX4ab4atfNdO9YDUoMTe76FrTAKAmFBu5LnOpZaB2r56i0FUbkH9ZYEbatvVOFBJK6oJaH6KbK65Y1qxVydh790ACxY21np/OB2T2qQIDAQAB";

	private static final GrowthbeatCore instance = new GrowthbeatCore();
	private static final Logger logger = new Logger();
	private static final GrowthbeatHttpClient httpClient = new GrowthbeatHttpClient();

	private Client client;
	private List<ClientObserver> clientObservers = new ArrayList<ClientObserver>();

	private GrowthbeatCore() {
		logger.setTag(LOGGER_DEFAULT_TAG);
		httpClient.setBaseUrl(HTTP_CLIENT_DEFAULT_BASE_URL);
		if (Preference.getInstance().getFileName() == null)
			Preference.getInstance().setFileName(PREFERENCE_DEFAULT_FILE_NAME);
	}

	public static GrowthbeatCore getInstance() {
		return instance;
	}

	public static void setHttpClientBaseUrl(String baseUrl) {
		httpClient.setBaseUrl(baseUrl);
	}

	public static void setPreferenceFileName(String fileName) {
		Preference.getInstance().setFileName(fileName);
	}

	public static void setLoggerSilent(boolean silent) {
		logger.setSilent(silent);
	}

	public void initialize(final Context context, final String applicationId, final String credentialId) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				logger.info(String.format("Initializing... (applicationId:%s)", applicationId));

				Preference.getInstance().setContext(context.getApplicationContext());

				client = loadClient();
				if (client != null && client.getApplication().getId().equals(applicationId)) {
					logger.info(String.format("Client already exists. (id:%s)", client.getId()));
					update(client);
					return;
				}

				Preference.getInstance().removeAll();

				logger.info(String.format("Creating client... (applicationId:%s)", applicationId));
				client = Client.create(applicationId, credentialId, PUBLIC_KEY);

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

	public Logger getLogger() {
		return logger;
	}

	public GrowthbeatHttpClient getHttpClient() {
		return httpClient;
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
			logger.warning(message);
		}

	}

}