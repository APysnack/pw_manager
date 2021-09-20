package controller;

import java.util.ArrayList;

import model.DbConnection;

public class Controller {
	
	DbConnection conn;
	
	public Controller(DbConnection conn){
		this.conn = conn;
	}
	
	public boolean authenticateUser(String userName, String password) {
		ArrayList<String> arrList = conn.get_all_from_db();
		System.out.println(arrList);
		return true;
	}
}
