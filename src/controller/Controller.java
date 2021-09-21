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
}
