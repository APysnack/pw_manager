package controller;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class CUtils {

	String saltVal = "";
	String hashedPW = "";
	private String AESKey = "";
	private static SecretKeySpec secretKey;
	private static byte[] key;
	private String hashedInput = "";

	public CUtils() {
	}

	public String generateKey(String originalPassword) {
		this.saltVal = PassBasedEnc.getSaltvalue(30);
		this.AESKey = PassBasedEnc.generateSecurePassword(originalPassword, saltVal);
		this.hashedPW = PassBasedEnc.generateSecurePassword(AESKey, saltVal);
		return this.hashedPW;
	}
	
	public String generateKey(String originalPassword, String saltValue) {
		this.hashedPW = PassBasedEnc.generateSecurePassword(originalPassword, saltValue);
		return this.hashedPW;
	}

	public Boolean verifyPassword(String inputPassword, String dbPassword, String saltValue) {
		String AK = PassBasedEnc.generateSecurePassword(inputPassword, saltValue);
		Boolean result = PassBasedEnc.verifyUserPassword(AK, dbPassword, saltValue);
		return result;
	}

	public String getSaltVal() {
		return this.saltVal;
	}
	public String getEncryptedPassword() {
		return this.hashedPW;
	}
	
	private static void setKey(String myKey) {
		MessageDigest sha = null;
		try {
			key = myKey.getBytes("UTF-8");
			sha = MessageDigest.getInstance("SHA-1");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16);
			secretKey = new SecretKeySpec(key, "AES");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private String encryptData(String strToEncrypt) {
		try {
			setKey(this.AESKey);
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
		} catch (Exception e) {
			System.out.println("Error while encrypting: " + e.toString());
		}
		return null;
	}
	
	
	public String encrypt(String input, String originalPassword) {
		this.saltVal = PassBasedEnc.getSaltvalue(30);
		this.AESKey = PassBasedEnc.generateSecurePassword(input, saltVal);
		String encryptedString = encryptData(originalPassword);
		return encryptedString;
	}

	public String decrypt(String input, String encryptedPassword, String passwordSalt) {
		this.AESKey = PassBasedEnc.generateSecurePassword(input, passwordSalt);
		try {
			setKey(this.AESKey);
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedPassword)));
		} catch (Exception e) {
			System.out.println("Error while decrypting: " + e.toString());
		}
		return null;
	}
	
	public String getHashedInput() {
		return this.hashedInput;
	}

}
