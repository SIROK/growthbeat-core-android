package com.growthbeat;

import android.util.Log;

public class Logger {

	private static final Logger logger = new Logger();

	private boolean silent = false;
	private String tag = null;

	private Logger() {
		super();
	}

	public static Logger getInstance() {
		return logger;
	}

	public void debug(String message) {

		if (!silent)
			Log.d(tag, message);

	}

	public void info(String message) {

		if (!silent)
			Log.i(tag, message);

	}

	public void warning(String message) {

		if (!silent)
			Log.w(tag, message);

	}

	public void error(String message) {

		if (!silent)
			Log.e(tag, message);

	}

	public boolean getSilent() {
		return silent;
	}

	public void setSilent(boolean silent) {
		this.silent = silent;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

}
