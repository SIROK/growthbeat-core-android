package com.growthbeat;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.growthbeat.model.Intent;

public class UrlIntentHandler implements IntentHandler {

	Context context;
	
	public UrlIntentHandler(Context context)
	{
		this.context = context;
	}
	
	@Override
	public boolean handleIntent(Intent intent) {
		if (intent.getType().equals("url"))
		{
			boolean error = false;
			try
			{
				Uri uri = Uri.parse(intent.getUrl());
				android.content.Intent i = new android.content.Intent(android.content.Intent.ACTION_VIEW, uri);
				i.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(i);
			}
			catch (Exception e)
			{
				Log.e("exception", e.toString());
				error = true;
			}
			finally
			{
				
			}
			return !error;
		}
		else
			return false;
	}

}
