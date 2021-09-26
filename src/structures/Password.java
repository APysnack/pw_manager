package structures;

import java.util.ArrayList;
import java.util.List;

public class Password {

	int pwID;
	int userID;
	String appName;
	String appUserName;
	String encryptedPassword;
	int passwordLength;

	public Password() {

	}

	public Password(int pwID, int userID, String appName, String appUserName, String encryptedPassword) {
		this.pwID = pwID;
		this.userID = userID;
		this.appName = appName;
		this.appUserName = appUserName;
		this.encryptedPassword = encryptedPassword;
	}

	public Password(int pwID, String appName, String appUserName, String encryptedPassword) {
		this.pwID = pwID;
		this.appName = appName;
		this.appUserName = appUserName;
		this.encryptedPassword = encryptedPassword;
	}
	
	public Password(String appName, String appUserName, String encryptedPassword, int pwLength) {
		this.appName = appName;
		this.appUserName = appUserName;
		this.encryptedPassword = encryptedPassword;
		this.passwordLength = pwLength;
	}

	public Password(int pwID, int userID, String appName, String appUserName, String encryptedPassword, int pwLength) {
		this.pwID = pwID;
		this.userID = userID;
		this.appName = appName;
		this.appUserName = appUserName;
		this.encryptedPassword = encryptedPassword;
		this.passwordLength = pwLength;
	}

	public void setAppUserName(String appUserName) {
		this.appUserName = appUserName;
	}
	
	public void setUserID(int userID) {
		this.pwID = userID;
	}
	
	public void setPasswordLength(int passwordLength) {
		this.passwordLength = passwordLength;
	}
	
	public void setPasswordID(int pwID) {
		this.pwID = pwID;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}
	
	public int getPasswordID() {
		return pwID;
	}

	public int getUserID() {
		return userID;
	}

	public int getPasswordLength() {
		return passwordLength;
	}
	
	public String getAppName() {
		return appName;
	}

	public String getEncryptedPassword() {
		return encryptedPassword;
	}

	@Override
	public String toString() {
		String pwInfo = "{pwID: + " + pwID + ", appName: " + appName + ", appUserName: " + appUserName + ", passwordLength: " + passwordLength + "}";
		return pwInfo;
	}
	
	public String getAppUserName() {
		return appUserName;
	}
}
