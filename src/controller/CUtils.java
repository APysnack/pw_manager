package controller;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import structures.User;

import java.util.Base64;
import java.util.Random;

public class CUtils {

	String saltVal = "";
	String hashedPW = "";
	private String AESKey = "";
	private static SecretKeySpec secretKey;
	private static byte[] key;
	
	// SID and AUTH Token values need to be set based on twilio account information for 2fa to work
	public static final String ACCOUNT_SID = "ENTER YOUR TWILIO ACCOUNT_SID HERE";
	public static final String AUTH_TOKEN = "ENTER YOUR TWILIO AUTH_TOKEN HERE";
	public static final String SMS_PHONE_NUMBER = "ENTER YOUR TWILIO PHONE NUMBER HERE";
	public static final int verifMinNum = 100000;
	public static final int verifMaxNum = 999999;

	public CUtils() {
		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
	}

	public String generateKey(String originalPassword) {
		this.saltVal = PassBasedEnc.getSaltvalue(30);
		this.AESKey = PassBasedEnc.generateSecurePassword(originalPassword, saltVal);
		this.hashedPW = PassBasedEnc.generateSecurePassword(AESKey, saltVal);
		return this.hashedPW;
	}

	public String generateAESKey(String input, String saltVal) {
		this.AESKey = PassBasedEnc.generateSecurePassword(input, saltVal);
		return AESKey;
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

	public String encrypt(String input, String originalString, String saltVal) {
		this.AESKey = PassBasedEnc.generateSecurePassword(input, saltVal);
		String encryptedString = encryptData(originalString);
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

	public String getAESKey() {
		return this.AESKey;
	}

	public String getRandomString(boolean lowerAlpha, boolean upperAlpha, boolean numeric, boolean symbols,
			int length) {
		String randChars = "";
		if (lowerAlpha) {
			randChars = randChars + "abcdefghijklmnopqrstuvwxyz";
		}
		if (upperAlpha) {
			randChars = randChars + "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		}
		if (numeric) {
			randChars = randChars + "01234567890";
		}
		if (symbols) {
			randChars = randChars + "~`!@#$%^&*()_-+={[}]|\\:;\"'<,>.?/";
		}

		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < length) { // length of the random string.
			int index = (int) (rnd.nextFloat() * randChars.length());
			salt.append(randChars.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;
	}

	public static boolean isModified(String name, String inputType) {
		if (name.matches("[*]+")) {
			return false;
		}
		if (inputType.equals("password") && name.equals("Enter Password")) {
			return false;
		}
		if (inputType.equals("userName") && name.equals("Enter Username")) {
			return false;
		}
		if (inputType.equals("appName") && name.equals("Application Name")) {
			return false;
		}
		if (inputType.equals("mobileNumber") && name.equals("Enter Mobile Number")) {
			return false;
		}

		return true;
	}

	public static int generateVerificationNumber() {
		int nonce = -1;
		nonce = (int) (Math.random() * (verifMaxNum - verifMinNum + 1) + verifMinNum);
		return nonce;
	}

	// needs to be written: gets user's mobile number (assume sanitized input
	// with a 10 digit number representing in a string: e.g. "4105559672")
	// function should text the user and return true only if the user confirms their
	// login
	public boolean sendMessage(String mobileNumber, int nonce) {

		String verifString = "Please verify your login attempt with Secure Password Manager by entering this number on your screen: "
				+ nonce;

		try {
			Message message = Message
					.creator(new PhoneNumber("+1" + mobileNumber), new PhoneNumber(SMS_PHONE_NUMBER), verifString)
					.create();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// Still needs to be written, should remove ()'s and -'s from phone numbers
	// remove any non-numbers
	public String sanitizeInput(String input, String inputType) {
		if (inputType == "mobileNumber") {
			// remove ()'s and -'s and non-numbers
		}
		return input;
	}

	// more edge cases could be added: input is invalid if it contains spaces
	// (exceptions are the strings: "Enter Password", "Enter Username", "Application
	// Name", and "Enter Mobile Number" for their respective input types)
	// inputTypes can be: userName, password, appName, appUserName, mobileNumber
	public Boolean validateInput(String input, String inputType) {
		// input with spaces are considered valid as long as they are one of the
		// placeholder text field values
		if (!isModified(input, inputType)) {
			return true;
		}

		int maxStringLength;
		int minStringLength;

		// symbols should have been previously removed by sanitizeInput function,
		// leaving length = 10
		if (inputType == "mobileNumber") {
			maxStringLength = 10;
			minStringLength = 10;
			return true;
		}

		// passwords shorter than 12 chars are accepted, but discouraged
		if (inputType == "password") {
			maxStringLength = 128;
			minStringLength = 8;
		} else {
			maxStringLength = 64;
			minStringLength = 4;
		}

		if (inputType != "Application Name") {
			if (input.contains(" ")) {
				return false;
			}
		}

		if (input.length() < minStringLength || input.length() > maxStringLength) {
			return false;
		}

		return true;
	}

}
