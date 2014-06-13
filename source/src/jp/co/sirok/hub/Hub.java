package jp.co.sirok.hub;

import jp.co.sirok.hub.model.Client;

public class Hub {

	private static final Hub instance = new Hub();
	private static final String DEFAULT_BASE_URL = "http://api.localhost:8085/";

	private final Logger logger = new Logger();
	private Client client;

	public static Hub getInstance() {
		return instance;
	}

	public Logger getLogger() {
		return logger;
	}

	public Client getClient() {
		return client;
	}

}
