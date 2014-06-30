package com.growthbeat;

import android.util.Log;

public class Logger {

	private static final Logger logger = new Logger();
	private static final String TAG = "Growthbeat";
	private boolean silent = false;

	private Logger() {
		super();
	}

	public static Logger getInstance() {
		return logger;
	}

	public void debug(String message) {

		if (!silent)
			Log.d(TAG, message);

	}

	public void info(String message) {

		if (!silent)
			Log.i(TAG, message);

	}

	public void warning(String message) {

		if (!silent)
			Log.w(TAG, message);

	}

	public void error(String message) {

		if (!silent)
			Log.e(TAG, message);

	}

	public boolean getSilent() {
		return silent;
	}

	public void setSilent(boolean silent) {
		this.silent = silent;
	}

}
