package com.pcc.sys.lib;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class FEncrypterData {

	private final String keyString = "PEESOFTS";

	public String encode(String str) {

		try {
			byte[] utf8 = str.getBytes("UTF-8");
			byte[] keyByte = keyString.getBytes();
			SecretKeySpec key = new SecretKeySpec(keyByte, 0, keyByte.length, "DES");

			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, key);

			byte[] enc = cipher.doFinal(utf8);
			return Base64.encodeBase64String(enc);

		} catch (UnsupportedEncodingException | NoSuchAlgorithmException
				| NoSuchPaddingException | InvalidKeyException
				| IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
			return "";

		}

	}

	public String decode(String str) {

		try {
			byte[] dat1 = Base64.decodeBase64(str);
			byte[] keyByte = keyString.getBytes();
			SecretKeySpec key = new SecretKeySpec(keyByte, 0, keyByte.length, "DES");

			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, key);

			byte[] enc = cipher.doFinal(dat1);
			return new String(enc, "UTF-8");

		} catch (UnsupportedEncodingException | NoSuchAlgorithmException
				| NoSuchPaddingException | InvalidKeyException
				| IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
			return "";

		}

	}

	public static void main(String[] args) {

		//TEST OK
		FEncrypterData enc1 = new FEncrypterData();
		try {
			String s = "This is a test";
			String ss1 = enc1.encode(s);
			System.out.println(ss1);
			String ss2 = enc1.decode(ss1);
			System.out.println(ss2);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
