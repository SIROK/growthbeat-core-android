package com.growthbeat.utils;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import android.util.Base64;

public class RSAUtils {

	private static final String CIPHER_ALGORITHM = "RSA";
	private static final String BLOCK_MODE = "ECB";
	private static final String PADDING_MODE = "PKCS1Padding";
	private static final String TRANSFORMATION = CIPHER_ALGORITHM + "/" + BLOCK_MODE + "/" + PADDING_MODE;

	public static PublicKey getPublicKey(String base64EncodedPublicKey) {

		try {
			byte[] keyData = Base64.decode(base64EncodedPublicKey, Base64.NO_WRAP);
			KeySpec keySpec = new X509EncodedKeySpec(keyData);
			KeyFactory keyFactory = KeyFactory.getInstance(CIPHER_ALGORITHM);
			return keyFactory.generatePublic(keySpec);
		} catch (NoSuchAlgorithmException e) {
			return null;
		} catch (InvalidKeySpecException e) {
			return null;
		}

	}

	public static String encrypt(String data, String base64EncodedPublicKey) {

		byte[] dataBytes = data.getBytes();
		PublicKey publicKey = getPublicKey(base64EncodedPublicKey);
		byte[] encryptedDataBytes = encrypt(dataBytes, publicKey);
		return Base64.encodeToString(encryptedDataBytes, Base64.NO_WRAP);

	}

	public static byte[] encrypt(byte[] data, PublicKey publicKey) {

		try {
			Cipher encryptCipher = Cipher.getInstance(TRANSFORMATION);
			encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return encryptCipher.doFinal(data);
		} catch (IllegalBlockSizeException e) {
			return null;
		} catch (BadPaddingException e) {
			return null;
		} catch (InvalidKeyException e) {
			return null;
		} catch (NoSuchAlgorithmException e) {
			return null;
		} catch (NoSuchPaddingException e) {
			return null;
		}

	}

}
