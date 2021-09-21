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
	public String userName;

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

	// tries to add user to the database. returns true if successful. Uses prepared
	// statement to prevent SQL injection
	// still needs to handle the user's permissions to add/edit/remove from db
	public boolean addUserToDb(String userName, String password, boolean addPermission, boolean editPermission,
			boolean deletePermission) {
		new_query = "INSERT INTO USERS (USERNAME, PASSWORD) VALUES (?,?)";

		try {
			PreparedStatement pStmt = conn.prepareStatement(new_query);
			pStmt.setString(1, userName);
			pStmt.setString(2, password);

			// if rowsAffected > 0, insert was successful
			int rowsAffected = pStmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		}
	}

	// note: prepared statements aren't usually necessary if we're not adding
	// anything to the database
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

	public boolean authenticateUserInDb(String userName, String password) {
		new_query = "SELECT COUNT(*) FROM USERS WHERE USERNAME=? AND PASSWORD=?";

		try {
			PreparedStatement pStmt = conn.prepareStatement(new_query);
			pStmt.setString(1,  userName);
			pStmt.setString(2, password);
			ResultSet result = pStmt.executeQuery();
			int userCount = result.getInt(1);
			
			if(userCount > 0) {
				this.userName = userName;
				return true;
			}
			else {
				return false;
			}
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		}
	}
	
	
	public String getUserName() {
		return userName;
	}
}