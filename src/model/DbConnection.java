package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;

import org.sqlite.SQLiteConfig;

import structures.Password;
import structures.User;

public class DbConnection {

	String dbpath;
	String new_query;
	ResultSet result;
	Connection conn;
	public User currentUser;
	String dbName = "DB42328112177C2D6F2F6CA7F33C8E81084B8FF3E14202254137E22673BCE2C8";
	String dbPass = "5E884898DA28047151D0E56F8DC6292773603D0D6AABBDD62A11EF721D1542D8";

	// tries to connect to the database, prints out an error if unsuccessful
	public DbConnection() {
		this.dbpath = "jdbc:sqlite:pwdb.db";
		this.conn = null;
	}

	// opens a connection to the database before a query, foreign keys must be
	// enforced for cascading delete to work
	public void openConnection() {
		try {
			SQLiteConfig config = new SQLiteConfig();
			config.enforceForeignKeys(true);
			conn = DriverManager.getConnection(dbpath, dbName, dbPass);
		} catch (SQLException e) {
			System.out.println(e);
		}
	}

	// closes sql connection after a query
	public void closeConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void initDatabase() {
		openConnection();
		PreparedStatement pStmt;
		new_query = "SELECT NAME FROM SQLITE_MASTER WHERE TYPE='TABLE'";
		try {
			Statement stmt = conn.createStatement();
			String sqlCommand = "CREATE TABLE IF NOT EXISTS USERS(" + "ID INTEGER PRIMARY KEY,"
					+ "USERNAME TEXT NOT NULL UNIQUE," + "PASSWORD CHAR(64) NOT NULL," + "SALTVAL CHAR(32) NOT NULL,"
					+ "PASSWORDLENGTH INT NOT NULL," + "CANADDUSER BOOLEAN NOT NULL," + "CANEDITUSER BOOLEAN NOT NULL,"
					+ "CANDELETEUSER BOOLEAN NOT NULL," + "MOBILENUMBER CHAR(64) NOT NULL);";

			stmt.executeUpdate(sqlCommand);

			sqlCommand = "CREATE TABLE IF NOT EXISTS PASSWORDS(" + "PID INTEGER PRIMARY KEY," + "UID INTEGER,"
					+ "APPLICATION TEXT NOT NULL," + "APPUSERNAME TEXT NOT NULL," + "PASSWORD CHAR(64) NOT NULL,"
					+ "SALTVAL CHAR(32) NOT NULL," + "PASSWORDLENGTH INT NOT NULL,"
					+ "FOREIGN KEY (UID) REFERENCES USERS(ID) ON DELETE CASCADE);";

			stmt.executeUpdate(sqlCommand);

			sqlCommand = "CREATE TABLE IF NOT EXISTS LOGINS(" + "ID INTEGER PRIMARY KEY," + "UID INTEGER,"
					+ "FAILEDATTEMPTS INTEGER NOT NULL," + "LASTLOGIN TEXT NOT NULL,"
					+ "FOREIGN KEY (UID) REFERENCES USERS(ID) ON DELETE CASCADE);";

			stmt.executeUpdate(sqlCommand);

		} catch (SQLException e) {
			System.out.println(e);
		} finally {
			closeConnection();
		}
	}

	// all the user information is stored a user object, this returns that
	// information to the caller
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

