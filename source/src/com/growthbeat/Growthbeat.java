package com.growthbeat;

import com.growthbeat.http.HttpClient;
import com.growthbeat.model.Client;

public class Growthbeat {

	private static final Growthbeat instance = new Growthbeat();
	private static final String DEFAULT_BASE_URL = "http://api.localhost:8085/";

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

		Logger.getInstance().info(String.format("initialize (applicationId:%s)", applicationId));

		new Thread(new Runnable() {

			@Override
			public void run() {
				Growthbeat.this.client = Client.create(applicationId, Growthbeat.this.secret);
				Logger.getInstance().info(String.format("client created (id:%s)", Growthbeat.this.client.getId()));
			}
		}).start();

	}

	public Client getClient() {
		return client;
	}

}
