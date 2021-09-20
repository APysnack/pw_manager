package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import views.LoginWindow;

public class DbConnection {
	String dbpath;
	String new_query;
	ResultSet result;
	Connection conn;
	
	public DbConnection(){
		try {
			this.dbpath = "jdbc:sqlite:pwdb.db";
			Connection conn = DriverManager.getConnection(dbpath);
			this.conn = conn;
		} catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	public ArrayList<String> get_all_from_db() {
		new_query = "select * from passwords";
		ArrayList<String> arrList = new ArrayList<String>();
		
		Statement stmt;
		
		try {
			stmt = conn.createStatement();
			result = stmt.executeQuery(new_query);
			
			while(result.next()) {
				arrList.add(result.getString(2));
			}
			
			return arrList;

		} catch (SQLException e) {
			System.out.println(e);
			return arrList;
		}
		
	}
}