	// tries to add user to the database. returns true if successful. Uses prepared
	// statement to prevent SQL injection
	public boolean addUserToDb(User user) {
		openConnection();
		new_query = "INSERT INTO USERS (USERNAME, PASSWORD, SALTVAL, PASSWORDLENGTH, CANADDUSER, CANEDITUSER, CANDELETEUSER, MOBILENUMBER) VALUES (?,?,?,?,?,?,?,?);";
		PreparedStatement pStmt;

		try {
			pStmt = conn.prepareStatement(new_query, Statement.RETURN_GENERATED_KEYS);
			pStmt.setString(1, user.getUsername());
			pStmt.setString(2, user.getEncryptedPassword());
			pStmt.setString(3, user.getSaltVal());
			pStmt.setInt(4, user.getPasswordLength());
			pStmt.setBoolean(5, user.getAddPermission());
			pStmt.setBoolean(6, user.getEditPermission());
			pStmt.setBoolean(7, user.getDeletePermission());
			pStmt.setString(8, user.getEncryptedNumber());

			// if rowsAffected == 1, insert was successful
			int rowsAffected = pStmt.executeUpdate();
			if (rowsAffected == 1) {
				try (ResultSet generatedKeys = pStmt.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						int userID = (int) generatedKeys.getLong(1);
						new_query = "INSERT INTO LOGINS (UID, FAILEDATTEMPTS, LASTLOGIN) VALUES (?,?,?);";
						try {
							pStmt = conn.prepareStatement(new_query);
							pStmt.setInt(1, userID);
							pStmt.setInt(2, 0);
							pStmt.setString(3, "2010-01-01");
							rowsAffected = pStmt.executeUpdate();
							if (rowsAffected == 1) {
								return true;
							}
						} catch (SQLException e) {
							System.out.println(e);
							return false;
						}
					}
				}
			}
			return false;
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

	public int getAdminRowCount() {
		openConnection();
		new_query = "SELECT COUNT(*) FROM USERS WHERE CANADDUSER=1 AND CANEDITUSER=1 AND CANDELETEUSER=1";

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

	public boolean checkPasswordCollision(String appName, String appUserName) {
		openConnection();
		new_query = "SELECT COUNT(*) FROM PASSWORDS WHERE APPLICATION=? AND APPUSERNAME=? AND UID=?";
		PreparedStatement pStmt;

		try {
			pStmt = conn.prepareStatement(new_query);
			pStmt.setString(1, appName);
			pStmt.setString(2, appUserName);
			pStmt.setInt(3, currentUser.getUserID());
			result = pStmt.executeQuery();
			int numRows = result.getInt(1);
			if (numRows == 0) {
				return false;
			} else {
				return true;
			}
		} catch (SQLException e) {
			System.out.println(e);
		} finally {
			closeConnection();
		}
		return true;

	}

	// accepts a user object from the called and sets it the currently logged in
	// user
	public void setCurrentUser(User user) {
		this.currentUser = user;
	}

	// returns the user object for the user with the name {userName}
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
				user.setSaltVal(result.getString(4));
				user.setPasswordLength(result.getInt(5));
				user.setAddPermission(result.getBoolean(6));
				user.setEditPermission(result.getBoolean(7));
				user.setDeletePermission(result.getBoolean(8));
				user.setEncryptedNumber(result.getString(9));
			}

			return user;
		} catch (SQLException e) {
			System.out.println(e);
			return user;
		} finally {
			closeConnection();
		}
	}

	// when user tries to log in, checks to confirm that the username exists in db
	// if rows returned == 1, user was found
	public boolean confirmUserExists(String userName) {
		openConnection();
		new_query = "SELECT COUNT(*) FROM USERS WHERE USERNAME=?";
		PreparedStatement pStmt;

		try {
			pStmt = conn.prepareStatement(new_query);
			pStmt.setString(1, userName);
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

	// inserts the data from a password object received by the caller into the
	// database
	public boolean addPasswordToDb(Password password) {
		openConnection();
		new_query = "INSERT INTO PASSWORDS (UID, APPLICATION, APPUSERNAME, PASSWORD, SALTVAL, PASSWORDLENGTH) VALUES (?,?,?,?,?, ?)";
		PreparedStatement pStmt;

		try {
			pStmt = conn.prepareStatement(new_query);
			pStmt.setInt(1, currentUser.getUserID());
			pStmt.setString(2, password.getAppName());
			pStmt.setString(3, password.getAppUserName());
			pStmt.setString(4, password.getEncryptedPassword());
			pStmt.setString(5, password.getSaltVal());
			pStmt.setInt(6, password.getPasswordLength());

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

	// updates the db entry with {oldAppName} and {oldAppUserName} and modifies
	// their user info with the new data stored in the {newPW} Password object
	public boolean editPassword(String oldAppName, String oldAppUserName, Password newPW, int userID) {
		openConnection();

		new_query = "update passwords set application=?, appUserName=?, password=?, saltVal=?, passwordLength=? where application=? and appUserName=? and uid=?";
		PreparedStatement pStmt;

		try {
			pStmt = conn.prepareStatement(new_query);
			pStmt.setString(1, newPW.getAppName());
			pStmt.setString(2, newPW.getAppUserName());
			pStmt.setString(3, newPW.getEncryptedPassword());
			pStmt.setString(4, newPW.getSaltVal());
			pStmt.setInt(5, newPW.getPasswordLength());
			pStmt.setString(6, oldAppName);
			pStmt.setString(7, oldAppUserName);
			pStmt.setInt(8, userID);

			int rowsAffected = pStmt.executeUpdate();
			if (rowsAffected == 1) {
				return true;
			} else {
				return false;
			}

		} catch (SQLException e) {
			System.out.println(e);
		}

		return true;
	}

	// updates the db entry with {oldUserName} and modifies their user info with the
	// new data stored in the {user} User object
	public boolean editUser(String oldUserName, User user) {
		openConnection();
		new_query = "update users set userName=?, password=?, saltVal=?, passwordLength=?, canadduser=?, canedituser=?, candeleteuser=?, mobileNumber=? where username=?";
		PreparedStatement pStmt;

		try {
			pStmt = conn.prepareStatement(new_query);
			pStmt.setString(1, user.getUsername());
			pStmt.setString(2, user.getEncryptedPassword());
			pStmt.setString(3, user.getSaltVal());
			pStmt.setInt(4, user.getPasswordLength());
			pStmt.setBoolean(5, user.getAddPermission());
			pStmt.setBoolean(6, user.getEditPermission());
			pStmt.setBoolean(7, user.getDeletePermission());
			pStmt.setString(8, user.getEncryptedNumber());
			pStmt.setString(9, oldUserName);
			int rowsAffected = pStmt.executeUpdate();
			if (rowsAffected == 1) {
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

	// returns all the password information for the password that has both {appName}
	// and {appUserName}
	public Password getPasswordInfo(String appName, String appUserName) {
		Password pw = new Password();
		openConnection();
		new_query = "SELECT * FROM PASSWORDS WHERE APPLICATION=? AND APPUSERNAME=? AND UID=?";
		PreparedStatement pStmt;
		try {
			pStmt = conn.prepareStatement(new_query);
			pStmt.setString(1, appName);
			pStmt.setString(2, appUserName);
			pStmt.setInt(3, currentUser.getUserID());

			ResultSet result = pStmt.executeQuery();
			while (result.next()) {
				pw.setUserID(result.getInt(2));
				pw.setAppName(result.getString(3));
				pw.setAppUserName(result.getString(4));
				pw.setEncryptedPassword(result.getString(5));
				pw.setSaltVal(result.getString(6));
				pw.setPasswordLength(result.getInt(7));
			}
		} catch (SQLException e) {
			System.out.println(e);
		} finally {
			closeConnection();
		}

		return pw;
	}

	// retrieves all passwords from the database for the logged in user
	public ArrayList<Password> getAllPasswords(int userID) {
		openConnection();
		ArrayList<Password> passwordList = new ArrayList<Password>();
		openConnection();
		new_query = "SELECT * FROM PASSWORDS WHERE UID=?";
		PreparedStatement pStmt;

		int uid = userID;

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
					tempPass.setSaltVal(result.getString(6));
					tempPass.setPasswordLength(result.getInt(7));
					passwordList.add(tempPass);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		closeConnection();
		return passwordList;
	}

	// deletes a user with {userName} from the database
	public boolean deleteUserFromDb(String userName) {
		openConnection();
		new_query = "DELETE FROM USERS WHERE USERNAME=?";
		PreparedStatement pStmt;
		try {
			pStmt = conn.prepareStatement(new_query);
			pStmt.setString(1, userName);
			int rowsAffected = pStmt.executeUpdate();
			if (rowsAffected == 1) {
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

	// returns true if user is locked out
	public boolean getUserLockoutStatus(int userID) {
		openConnection();
		new_query = "SELECT * FROM LOGINS WHERE UID=?";
		int failedLogins = -1;
		String lastLogin = "";
		PreparedStatement pStmt;
		try {
			pStmt = conn.prepareStatement(new_query);
			pStmt.setInt(1, userID);
			ResultSet result = pStmt.executeQuery();
			while (result.next()) {
				failedLogins = result.getInt(3);
				lastLogin = result.getString(4);
			}
			LocalDate lastLoginDate = LocalDate.parse(lastLogin);
			LocalDate currentDate = LocalDate.now();
			if (currentDate.isAfter(lastLoginDate)) {
				new_query = "update logins set failedAttempts=?, lastLogin=?";
				pStmt = conn.prepareStatement(new_query);
				failedLogins = 0;
				pStmt.setInt(1, 0);
				pStmt.setString(2, currentDate.toString());
				pStmt.executeUpdate();
				return false;
			} else {
				if (failedLogins > 3) {
					return true;
				}
				return false;
			}
		}

		catch (SQLException e) {
			System.out.println(e);
			return true;
		} finally {
			closeConnection();
		}
	}

	public void updateLoginAttempts(int userID, String updateType) {
		openConnection();
		if (updateType == "reset") {
			new_query = "update logins set failedAttempts=0 where uid=?";
		} else {
			new_query = "update logins set failedAttempts=failedAttempts+1 where uid=?";
		}
		PreparedStatement pStmt;
		try {
			pStmt = conn.prepareStatement(new_query);
			pStmt.setInt(1, userID);
			pStmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}

	}

	// deletes a password with both {appName} and {appUserName}
	public boolean deletePWFromDb(String appName, String appUserName) {
		openConnection();
		new_query = "DELETE FROM PASSWORDS WHERE APPLICATION=? AND APPUSERNAME=? AND UID=?";
		PreparedStatement pStmt;

		try {
			pStmt = conn.prepareStatement(new_query);
			pStmt.setString(1, appName);
			pStmt.setString(2, appUserName);
			pStmt.setInt(3, currentUser.getUserID());
			int rowsAffected = pStmt.executeUpdate();
			if (rowsAffected == 1) {
				return true;
			} else {
				return false;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			closeConnection();
		}
	}

	public int getFailedLogins(String userName) {
		openConnection();
		new_query = "SELECT ID FROM USERS WHERE USERNAME=?";
		PreparedStatement pStmt;
		int userID = 0;
		int failedAttempts = 0;
		try {
			pStmt = conn.prepareStatement(new_query);
			pStmt.setString(1, userName);
			ResultSet result = pStmt.executeQuery();
			while (result.next()) {
				userID = result.getInt(1);
			}
			if (userID != 0) {
				new_query = "SELECT FAILEDATTEMPTS FROM LOGINS WHERE UID=?";
				pStmt = conn.prepareStatement(new_query);
				pStmt.setInt(1, userID);
				ResultSet attemptResult = pStmt.executeQuery();
				while (attemptResult.next()) {
					failedAttempts = attemptResult.getInt(1);
				}
				return failedAttempts;
			}
			return -1;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		} finally {
			closeConnection();
		}
	}
}