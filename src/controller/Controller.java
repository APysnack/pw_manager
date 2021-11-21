package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.DbConnection;
import structures.Password;
import structures.PasswordSet;
import structures.User;
import controller.CUtils;

public class Controller {

	DbConnection conn;
	User currentUser;
	CUtils utils;

	// needed to make calls to the database
	public Controller(DbConnection conn) {
		this.conn = conn;
		this.utils = new CUtils();
	}

	// still needs input validation. passwords should not be greater than char[64],
	// username is a text type, but we should set limitations for number of
	// characters/no spaces should be allowed in usernames or passwords/etc. (no
	// passwords should be accepted that are all asterisks e.g. *****)
	public boolean addUser(String userName, String password, boolean addPermission, boolean editPermission,
			boolean deletePermission) {
		utils.encryptPassword(password);
		String encryptedPW = utils.getEncryptedPassword();
		String saltVal = utils.getSaltVal();
		int pwLength = password.length();
		password = "";
		if(encryptedPW == "invalid") {
			return false;
		}
		else {
			User user = new User(userName, encryptedPW, saltVal, pwLength, addPermission, editPermission, deletePermission);
			boolean insertSuccessful = conn.addUserToDb(user);

			if (insertSuccessful) {
				return true;
			} else {
				return false;
			}
			
		}
	}

	// needs input validation currently uses authenticateUserInDb to verify
	// that a user with userName + password is found. returns boolean true if user
	// count == 1. Then gets the user's information from the database and sets it as
	// the current user for both the controller and dbconnection
	public boolean authenticateUser(String userName, String password) {
			User user = conn.getUser(userName);
			String encPassword = user.getEncryptedPassword();
			String saltVal = user.getSaltVal();
			boolean userAuthenticated = utils.verifyPassword(password, encPassword, saltVal);
			if (userAuthenticated) {
				conn.setCurrentUser(user);
				this.currentUser = user;
				return true;
			} else {
				return false;
			}
	}

	// should generate a secure password (as randomly as possible)
	public String generateRandomPassword() {
		Random rand = new Random();
		int rand_int = rand.nextInt(1000);
		String str = String.valueOf(rand_int);
		return str;
	}

	// gets the user information from the database for the user with the name
	// {username}
	public User getUserInfo(String username) {
		User user = conn.getUser(username);
		return user;
	}

	// gets the password object associated with {applicationName} and {appUserName}
	// from the db
	public Password getPasswordInfo(String applicationName, String appUserName) {
		Password password = conn.getPasswordInfo(applicationName, appUserName);
		return password;
	}

	// validation needed
	public boolean deleteUser(String userName) {

		conn.deleteUserFromDb(userName);
		return true;
	}

	// may need validation/error checking
	public boolean deletePassword(String appName, String userName) {
		boolean pwDeleted = conn.deletePWFromDb(appName, userName);
		return pwDeleted;
	}

	// needs to do error checking then encrypt password and send it to the db
	public boolean addNewPassword(String appName, String appUserName, String appPassword) {
		int pwLen = appPassword.length();
		Password newPassword = new Password(appName, appUserName, appPassword, pwLen);
		conn.addPasswordToDb(newPassword);
		return true;
	}

	// retrieves encrypted password from the database. needs to performs decryption
	// and
	// return the unencrypted password as a string
	public String getDecryptedPassword(String appName, String username) {
		String returnString = "";
		returnString = conn.getEncryptedPasswordFor(appName, username);
		return returnString;
	}

	// modifies an existing password in the database. needs validation checking, and
	// particular do not allow spaces or any password that looks like *****
	public boolean editPassword(String oldAppName, String oldAppUserName, String newAppName, String newAppUserName,
			String password) {

		int pwLen = password.length();

		Password pw = new Password(newAppName, newAppUserName, password, pwLen);
		boolean pwUpdated = conn.editPassword(oldAppName, oldAppUserName, pw);

		return true;
	}

	// this function gets all the passwords from database and stores them in a
	// "password set" which groups all usernames by application name (e.g. if you
	// have multiple gmail accounts, they're all grouped together as a password set).
	public ArrayList<PasswordSet> getAllPasswords() {

		// creation of the list object to be returned
		ArrayList<PasswordSet> passwordSetList = new ArrayList<PasswordSet>();
		ArrayList<Password> passwordList = conn.getAllPasswords();
		ArrayList<String> applicationList = new ArrayList<String>();

		for (int i = 0; i < passwordList.size(); i++) {
			if (applicationList.contains(passwordList.get(i).getAppName())) {
				int index = applicationList.indexOf(passwordList.get(i).getAppName());
				passwordSetList.get(index).addPassword(passwordList.get(i));
			} else {
				applicationList.add(passwordList.get(i).getAppName());
				PasswordSet passwordSet = new PasswordSet(passwordList.get(i).getAppName());
				passwordSet.addPassword(passwordList.get(i));
				passwordSetList.add(passwordSet);
			}
		}

		return passwordSetList;
	}

	
	// needs encryption before storing the password in the database
	public boolean editUser(String oldUserName, String newUserName, String newPassword, boolean addPermission,
			boolean editPermission, boolean deletePermission) {
		int pwLen = newPassword.length();
		User newUserInfo = new User(newUserName, newPassword, pwLen, addPermission, editPermission, deletePermission);
		boolean userEdited = conn.editUser(oldUserName, newUserInfo);
		if (userEdited == true) {
			return true;
		} else {
			return false;
		}
	}

}
