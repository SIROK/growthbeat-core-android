package com.growthbeat;

import android.content.Context;

import com.growthbeat.http.GrowthbeatHttpClient;
import com.growthbeat.model.Client;

public class GrowthbeatCore {

	private static final String LOGGER_DEFAULT_TAG = "Growthbeat";
	private static final String HTTP_CLIENT_DEFAULT_BASE_URL = "https://api.growthbeat.com/";
	private static final String PREFERENCE_DEFAULT_FILE_NAME = "growthbeat-preferences";

	private static final GrowthbeatCore instance = new GrowthbeatCore();
	private final Logger logger = new Logger(LOGGER_DEFAULT_TAG);
	private final GrowthbeatHttpClient httpClient = new GrowthbeatHttpClient(HTTP_CLIENT_DEFAULT_BASE_URL);
	private final Preference preference = new Preference(PREFERENCE_DEFAULT_FILE_NAME);

	private Context context = null;
	private Client client;

	private GrowthbeatCore() {
		super();
	}

	public static GrowthbeatCore getInstance() {
		return instance;
	}

	public void initialize(Context context, final String applicationId, final String credentialId) {

		if (this.context != null)
			return;

		this.context = context.getApplicationContext();

		new Thread(new Runnable() {

			@Override
			public void run() {

				logger.info(String.format("Initializing... (applicationId:%s)", applicationId));

				preference.setContext(GrowthbeatCore.this.context);

				client = Client.load();
				if (client != null && client.getApplication().getId().equals(applicationId)) {
					logger.info(String.format("Client already exists. (id:%s)", client.getId()));
					return;
				}

				preference.removeAll();

				logger.info(String.format("Creating client... (applicationId:%s)", applicationId));
				client = Client.create(applicationId, credentialId);

				if (client == null) {
					logger.info("Failed to create client.");
					return;
				}

				Client.save(client);
				logger.info(String.format("Client created. (id:%s)", client.getId()));

			}

		}).start();

	}

	public Client getClient() {
		return client;
	}

	public Client waitClient() {
		while (true) {
			if (client != null)
				return client;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
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

	public Context getContext() {
		return context;
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
