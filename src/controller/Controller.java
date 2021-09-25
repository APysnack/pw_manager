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
		return "password";
	}

	// modifies an existing password in the database (oldAppName is used to identify
	// it)
	public boolean editPassword(String oldAppName, String oldAppUsername, String newAppName, String newAppUsername,
			String password) {
		return true;
	}

	// TODO: this function is not written. it should call conn function to get
	// passwords from db. Need to develop an
	// algorithm that will create a list of PasswordSet objects that have an
	// applicationName (e.g. facebook) and an
	// ArrayList<Password> containing all the Password objects that are associated
	// with facebook
	
	public ArrayList<PasswordSet> getAllPasswords() {

		// creation of the list object to be returned
		ArrayList<PasswordSet> passwordSetList = new ArrayList<PasswordSet>();

		// assume this is the list of passwords received from the database
		Password password1 = new Password(1, 1, "facebook", "myusername", "nfpassword", 10);
		Password password2 = new Password(2, 1, "netflix", "nfusername", "fbpassword", 10);
		Password password3 = new Password(3, 1, "gmail", "gmusername1", "gmpassword", 10);
		Password password4 = new Password(4, 1, "gmail", "gmusername2", "gmpassword", 10);
		Password password5 = new Password(5, 1, "netflix", "nfusername2", "nfpassword", 10);
		Password password6 = new Password(6, 1, "gmail", "gmusername3", "gmpassword", 10);

		// an algorithm needs to be written to sort these into ArrayList<Password> type.
		// Example shown below:
		ArrayList<Password> gmailPasswordList = new ArrayList<Password>();
		gmailPasswordList.add(password3);
		gmailPasswordList.add(password4);
		gmailPasswordList.add(password6);

		// for each of these, they should be saved as a password set object
		PasswordSet gmailPasswordSet = new PasswordSet("gmail", gmailPasswordList);

		ArrayList<Password> netflixPasswordList = new ArrayList<Password>();
		netflixPasswordList.add(password2);
		netflixPasswordList.add(password5);

		PasswordSet netflixPasswordSet = new PasswordSet("netflix", netflixPasswordList);

		ArrayList<Password> facebookPasswordList = new ArrayList<Password>();
		facebookPasswordList.add(password1);

		PasswordSet facebookPasswordSet = new PasswordSet("facebook", facebookPasswordList);

		// afterwards, all of these password sets should be saved in an array of
		// password sets
		passwordSetList.add(gmailPasswordSet);
		passwordSetList.add(netflixPasswordSet);
		passwordSetList.add(facebookPasswordSet);

		return passwordSetList;
	}

}
