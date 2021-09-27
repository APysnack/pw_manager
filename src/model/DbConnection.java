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

	// returns the user object for the user that is currently logged in
	public User getCurrentUser() {
		return this.currentUser;
	}

	// returns a list of all usernames in the users table
	public ArrayList<String> getAllUserNames() {
		openConnection();
		new_query = "SELECT * FROM USERS";

		PreparedStatement pStmt;
		ArrayList<String> allUsers = new ArrayList<String>();

		try {
			pStmt = conn.prepareStatement(new_query);
			result = pStmt.executeQuery();

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

	/**********************************
	 * THINGS THAT NEED TO BE REWRITTEN OR MODIFIED
	 *********************************/

	// tries to add user to the database. returns true if successful. Uses prepared
	// statement to prevent SQL injection
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

			// if rowsAffected == 1, insert was successful
			int rowsAffected = pStmt.executeUpdate();
			if (rowsAffected == 1) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		}

		finally {
			closeConnection();
		}

	}

	// gets the number of rows in the designated table
	public int getRowCountFromTable(String tableName) {
		openConnection();
		new_query = "SELECT COUNT(*) FROM " + tableName;

		PreparedStatement pStmt;

		try {
			pStmt = conn.prepareStatement(new_query);
			result = pStmt.executeQuery();
			int numRows = result.getInt(1);
			return numRows;
		} catch (SQLException e) {
			System.out.println(e);
			return -1;
		} finally {
			closeConnection();
		}
	}

	// sets the current user for the database object as the designated user
	public void setCurrentUser(User user) {
		this.currentUser = user;
	}

	// returns the user object for the user with the name username
	public User getUser(String userName) {
		openConnection();

		new_query = "SELECT * FROM USERS WHERE USERNAME=?";
		User user = new User();
		PreparedStatement pStmt;

		try {
			pStmt = conn.prepareStatement(new_query);
			pStmt.setString(1, userName);
			ResultSet result = pStmt.executeQuery();

			while (result.next()) {
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
	// are correct. if rows returned == 1, authentication was successful
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

			if (numRows == 1) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		} finally {
			closeConnection();
		}
	}

	// inserts the data from a password object into the database
	public boolean addPasswordToDb(Password password) {
		openConnection();
		new_query = "INSERT INTO PASSWORDS (UID, APPLICATION, APPUSERNAME, PASSWORD, PASSWORDLENGTH) VALUES (?, ?,?,?,?)";
		PreparedStatement pStmt;

		try {
			pStmt = conn.prepareStatement(new_query);
			pStmt.setInt(1, currentUser.getUserID());
			pStmt.setString(2, password.getAppName());
			pStmt.setString(3, password.getAppUserName());
			pStmt.setString(4, password.getEncryptedPassword());
			pStmt.setInt(5, password.getPasswordLength());

			// if rowsAffected == 1, insert was successful
			int rowsAffected = pStmt.executeUpdate();
			if (rowsAffected == 1) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		}

		finally {
			closeConnection();
		}
	}
	
	public boolean editPassword(String oldAppName, String oldAppUserName, Password newPW) {
		openConnection();
		new_query = "update passwords set application=?, appUserName=?, password=?, passwordLength=? where application=? and appUserName=?";
		PreparedStatement pStmt;
		
		try {
			pStmt = conn.prepareStatement(new_query);
			pStmt.setString(1, newPW.getAppName());
			pStmt.setString(2, newPW.getAppUserName());
			pStmt.setString(3, newPW.getEncryptedPassword());
			pStmt.setInt(4, newPW.getPasswordLength());
			pStmt.setString(5, oldAppName);
			pStmt.setString(6, oldAppUserName);
			
			int rowsAffected = pStmt.executeUpdate();
			if (rowsAffected == 1) {
				return true;
			} else {
				return false;
			}
			
		}
		catch(SQLException e) {
			System.out.println(e);
		}
		
		
		return true;
	}
	
	public Password getPasswordInfo(String appName, String appUserName) {
		Password pw = new Password();
		openConnection();
		new_query = "SELECT * FROM PASSWORDS WHERE APPLICATION=? AND APPUSERNAME=?";
		PreparedStatement pStmt;
		try {
			pStmt = conn.prepareStatement(new_query);
			pStmt.setString(1, appName);
			pStmt.setString(2, appUserName);
			
			ResultSet result = pStmt.executeQuery();
			while (result.next()) {
				pw.setUserID(result.getInt(1));
				pw.setAppName(result.getString(3));
				pw.setAppUserName(result.getString(4));
				pw.setEncryptedPassword(result.getString(5));
				pw.setPasswordLength(result.getInt(6));
			}
		}
		catch(SQLException e) {
			System.out.println(e);
		}
		finally {
			closeConnection();
		}
		
		return pw;
	}
	
	public String getEncryptedPasswordFor(String applicationName, String appUserName) {
		openConnection();
		new_query = "SELECT * FROM PASSWORDS WHERE APPLICATION=? AND APPUSERNAME=?";
		PreparedStatement pStmt;
		String returnString = "";
		
		try {
			pStmt = conn.prepareStatement(new_query);
			pStmt.setString(1, applicationName);
			pStmt.setString(2, appUserName);
			ResultSet result = pStmt.executeQuery();
			while(result.next()) {
				returnString = result.getString(5);
			}
			return returnString;
		}
		catch(SQLException e){
			System.out.println(e);
			return returnString;

		}
		finally {
			closeConnection();
		}
	}

	public ArrayList<Password> getAllPasswords() {
		openConnection();
		ArrayList<Password> passwordList = new ArrayList<Password>();
		openConnection();
		new_query = "SELECT * FROM PASSWORDS WHERE UID=?";
		PreparedStatement pStmt;

		int uid = currentUser.getUserID();

		if (currentUser.getUserID() > 0) {
			try {
				pStmt = conn.prepareStatement(new_query);
				pStmt.setInt(1, uid);
				ResultSet result = pStmt.executeQuery();
				
				while (result.next()) {
					Password tempPass = new Password();
					tempPass.setPasswordID(result.getInt(1));
					tempPass.setUserID(result.getInt(2));
					tempPass.setAppName(result.getString(3));
					tempPass.setAppUserName(result.getString(4));
					tempPass.setEncryptedPassword(result.getString(5));
					tempPass.setPasswordLength(result.getInt(6));
					passwordList.add(tempPass);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		closeConnection();
		return passwordList;
	}
	
	public boolean deletePWFromDb(String appName, String userName) {
		openConnection();
		new_query = "DELETE FROM PASSWORDS WHERE APPLICATION=? AND APPUSERNAME=?";
		PreparedStatement pStmt;
		
		try {
			pStmt = conn.prepareStatement(new_query);
			pStmt.setString(1, appName);
			pStmt.setString(2, userName);
			int rowsAffected = pStmt.executeUpdate();
			if(rowsAffected == 1) {
				return true;
			}
			else {
				return false;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		finally {
			closeConnection();
		}
	}
}