package com.growthbeat.growthbeatcoresample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.growthbeat.GrowthbeatCore;
import com.growthbeat.model.Client;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		new Thread(new Runnable() {
			@Override
			public void run() {
				Client client = GrowthbeatCore.getInstance().waitClient();
				Log.i("Growthbeat Core", "client.id:" + client.getId());
			}
		}).start();
		GrowthbeatCore.getInstance().initialize(this.getApplicationContext(), "dy6VlRMnN3juhW9L", "NuvkVhQtRDG2nrNeDzHXzZO5c6j0Xu5t");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
