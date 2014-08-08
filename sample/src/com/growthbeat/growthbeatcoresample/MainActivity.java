package com.growthbeat.growthbeatcoresample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.growthbeat.GrowthbeatCore;
import com.growthbeat.model.Client;
import com.growthbeat.observer.ClientObserver;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		GrowthbeatCore.getInstance().addClientObserver(new ClientObserver() {
			@Override
			public void update(Client client) {
				Log.i("Growthbeat", String.format("Current client ID:%s", client.getId()));
			}
		});
		GrowthbeatCore.getInstance().initialize(this.getApplicationContext(), "dy6VlRMnN3juhW9L", "NuvkVhQtRDG2nrNeDzHXzZO5c6j0Xu5t");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
