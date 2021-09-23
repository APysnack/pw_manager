package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.DbConnection;
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
	// e.g. [true, true, false] for a user who can add and edit users, but cannot delete them
	// 
	public User getUserInfo(String username) {
		List<Boolean> privilegeList = conn.getUserPrivileges(username);
		String userPassword = conn.getUserPW(username);
		int passwordLength = conn.getUserPWLength(username);
		User user = new User(username, userPassword, privilegeList);
		user.setPasswordLength(passwordLength);
		return user;
	}
	
	// any verification checks needed before making a call to conn.deleteUserFromDB(String deleteUser);
	public boolean deleteUser(String deleteUser) {
		return true;
	}

}
