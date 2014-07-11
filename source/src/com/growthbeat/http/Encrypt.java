package com.growthbeat.http;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;
import java.util.Map.Entry;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import android.util.Base64;

import com.growthbeat.GrowthbeatException;

public class Encrypt {

	private static final String CIPHER_ALGORITHM = "RSA";
	private static final String CIPHER_MODE = CIPHER_ALGORITHM + "/ECB/PKCS1PADDING";
	private Cipher cipher;
	private Key publicKey;
	private String rawPublicKey;

	public Encrypt(String publicKey) {
		setPublicKey(publicKey);
	}

	public String encryptParams(Map<String, Object> params) {

		String query = "";
		for (Entry<String, Object> param : params.entrySet())
			query = query + "&" + param.getKey() + "=" + param.getValue();

		try {
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return Base64.encodeToString(cipher.doFinal(query.getBytes()), Base64.DEFAULT);
		} catch (InvalidKeyException e) {
		} catch (IllegalBlockSizeException e) {
		} catch (BadPaddingException e) {
		}

		throw new GrowthbeatException("encrypt fail.");
	}

	public Encrypt setPublicKey(String key) {

		try {

			this.rawPublicKey = key;

			if (cipher == null)
				cipher = Cipher.getInstance(CIPHER_MODE);

			KeyFactory keyFactory = KeyFactory.getInstance(CIPHER_ALGORITHM);
			this.publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(key.getBytes()));
			return this;

		} catch (NoSuchAlgorithmException e) {
		} catch (NoSuchPaddingException e) {
		} catch (InvalidKeySpecException e) {
		}

		throw new GrowthbeatException("public key is invalid.");

	}

	public String getPublicKey() {
		return this.rawPublicKey;
	}

}
