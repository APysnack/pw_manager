package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.DbConnection;
import structures.Password;
import structures.PasswordSet;
import structures.User;

public class Controller {

	DbConnection conn;
	User currentUser;

	// needed to make calls to the database
	public Controller(DbConnection conn) {
		this.conn = conn;
	}

	// still needs input validation. passwords should not be greater than char[64],
	// username is a text type, but we should set limitations for number of
	// characters/spaces/etc.
	public boolean addUser(String userName, String password, boolean addPermission, boolean editPermission,
			boolean deletePermission) {

		int pwLength = password.length();
		User user = new User(userName, password, addPermission, editPermission, deletePermission, pwLength);

		boolean insertSuccessful = conn.addUserToDb(user);

		if (insertSuccessful) {
			return true;
		} else {
			return false;
		}
	}

	// needs encryption/decryption. currently uses authenticateUserInDb to verify
	// that a user with userName + password is found. returns boolean true if user
	// count == 1. Then gets the user's information from the database and sets it as
	// the current user for both the controller and dbconnection
	public boolean authenticateUser(String userName, String password) {
		boolean userAuthenticated = conn.authenticateUserInDb(userName, password);
		if (userAuthenticated) {
			User user = conn.getUser(userName);
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

	public User getUserInfo(String username) {
		User user = conn.getUser(username);
		return user;
	}

	// needs to get the password associated with applicationName, username from the
	// db and creates a password object (password id, user id, appName,
	// encryptedPassword,
	// passwordLengthBeforeDecryption)
	public Password getPasswordInfo(String applicationName, String appUserName) {
		Password password = new Password(2, 1, "facebook", "myusername", "facebookPW", 10);
		return password;
	}

	// any verification checks needed before making a call to
	// conn.deleteUserFromDB(String userName);
	public boolean deleteUser(String userName) {
		return true;
	}

	// any verification checks needed before making a call to
	// conn.deleteUserFromDB(String appName);
	public boolean deletePassword(String appName) {
		return true;
	}

	// needs to do error checking then encrypt password and send it to the db
	public boolean addNewPassword(String appName, String appUserName, String appPassword) {
		int pwLen = appPassword.length();
		Password newPassword = new Password(appName, appUserName, appPassword, pwLen);
		conn.addPasswordToDb(newPassword);
		return true;
	}

	// retrieves encrypted password from the database, performs decrypted and
	// returns the unencrypted password as a string
	public String getDecryptedPassword(String appName, String username) {
		String returnString = "";
		returnString = conn.getPasswordFor(appName, username);
		return returnString;
	}

	// modifies an existing password in the database (oldAppName is used to identify
	// it)
	public boolean editPassword(String oldAppName, String oldAppUsername, String newAppName, String newAppUsername,
			String password) {
		return true;
	}

	// this function get all the passwords from database and stores them in a
	// "password set" which groups all usernames by application name (e.g. if you
	// have multiple gmail accounts, they're all stored on the same object).
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

}
