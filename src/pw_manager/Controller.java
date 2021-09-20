package pw_manager;

import java.util.ArrayList;

public class Controller {
	
	DbConnection conn;
	
	Controller(DbConnection conn){
		this.conn = conn;
	}
	
	public boolean authenticateUser(String userName, String password) {
		ArrayList<String> arrList = conn.get_all_from_db();
		System.out.println(arrList);
		return true;
	}
}
