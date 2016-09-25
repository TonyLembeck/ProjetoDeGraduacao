package br.com.android.sample.view.autenticacao.domain.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256 {

	public static String getSHA256(String senha) {

		MessageDigest algorithm;
		StringBuilder hexString = new StringBuilder();
		byte messageDigest[];
		try {
			algorithm = MessageDigest.getInstance("SHA-256");
				messageDigest = algorithm.digest(senha.getBytes("UTF-8"));
				for (byte b : messageDigest) {
					hexString.append(String.format("%02X", 0xFF & b));
				}
				senha = hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return senha;
	}
}
