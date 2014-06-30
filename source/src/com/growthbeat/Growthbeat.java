package com.growthbeat;

import java.util.ArrayList;
import java.util.List;

import com.growthbeat.http.HttpClient;
import com.growthbeat.model.Client;
import com.growthpush.observer.ClientObserver;

public class Growthbeat {

	private static final Growthbeat instance = new Growthbeat();
	private static final String DEFAULT_BASE_URL = "http://api.localhost:8085/";

	private Client client;
	private List<ClientObserver> clientObservers = new ArrayList<ClientObserver>();

	private Growthbeat() {
		HttpClient.getInstance().setBaseUrl(DEFAULT_BASE_URL);
	}

	public static Growthbeat getInstance() {
		return instance;
	}

	public void initialize(final String applicationId, final String secret) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				Logger.getInstance().info(String.format("Initializing... (applicationId:%s)", applicationId));

				client = Preference.getInstance().fetchClient();
				if (client != null && client.getApplication().getId().equals(applicationId)) {
					Logger.getInstance().info(String.format("Client already exists. (id:%s)", client.getId()));
					update(client);
					return;
				}

				// TODO clear preference

				Logger.getInstance().info(String.format("Creating client... (applicationId:%s)", applicationId));
				client = Client.create(applicationId, secret);

				if (client == null) {
					Logger.getInstance().info("Failed to create client.");
					return;
				}

				Preference.getInstance().saveClient(client);
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

}
