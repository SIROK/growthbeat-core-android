package com.growthbeat.utils;

import java.util.Locale;
import java.util.TimeZone;

import android.os.Build;

public final class DeviceUtils {

	public static String getModel() {
		return Build.MODEL;
	}

	public static String getDevice() {
		return Build.DEVICE;
	}

	public static String getOsVersion() {
		return Build.VERSION.RELEASE;
	}

	public static int getApiVersion() {
		return Build.VERSION.SDK_INT;
	}

	public static String getCountry() {
		return Locale.getDefault().getCountry();
	}

	public static String getLanguage() {
		return Locale.getDefault().getLanguage();
	}

	public static String getTimeZone() {
		return TimeZone.getDefault().getID();
	}

}
