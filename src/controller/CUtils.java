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
	
	PassBasedEnc pbe;
	String saltValue = "";
	String encryptedPW = "";
	
	public CUtils() {
		this.pbe = new PassBasedEnc();
	}
	
	public void encryptPassword(String originalPassword) {
		this.saltValue = pbe.getSaltvalue(30);
		this.encryptedPW = pbe.generateSecurePassword(originalPassword, saltValue);
	}
	
	public Boolean verifyPassword(String inputPassword, String dbPassword, String saltValue) {
		Boolean result = pbe.verifyUserPassword(inputPassword, dbPassword, saltValue);
		return result;
	}
	
	public String getSaltVal() {
		return this.saltValue;
	}
	
	public String getEncryptedPassword() {
		return this.encryptedPW;
	}
	
}
	
