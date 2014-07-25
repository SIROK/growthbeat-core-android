package com.growthbeat.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

public class HttpUtils {

	public static final UrlEncodedFormEntity makeUrlEncodedFormEntity(Map<String, Object> parameters) {

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		for (Map.Entry<String, Object> entry : parameters.entrySet())
			nameValuePairs.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));

		try {
			return new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8);
		} catch (UnsupportedEncodingException e) {
			return null;
		}

	}

	public static final Map<String, Object> makeEncryptedParameters(Map<String, Object> parameters, String base64EncodedPublicKey) {

		Map<String, Object> encryptedParameters = new HashMap<String, Object>();

		encryptedParameters.put("publicKey", base64EncodedPublicKey);

		UrlEncodedFormEntity urlEncodedFormEntity = makeUrlEncodedFormEntity(parameters);
		try {
			String urlEncodedFormEntityString = IOUtils.toString(urlEncodedFormEntity.getContent());
			String data = RSAUtils.encrypt(urlEncodedFormEntityString, base64EncodedPublicKey);
			encryptedParameters.put("data", data);
		} catch (IOException e) {
			return null;
		}

		return encryptedParameters;

	}
}
