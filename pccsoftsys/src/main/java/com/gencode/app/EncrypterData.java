/*
 * The MIT License
 *
 * Copyright (c) 2020 preecha daroonpunth (prch13@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.gencode.app;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class EncrypterData {

	private final String keyString = "PEESOFTS";

	public String encode(String str) {

		try {
			byte[] utf8 = str.getBytes("UTF8");
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
			return new String(enc, "UTF8");

		} catch (UnsupportedEncodingException | NoSuchAlgorithmException
				| NoSuchPaddingException | InvalidKeyException
				| IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
			return "";

		}

	}

//	public static void main(String[] args) {
//
//		//TEST OK
//		EncrypterData enc1 = new EncrypterData();
//		try {
//			String s = "This is a test";
//			String ss1 = enc1.encode(s);
//			System.out.println(ss1);
//			String ss2 = enc1.decode(ss1);
//			System.out.println(ss2);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}

}
