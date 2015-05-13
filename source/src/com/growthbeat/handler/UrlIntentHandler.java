package com.growthbeat.handler;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.growthbeat.model.Intent;
import com.growthbeat.model.UrlIntent;
import com.growthbeat.model.type.IntentType;

public class UrlIntentHandler implements IntentHandler {

	Context context;

	public UrlIntentHandler(Context context) {
		this.context = context;
	}

	@Override
	public boolean handleIntent(Intent intent) {
		if (intent.getType() == IntentType.url) {

			try {
				Uri uri = Uri.parse(((UrlIntent) intent).getUrl());
				android.content.Intent i = new android.content.Intent(android.content.Intent.ACTION_VIEW, uri);
				i.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(i);
			} catch (Exception e) {
				Log.e("exception", e.toString());
			}

			return true;
		}

		return false;
	}

}
