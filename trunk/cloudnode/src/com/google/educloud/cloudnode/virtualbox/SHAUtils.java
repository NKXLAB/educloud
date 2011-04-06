package com.google.educloud.cloudnode.virtualbox;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHAUtils {

	/**
	 * It will use SHA-256 hashing algorithm to generate a hash value for a password.
	 *
	 * @param password
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String generateHash(String password) throws NoSuchAlgorithmException {

		MessageDigest md = MessageDigest.getInstance("SHA-256");

        md.update(password.getBytes());
        byte byteData[] = md.digest();

        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
	}
}
