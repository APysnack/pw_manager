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

	// adds a user into the database. ensures that a user cannot create another user
	// with permissions they themselves don't have
	public boolean addUser(String userName, String password, boolean addPermission, boolean editPermission,
			boolean deletePermission) {
		if (currentUser != null) {
			if (currentUser.getAddPermission() == false && addPermission == true
					|| currentUser.getEditPermission() == false && editPermission == true
					|| currentUser.getDeletePermission() == false && deletePermission == true) {
				return false;
			}
		}

		// validates input for username/password, actual function needs to be written
		boolean validUserName = utils.validateInput(userName, "userName");
		boolean validPassword = utils.validateInput(password, "password");
		if (validPassword && validUserName) {

			// stores hashed password and salt value in the database
			String hashedPassword = utils.generateKey(password);
			String saltValue = utils.getSaltVal();
			int pwLength = password.length();
			password = "";
			User user = new User(userName, hashedPassword, saltValue, pwLength, addPermission, editPermission,
					deletePermission);
			boolean insertSuccessful = conn.addUserToDb(user);
			if (insertSuccessful) {
				return true;
			}

		}
		return false;
	}

	// uses confirmUserExists to verify that a user with userName + password is
	// found.
	// returns boolean true if user count == 1. Then gets the user's information
	// from the database
	// and sets it as the current user for both the controller and dbconnection
	public boolean authenticateUser(String userName, String password) {
		boolean validUserName = utils.validateInput(userName, "userName");
		boolean validPassword = utils.validateInput(password, "password");
		if (validUserName && validPassword) {
			boolean userExists = conn.confirmUserExists(userName);
			if (userExists) {
				User user = conn.getUser(userName);
				// checks to determine if user is currently locked out from too many failed
				// login attempts
				boolean userIsLockedOut = conn.getUserLockoutStatus(user.getUserID());
				if (userIsLockedOut) {
					return false;
				} else {
					// hashes the input to verify that it matches the output hash stored for this
					// user in the db
					String encPassword = user.getEncryptedPassword();
					String saltVal = user.getSaltVal();
					boolean userAuthenticated = utils.verifyPassword(password, encPassword, saltVal);
					if (userAuthenticated) {
						conn.updateLoginAttempts(user.getUserID(), "reset");
						conn.setCurrentUser(user);
						this.currentUser = user;
						this.input = utils.getHashedInput();
						return true;
					} else {
						conn.updateLoginAttempts(user.getUserID(), "increment");
					}

				}

			}

		}
		return false;
	}

	// Generates a random string. User inputs boolean to specify if string should
	// contain (a-z, A-Z, 0-9, symbols, etc.)
	public String generateRandomPassword(boolean lowerAlpha, boolean upperAlpha, boolean numeric, boolean symbols,
			int length) {
		String randomPW = utils.getRandomString(lowerAlpha, upperAlpha, numeric, symbols, length);
		return randomPW;
	}

	// gets the user information from the database for the user with the name
	// {username}, input validation not needed since this value comes directly from
	// the database
	public User getUserInfo(String username) {
		User user = conn.getUser(username);
		return user;
	}

	// gets the password object associated with {applicationName} and {appUserName}
	// from the db. input validation not needed since this value comes directly from
	// the database
	public Password getPasswordInfo(String applicationName, String appUserName) {
		Password password = conn.getPasswordInfo(applicationName, appUserName);
		return password;
	}

	// deletes user with the name {userName}
	// input validation not needed since this value comes directly from the database
	public boolean deleteUser(String userName) {
		conn.deleteUserFromDb(userName);
		return true;
	}

	// deletes password with the app name {appName} and username {userName}
	// input validation not needed since this value comes directly from the database
	public boolean deletePassword(String appName, String userName) {
		boolean pwDeleted = conn.deletePWFromDb(appName, userName);
		return pwDeleted;
	}

	// adds a non-primary password to the database for the current user
	public boolean addNewPassword(String appName, String appUserName, String appPassword) {
		// input validation, function still needs to be written
		boolean validAppName = utils.validateInput(appName, "appName");
		boolean validAppUserName = utils.validateInput(appUserName, "appUserName");
		boolean validPassword = utils.validateInput(appPassword, "password");
		if (validPassword && validAppName && validAppUserName) {
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
	// input validation not needed since these values come from the db
	public String getDecryptedPassword(String appName, String userName) {
		Password password = conn.getPasswordInfo(appName, userName);
		String encryptedPassword = password.getEncryptedPassword();
		String passwordSalt = password.getSaltVal();
		String returnString = utils.decrypt(input, encryptedPassword, passwordSalt);
		return returnString;
	}

	// modifies a non-primary password that currently exists in the database
	public boolean editPassword(String oldAppName, String oldAppUserName, String newAppName, String newAppUserName,
			String password) {
		boolean validAppName = utils.validateInput(newAppName, "appName");
		boolean validAppUserName = utils.validateInput(newAppUserName, "appUserName");
		boolean validPassword = utils.validateInput(password, "password");
		if (validPassword && validAppName && validAppUserName) {
			boolean modified = utils.isModified(password, "password");
			if (modified) {
				int pwLen = password.length();
				String encryptedPassword = utils.encrypt(input, password);
				String saltVal = utils.getSaltVal();
				Password newPassword = new Password(newAppName, newAppUserName, encryptedPassword, saltVal, pwLen);
				boolean pwUpdated = conn.editPassword(oldAppName, oldAppUserName, newPassword);
				if (pwUpdated) {
					System.out.println("test");
					return true;
				}
			}
			else {
				Password oldPassword = conn.getPasswordInfo(oldAppName, oldAppUserName);
				Password newPassword = new Password(newAppName, newAppUserName, oldPassword.getEncryptedPassword(), oldPassword.getSaltVal(), oldPassword.getPasswordLength());
				boolean pwUpdated = conn.editPassword(oldAppName, oldAppUserName, newPassword);
				if (pwUpdated) {
					return true;
				}
			}
		} 
		return false;
	}

	// this function gets all the passwords from database and stores them in a
	// "password set" which groups all usernames by application name (e.g. if you
	// have multiple gmail accounts, they're all grouped together as a password
	// set).
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

	// edits user already in the database with new name/password/permission
	// information. NOTE: needs to be thoroughly tested
	public boolean editUser(String oldUserName, String newUserName, String newPassword, boolean addPermission,
			boolean editPermission, boolean deletePermission) {
		if (currentUser.getAddPermission() == false && addPermission == true
				|| currentUser.getEditPermission() == false && editPermission == true
				|| currentUser.getDeletePermission() == false && deletePermission == true) {
			return false;
		}
		int pwLen = newPassword.length();

		boolean validUserName = utils.validateInput(newUserName, "userName");
		boolean validPassword = utils.validateInput(newPassword, "password");

		if (validUserName && validPassword) {
			User userToModify = conn.getUser(oldUserName);

			if (userToModify.getUsername() != newUserName) {
				userToModify.setUsername(newUserName);
			}

			boolean modified = utils.isModified(newPassword, "password");
			if (modified) {
				String encPassword = userToModify.getEncryptedPassword();
				String saltVal = userToModify.getSaltVal();
				boolean userAuthenticated = utils.verifyPassword(newPassword, encPassword, saltVal);

				if (!userAuthenticated) {
					String hashedPassword = utils.generateKey(newPassword);
					String saltValue = utils.getSaltVal();
					userToModify.setEncryptedPassword(hashedPassword);
					userToModify.setSaltVal(saltValue);
					
					if (userToModify.getPasswordLength() != pwLen) {
						userToModify.setPasswordLength(pwLen);
					}
				}
				

			}

			if (userToModify.getAddPermission() != addPermission) {
				userToModify.setAddPermission(addPermission);
			}

			if (userToModify.getEditPermission() != editPermission) {
				userToModify.setEditPermission(editPermission);
			}

			if (userToModify.getDeletePermission() != deletePermission) {
				userToModify.setDeletePermission(deletePermission);
			}


			boolean userEdited = conn.editUser(oldUserName, userToModify);
			if (userEdited == true) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

}
