package com.growthbeat;

import com.growthbeat.model.Intent;

public class NoopIntentHandler implements IntentHandler {

	@Override
	public boolean handleIntent(Intent intent) {
		if (intent.getType().equals("noop"))
			return true;
		else 
			return false;
	}

}
