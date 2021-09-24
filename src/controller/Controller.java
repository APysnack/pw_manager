package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.DbConnection;
import pw_manager.Password;
import pw_manager.User;

public class Controller {

	DbConnection conn;

	public Controller(DbConnection conn) {
		this.conn = conn;
	}

	// needs input validation. passwords should not be greater than char[64],
	// username is a text type, but we should set limitations for number of
	// characters/spaces/etc.
	public boolean addUser(String userName, String password, boolean addPermission, boolean editPermission,
			boolean deletePermission) {

		int pwLength = password.length();
		User user = new User(userName, password, addPermission, editPermission, deletePermission, pwLength);

		System.out.println(user);

		boolean insertSuccessful = conn.addUserToDb(user);

		if (insertSuccessful) {
			return true;
		} else {
			return false;
		}
	}

	// needs encryption/decryption
	public boolean authenticateUser(String userName, String password) {

		boolean userAuthenticated = conn.authenticateUserInDb(userName, password);

		if (userAuthenticated == true) {
			return true;
		} else {
			return false;
		}
	}

	// should generate a secure password (and as randomly as possible)
	public String generateRandomPassword() {
		Random rand = new Random();
		int rand_int = rand.nextInt(1000);
		String str = String.valueOf(rand_int);
		return str;
	}

	// getUserPrivileges returns a boolean list for [Add, Edit, Delete] permission
	// e.g. [true, true, false] for a user who can add and edit users, but cannot
	// delete them
	public User getUserInfo(String username) {
		List<Boolean> privilegeList = conn.getUserPrivileges(username);
		String userPassword = conn.getUserPW(username);
		int passwordLength = conn.getUserPWLength(username);
		User user = new User(username, userPassword, privilegeList);
		user.setPasswordLength(passwordLength);
		return user;
	}

	// creates a password object (password id, user id, appName, encryptedPassword, passwordLengthBeforeDecryption)
	public Password getPasswordInfo(String applicationName) {
		Password password = new Password(2, 1, "facebook", "facebookPW", 10);
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
	public boolean addNewPassword(String appName, String appPassword) {
		return true;
	}
	
	// retrieves encrypted password from the database, performs decrypted and returns the unencrypted password as a string
	public String getDecryptedPassword(String appName) {
		return "password";
	}
	
	
	// modifies an existing password in the database (oldAppName is used to identify it)
	public boolean editPassword(String oldAppName, String newAppName, String password) {
		return true;
	}

}
