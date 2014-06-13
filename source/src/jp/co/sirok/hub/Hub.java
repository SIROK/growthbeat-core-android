package jp.co.sirok.hub;

import jp.co.sirok.hub.http.HttpClient;
import jp.co.sirok.hub.model.Client;

public class Hub {

	private static final Hub instance = new Hub();
	private static final String DEFAULT_BASE_URL = "http://api.localhost:8085/";

	private final Logger logger = new Logger();
	private String secret;
	private Client client;

	private Hub() {
		HttpClient.getInstance().setBaseUrl(DEFAULT_BASE_URL);
	}

	public static Hub getInstance() {
		return instance;
	}

	public void initialize(final String applicationId, final String secret) {

		this.secret = secret;

		this.logger.info(String.format("initialize (applicationId:%s)", applicationId));

		new Thread(new Runnable() {

			@Override
			public void run() {
				Hub.this.client = Client.create(applicationId, secret);
				Hub.this.logger.info(String.format("client created (id:%s)", Hub.this.client.getId()));
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
