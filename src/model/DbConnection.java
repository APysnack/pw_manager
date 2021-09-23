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

import pw_manager.User;

public class DbConnection {

	String dbpath;
	String new_query;
	ResultSet result;
	Connection conn;
	public String userName;

	/*******************************
	 * THINGS YOU CAN MODIFY IF YOU WANT BUT MAY NOT NEED TO
	 ******************************/

	// tries to connect to the database, prints out an error if unsuccessful
	public DbConnection() {
		openConnection();
	}

	// gets the username of the currently logged in user
	public String getCurrentUserName() {
		return userName;
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

	public void openConnection() {
		try {
			this.dbpath = "jdbc:sqlite:pwdb.db";
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

		new_query = "INSERT INTO USERS (USERNAME, PASSWORD, PASSWORDLENGTH) VALUES (?,?,?)";

		try {
			PreparedStatement pStmt = conn.prepareStatement(new_query);
			pStmt.setString(1, user.getUsername());
			pStmt.setString(2, user.getEncryptedPassword());
			pStmt.setInt(3, user.getPasswordLength());

			// if rowsAffected > 0, insert was successful
			int rowsAffected = pStmt.executeUpdate();
			conn.close();

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

	// when user tries to log in, checks to confirm that the username and password
	// are correct
	public boolean authenticateUserInDb(String userName, String password) {
		openConnection();
		new_query = "SELECT COUNT(*) FROM USERS WHERE USERNAME=? AND PASSWORD=?";

		try {
			PreparedStatement pStmt = conn.prepareStatement(new_query);
			pStmt.setString(1, userName);
			pStmt.setString(2, password);
			ResultSet result = pStmt.executeQuery();
			int userCount = result.getInt(1);

			if (userCount > 0) {
				this.userName = userName;
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

	// Still needs to be written. Function should check the "privileges" table for
	// the privileges associated with the user that has the username in the string
	// argument. (Note: the privilege table has the foreign key UID)
	// should return a boolean list for the user's permission to add, edit, and
	// delete users
	// e.g. if the user can add/edit but not delete, the list should return [true,
	// true, false]
	public List<Boolean> getUserPrivileges(String username) {
		List<Boolean> privilegeList = new ArrayList<Boolean>(Arrays.asList(new Boolean[3]));
		Collections.fill(privilegeList, Boolean.TRUE);
		return privilegeList;
	}

	// Still needs to be written. Function should query the database for username's
	// password then return the password
	public String getUserPW(String username) {
		List<String> nameAndPassword = new ArrayList<String>();
		String tempPassword = "dummyPassword";
		return tempPassword;
	}

	// Still need to be written. currently there is no column in the database for
	// password length
	public int getUserPWLength(String username) {
		int pwLength = 10;
		return pwLength;
	}
}