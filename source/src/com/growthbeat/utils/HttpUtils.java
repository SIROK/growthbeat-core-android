package com.growthbeat.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

public class HttpUtils {

	public static final List<NameValuePair> makeNameValuePairs(Map<String, Object> parameters) {

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		for (Map.Entry<String, Object> entry : parameters.entrySet())
			nameValuePairs.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));

		return nameValuePairs;

	}

	public static final Map<String, Object> makeEncryptedParameters(Map<String, Object> parameters, String base64EncodedPublicKey) {

		Map<String, Object> encryptedParameters = new HashMap<String, Object>();
		encryptedParameters.put("publicKey", base64EncodedPublicKey);

		String query = URLEncodedUtils.format(makeNameValuePairs(parameters), "UTF-8");
		String data = RSAUtils.encrypt(query, base64EncodedPublicKey);
		encryptedParameters.put("data", data);

		return encryptedParameters;

	}
}
