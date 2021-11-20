package controller;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class CUtils {
	
	private String hashedPassword;
	
	public CUtils() {
		
	}
	
	private static String sha256(String originalString) {
		MessageDigest digest;
		String errorString = "invalid";
		try {
			digest = MessageDigest.getInstance("SHA-256");
			byte[] encodedhash = digest.digest(
					originalString.getBytes(StandardCharsets.UTF_8));
					
			StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
			for (int i = 0; i < encodedhash.length; i++) {
				String hex = Integer.toHexString(0xff & encodedhash[i]);
				if(hex.length() == 1) {
					hexString.append('0');
					}
				hexString.append(hex);
				}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return errorString;
		}
	}
	
	public void setPassword(String originalPassword) {
		this.hashedPassword = sha256(originalPassword);
	}
	
	public String getPassword() {
		return this.hashedPassword;
	}
	
}
