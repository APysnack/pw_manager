package controller;

import model.DbConnection;

public class Controller {
	
	DbConnection conn;
	
	public Controller(DbConnection conn){
		this.conn = conn;
	}
	
	// needs input validation
	public boolean addUser(String userName, String password, boolean addPermission, boolean editPermission, boolean deletePermission) {
		
		boolean insertSuccessful = conn.addUserToDb(userName, password, addPermission, editPermission, deletePermission);
		
		if(insertSuccessful) {
			return true;
		}
		else {
			return false;
		}
	}
	
	// needs encryption/decryption
	public boolean authenticateUser(String userName, String password) {
		
		boolean userAuthenticated = conn.authenticateUserInDb(userName, password);
		if(userAuthenticated == true) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public String getUserName() {
		String userName = conn.getUserName();
		return userName;
	}
}
