package de.kleindev.tftpserver.utils;

/**
 * Class used to prevent high risk plaintext string
 */
public class StringCrypt {
	/*
	public static String encrypt(String text) {
		try {
			String key = Main.properties.getProperty("Crypt-Key");
			Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
			Cipher cipher;
			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, aesKey);
			byte[] encrypted = cipher.doFinal(text.getBytes());
			return new String(encrypted);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException e) {
			return null;
		}
	}

	public static String decrypt(String text) {
		try {
			String key = Main.properties.getProperty("Crypt-Key");
			Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
			Cipher cipher;
			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, aesKey);
			String decrypted = new String(cipher.doFinal(text.getBytes()));
			return new String(decrypted);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException e) {
			return null;
		}
	}

	 */
}
