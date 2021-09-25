package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import structures.Password;
import structures.User;

public class DbConnection {

	String dbpath;
	String new_query;
	ResultSet result;
	Connection conn;
	public User currentUser;

	/*******************************
	 * THINGS YOU CAN MODIFY IF YOU WANT BUT MAY NOT NEED TO
	 ******************************/

	// tries to connect to the database, prints out an error if unsuccessful
	public DbConnection() {
		this.dbpath = "jdbc:sqlite:pwdb.db";
	}
	
	public User getCurrentUser() {
		return this.currentUser;
	}

	// gets a list of all usernames in the username table. since there are no
	// arguments passed in, this shouldn't need
	// protection against SQL injection
	public ArrayList<String> getAllUserNames() {
		openConnection();
		new_query = "SELECT * FROM USERS";

		Statement stmt;
		ArrayList<String> allUsers = new ArrayList<String>();

		try {
			stmt = conn.createStatement();
			result = stmt.executeQuery(new_query);

			while (result.next()) {
				allUsers.add(result.getString(2));
			}
			return allUsers;
		} catch (SQLException e) {
			System.out.println(e);
			return allUsers;
		} finally {
			closeConnection();
		}
	}
	
	public ArrayList<String> getAllPWData(){
		openConnection();
		ArrayList<String> temp = new ArrayList<String>();
		new_query = "SELECT * FROM PASSWORDS";
		closeConnection();
		return temp;
	}

	// gets a list of all usernames in the username table. since there are no
	// needs protection against sql injection and needs to only display apps for the
	// logged in user
	public ArrayList<String> getAllUserApps() {
		openConnection();
		new_query = "SELECT * FROM Passwords";

		Statement stmt;
		ArrayList<String> allUsers = new ArrayList<String>();

		try {
			stmt = conn.createStatement();
			result = stmt.executeQuery(new_query);

			while (result.next()) {
				allUsers.add(result.getString(3));
			}
			return allUsers;
		} catch (SQLException e) {
			System.out.println(e);
			return allUsers;
		} finally {
			closeConnection();
		}
	}

	public void openConnection() {
		try {
			Connection conn = DriverManager.getConnection(dbpath);
			this.conn = conn;
		} catch (SQLException e) {
			System.out.println(e);
		}
	}

	public void closeConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**********************************
	 * THINGS THAT NEED TO BE REWRITTEN OR MODIFIED
	 *********************************/

	// tries to add user to the database. returns true if successful. Uses prepared
	// statement to prevent SQL injection
	// still needs to handle the user's permissions to add/edit/remove from db
	public boolean addUserToDb(User user) {
		openConnection();
		new_query = "INSERT INTO USERS (USERNAME, PASSWORD, PASSWORDLENGTH, CANADDUSER, CANEDITUSER, CANDELETEUSER) VALUES (?,?,?,?,?,?)";
		PreparedStatement pStmt;
		try {
			pStmt = conn.prepareStatement(new_query);
			pStmt.setString(1, user.getUsername());
			pStmt.setString(2, user.getEncryptedPassword());
			pStmt.setInt(3, user.getPasswordLength());
			pStmt.setBoolean(4, user.getAddPermission());
			pStmt.setBoolean(5, user.getEditPermission());
			pStmt.setBoolean(6, user.getDeletePermission());
			
			// if rowsAffected > 0, insert was successful
			int rowsAffected = pStmt.executeUpdate();

	
			
			return true;
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		}

		finally {
			closeConnection();
		}
		
	}

	// note: this should probably be changed into a prepared statement since an
	// argument is being passed in
	public int getRowCountFromTable(String tableName) {
		openConnection();
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
		} finally {
			closeConnection();
		}
	}
	
	public void setCurrentUser(User user) {
		this.currentUser = user;
	}
	
	public User getUser(String userName) {
		openConnection();
		
		new_query = "SELECT * FROM USERS WHERE USERNAME=?";
		User user = new User();
		PreparedStatement pStmt;
		
		try {
			pStmt = conn.prepareStatement(new_query);
			pStmt.setString(1, userName);
			ResultSet result = pStmt.executeQuery();
			
			while(result.next()) {
				user.setUserID(result.getInt(1));
				user.setUsername(result.getString(2));
				user.setEncryptedPassword(result.getString(3));
				user.setPasswordLength(result.getInt(4));
				user.setAddPermission(result.getBoolean(5));
				user.setEditPermission(result.getBoolean(6));
				user.setDeletePermission(result.getBoolean(7));
			}
			
			return user;
		} catch (SQLException e) {
			System.out.println(e);
			return user;
		} finally {
			closeConnection();
		}
	}

	// when user tries to log in, checks to confirm that the username and password
	// are correct
	public boolean authenticateUserInDb(String userName, String password) {
		openConnection();
		new_query = "SELECT COUNT(*) FROM USERS WHERE USERNAME=? AND PASSWORD=?";
		PreparedStatement pStmt;
		
		try {
			pStmt = conn.prepareStatement(new_query);
			pStmt.setString(1, userName);
			pStmt.setString(2, password);
			ResultSet result = pStmt.executeQuery();
			int numRows = result.getInt(1);
			
			if(numRows == 1) {
				return true;
			}
			else {
				return false;
			}
		}
		catch(SQLException e) {
			System.out.println(e);
			return false;
		}
		finally{
			closeConnection();
		}
	}
	
	public boolean addPasswordToDb(Password password) {
		openConnection();
		new_query = "INSERT INTO PASSWORDS (UID, APPLICATION, USERNAME, PASSWORD, PASSWORDLENGTH) VALUES (?, ?,?,?,?)";
		PreparedStatement pStmt;
		
		try {
			pStmt = conn.prepareStatement(new_query);
			pStmt.setInt(1, currentUser.getUserID());
			pStmt.setString(2, password.getAppName());
			pStmt.setString(3, password.getAppUserName());
			pStmt.setString(4, password.getEncryptedPassword());
			pStmt.setInt(5, password.getPasswordLength());

			// if rowsAffected > 0, insert was successful
			int rowsAffected = pStmt.executeUpdate();

			return true;
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		}

		finally {
			closeConnection();
		}
	}
}