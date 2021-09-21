package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DbConnection {
	String dbpath;
	String new_query;
	ResultSet result;
	Connection conn;

	// tries to connect to the database, prints out an error if unsuccessful
	public DbConnection() {
		try {
			this.dbpath = "jdbc:sqlite:pwdb.db";
			Connection conn = DriverManager.getConnection(dbpath);
			this.conn = conn;
		} catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	
	// tries to add user to the database. returns true if successful. Uses prepared statement to prevent SQL injection
	// still needs to handle the user's permissions to add/edit/remove from db
	public boolean addUserToDb(String userName, String password, boolean addPermission, boolean editPermission,
			boolean deletePermission) {
		new_query = "INSERT INTO USERS (USERNAME, PASSWORD) VALUES (?,?)";

		try {
			PreparedStatement preparedStatement = conn.prepareStatement(new_query);
			preparedStatement.setString(1, userName);
			preparedStatement.setString(2, password);
			
			// if rowsAffected > 0, insert was successful
			int rowsAffected = preparedStatement.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		}
	}
	
	// note: prepared statements aren't usually necessary if we're not adding anything to the database
	public int getRowCountFromTable(String tableName) {
		new_query = "SELECT COUNT(*) FROM " + tableName;
		
		Statement stmt;
		
		try {
			stmt = conn.createStatement();
			result = stmt.executeQuery(new_query);
			int numRows = result.getInt(1);
			return numRows;
		} catch (SQLException e) {
			System.out.println(e);
			return -1;
		}
	}

	// old code, probably don't need it 
//	public ArrayList<String> get_all_from_db() {
//		new_query = "select * from passwords";
//		ArrayList<String> arrList = new ArrayList<String>();
//
//		Statement stmt;
//
//		try {
//			stmt = conn.createStatement();
//			result = stmt.executeQuery(new_query);
//
//			while (result.next()) {
//				arrList.add(result.getString(2));
//			}
//
//			return arrList;
//
//		} catch (SQLException e) {
//			System.out.println(e);
//			return arrList;
//		}
//
//	}
}