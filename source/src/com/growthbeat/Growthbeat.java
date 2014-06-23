package com.growthbeat;

import com.growthbeat.http.HttpClient;
import com.growthbeat.model.Client;

public class Growthbeat {

	private static final Growthbeat instance = new Growthbeat();
	private static final String DEFAULT_BASE_URL = "http://api.localhost:8085/";

	private final Logger logger = new Logger();
	private String secret;
	private Client client;

	private Growthbeat() {
		HttpClient.getInstance().setBaseUrl(DEFAULT_BASE_URL);
	}

	public static Growthbeat getInstance() {
		return instance;
	}

	public void initialize(final String applicationId, final String secret) {

		this.secret = secret;

		this.logger.info(String.format("initialize (applicationId:%s)", applicationId));

		new Thread(new Runnable() {

			@Override
			public void run() {
				Growthbeat.this.client = Client.create(applicationId, Growthbeat.this.secret);
				Growthbeat.this.logger.info(String.format("client created (id:%s)", Growthbeat.this.client.getId()));
			}
		}).start();

	}

	public Logger getLogger() {
		return logger;
	}

	public Client getClient() {
		return client;
	}

}
