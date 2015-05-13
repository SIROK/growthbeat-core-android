package com.growthbeat.handler;

import com.growthbeat.model.Intent;
import com.growthbeat.model.type.IntentType;

public class NoopIntentHandler implements IntentHandler {

	@Override
	public boolean handleIntent(Intent intent) {
		return intent.getType() == IntentType.noop;
	}

}
