package pw_manager;

import java.util.ArrayList;
import java.util.List;

public class Password {

	int userID;
	int pwID;
	int passwordLength;
	String appName;
	String encryptedPassword;

	public Password() {

	}

	public Password(int pwID, int userID, String appName, String encryptedPassword) {
		this.userID = userID;
		this.pwID = pwID;
		this.appName = appName;
		this.encryptedPassword = encryptedPassword;
	}

	public Password(int pwID, String appName, String encryptedPassword) {
		this.appName = appName;
		this.encryptedPassword = encryptedPassword;
	}
	
	public Password(String appName, String encryptedPassword, int pwLength) {
		this.appName = appName;
		this.encryptedPassword = encryptedPassword;
		this.passwordLength = pwLength;
	}

	public Password(int pwID, int userID, String appName, String encryptedPassword, int pwLength) {
		this.pwID = pwID;
		this.userID = userID;
		this.appName = appName;
		this.encryptedPassword = encryptedPassword;
		this.passwordLength = pwLength;
	}

	public void setUserID(int userID) {
		this.pwID = userID;
	}
	
	public void setPasswordLength(int passwordLength) {
		this.passwordLength = passwordLength;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}

	public int getUserID() {
		return pwID;
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
		String pwInfo = "temporaryStringInNeedOfReplacement";
		return pwInfo;
	}
}
