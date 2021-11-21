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
	String input;

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
		if(currentUser != null) {
			if(currentUser.getAddPermission() == false && addPermission == true || currentUser.getEditPermission() == false && editPermission == true || currentUser.getDeletePermission() == false && deletePermission == true) {
				return false;
			}
		}
		
		// function still needs to be written. Should return false if input is all asterisks, 
		// return false if input is "Enter Password" or if input contains spaces of any kind
		// consider other SQL/Length edge cases. currently always returns true
		Boolean validPassword = utils.validatePasswordInput(password);
		if(validPassword) {
			String hashedPassword = utils.generateKey(password);
			String saltValue = utils.getSaltVal();
			int pwLength = password.length();
			password = "";
			User user = new User(userName, hashedPassword, saltValue, pwLength, addPermission, editPermission, deletePermission);
			boolean insertSuccessful = conn.addUserToDb(user);
			if (insertSuccessful) {
					return true;
			}
			
		}
		return false;
	}

	// needs input validation currently uses authenticateUserInDb to verify
	// that a user with userName + password is found. returns boolean true if user
	// count == 1. Then gets the user's information from the database and sets it as
	// the current user for both the controller and dbconnection
	public boolean authenticateUser(String userName, String password) {
			boolean userExists = conn.confirmUserExists(userName);
			// function still needs to be written. Should return false if input is all asterisks, 
			// return false if input is "Enter Password" or if input contains spaces of any kind
			// consider other SQL/Length edge cases. currently always returns true
			Boolean validPassword = utils.validatePasswordInput(password);
			if(userExists && validPassword) {
				User user = conn.getUser(userName);
				String encPassword = user.getEncryptedPassword();
				String saltVal = user.getSaltVal();
				boolean userAuthenticated = utils.verifyPassword(password, encPassword, saltVal);
				if (userAuthenticated) {
					conn.setCurrentUser(user);
					this.currentUser = user;
					this.input = utils.getHashedInput();
					return true;
				}
				
			}
			return false;
	}

	// Still needs to be written: should generate a random password of length = length and according to the boolean rules
	// for whether the user wishes to generate a password using a-z, A-Z, 0-9, and/or with symbols (@!#$ etc.)
	public String generateRandomPassword(boolean lowerAlpha, boolean upperAlpha, boolean numeric, boolean symbols, int length) {
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

	// needs input validation
	public boolean addNewPassword(String appName, String appUserName, String appPassword) {
		// function still needs to be written. Should return false if input is all asterisks, 
		// return false if input is "Enter Password" or if input contains spaces of any kind
		// consider other SQL/Length edge cases. currently always returns true
		Boolean validPassword = utils.validatePasswordInput(appPassword);
		if(validPassword) {
			String encryptedPassword = utils.encrypt(input, appPassword);
			String saltVal = utils.getSaltVal();
			int pwLen = appPassword.length();
			Password newPassword = new Password(appName, appUserName, encryptedPassword, saltVal, pwLen);
			conn.addPasswordToDb(newPassword);
			return true;
			
		}
		return false;
	}

	// retrieves encrypted password from the database and decrypts
	public String getDecryptedPassword(String appName, String userName) {
		Password password = conn.getPasswordInfo(appName, userName);
		String encryptedPassword = password.getEncryptedPassword();
		String passwordSalt = password.getSaltVal();
		String returnString = utils.decrypt(input, encryptedPassword, passwordSalt);
		return returnString;
	}

	// modifies an existing password in the database. needs validation checking, and
	// particular do not allow spaces or any password that looks like *****
	public boolean editPassword(String oldAppName, String oldAppUserName, String newAppName, String newAppUserName,
			String password) {
		// function still needs to be written. Should return false if input is all asterisks, 
		// return false if input is "Enter Password" or if input contains spaces of any kind
		// consider other SQL/Length edge cases. currently always returns true
		Boolean validPassword = utils.validatePasswordInput(password);
		if(validPassword) {
			int pwLen = password.length();
			Password pw = new Password(newAppName, newAppUserName, password, pwLen);
			boolean pwUpdated = conn.editPassword(oldAppName, oldAppUserName, pw);
			return true;
		}

		return false;
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
		if(currentUser.getAddPermission() == false && addPermission == true || currentUser.getEditPermission() == false && editPermission == true || currentUser.getDeletePermission() == false && deletePermission == true) {
			return false;
		}
		int pwLen = newPassword.length();
		User userToModify = conn.getUser(oldUserName);		
		if(userToModify.getUsername() != newUserName) {
			userToModify.setUsername(newUserName);
		}
		
		// function still needs to be written. Should return false if input is all asterisks, 
		// return false if input is "Enter Password" or if input contains spaces of any kind
		// currently always returns true
		Boolean validPassword = utils.validatePasswordInput(newPassword);
		
		// NOTE: needs to be thoroughly tested
		if(validPassword) {
			String encPassword = userToModify.getEncryptedPassword();
			String saltVal = userToModify.getSaltVal();
			boolean userAuthenticated = utils.verifyPassword(newPassword, encPassword, saltVal);
			
			if(!userAuthenticated) {
				String hashedPassword = utils.generateKey(newPassword);
				String saltValue = utils.getSaltVal();
				userToModify.setEncryptedPassword(hashedPassword);
				userToModify.setSaltVal(saltValue);
			}
		}
		
		if(userToModify.getAddPermission() != addPermission) {
			userToModify.setAddPermission(addPermission);
		}
		
		if(userToModify.getEditPermission() != editPermission) {
			userToModify.setEditPermission(editPermission);
		}
		
		if(userToModify.getDeletePermission() != deletePermission) {
			userToModify.setDeletePermission(deletePermission);
		}
		
		boolean userEdited = conn.editUser(oldUserName, userToModify);
		if (userEdited == true) {
			return true;
		} else {
			return false;
		}
	}

}
