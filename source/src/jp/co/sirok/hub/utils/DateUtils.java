package jp.co.sirok.hub.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	private static final String FORMAT_DATETIME = "yyyy-MM-dd'T'HH:mm:ssZZ";
	private static final String FORMAT_DATE = "yyyy-MM-dd";

	public static String formatToDateTimeString(Date date) {

		if (date == null)
			return null;

		return new SimpleDateFormat(FORMAT_DATETIME).format(date);

	}

	public static String formatToDateString(Date date) {

		if (date == null)
			return null;

		return new SimpleDateFormat(FORMAT_DATE).format(date);

	}

	public static Date parseFromDateTimeString(String date) {

		if (date == null)
			return null;

		try {
			return new SimpleDateFormat(FORMAT_DATETIME).parse(date);
		} catch (ParseException e) {
			return null;
		}

	}

	public static Date parseFromDateString(String date) {

		if (date == null)
			return null;

		try {
			return new SimpleDateFormat(FORMAT_DATE).parse(date);
		} catch (ParseException e) {
			return null;
		}

	}

}
