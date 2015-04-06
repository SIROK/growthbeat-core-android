package com.growthbeat.growthbeatcoresample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

import com.growthbeat.GrowthbeatCore;
import com.growthbeat.model.Client;
import com.growthbeat.model.NoopIntent;
import com.growthbeat.model.UrlIntent;

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
		GrowthbeatCore.getInstance().initialize(this.getApplicationContext(), "OyVa3zboPjHVjsDC", "3EKydeJ0imxJ5WqS22FJfdVamFLgu7XA");

		findViewById(R.id.url_intent).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UrlIntent urlIntent = new UrlIntent();
				urlIntent.setUrl("https://growthbeat.com/");
				GrowthbeatCore.getInstance().handleIntent(urlIntent);
			}
		});

		findViewById(R.id.noop_intent).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				NoopIntent noopIntent = new NoopIntent();
				GrowthbeatCore.getInstance().handleIntent(noopIntent);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
