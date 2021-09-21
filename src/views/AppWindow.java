package views;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import controller.Controller;
import model.DbConnection;
import model.DbConnection;

public class AppWindow extends JFrame {
	
	CardLayout cl;
	JPanel scrnMgr;
	JPanel lgnPanel;
	DbConnection conn;
	String userName;
	Controller ctrl;
	
	public AppWindow() {
		
		// builds the App window, sets size, etc.
		buildAppWindow();
		
		// creates a database connection
		conn = new DbConnection();
		
		// creates a controller object, passes it the db connection
		Controller ctrl = new Controller(conn);
		this.ctrl = ctrl;
		
		// initial set-up to switch between screens in the GUI
		initializeScreenManager();
		
		// fetches the number of users in the database
		int numUsers = conn.getRowCountFromTable("users");
		
		// if the database is empty, prompts the window to create a new user
		if(numUsers == 0) {
			cl.show(scrnMgr, "Setup");
		}
		
		// otherwise, a user already exists in the database. prompts the user to log in
		else {
			cl.show(scrnMgr, "Login");
		}
		
		this.pack();
	}
	
	public void buildAppWindow() {
		DbConnection connect = new DbConnection();
		this.conn = connect;
		this.setSize(900, 550);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension dim = tk.getScreenSize();
		int xPos = (dim.width / 2) - (this.getWidth() / 2);
		int yPos = (dim.height / 2) - (this.getHeight() / 2);
		this.setLocation(xPos, yPos);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Password Manager");
	}
	
	public void initializeScreenManager(){
		CardLayout cl = new CardLayout();
		this.cl = cl;
		this.scrnMgr = new JPanel(cl);
		
		JPanel loginPanel = buildLoginPanel();
		JPanel mngUsrPanel = buildMngUserPanel();
		JPanel mngPWPanel = buildMngPWPanel();
		JPanel setupPanel = buildSetupPanel();
		JPanel errorPanel = buildErrorPanel();
		JPanel userPanel = buildUserPanel();
		
		scrnMgr.add(mngUsrPanel, "ManageUsers");
		scrnMgr.add(mngPWPanel, "ManagePasswords");
		scrnMgr.add(errorPanel, "Error");
		scrnMgr.add(setupPanel, "Setup");
		scrnMgr.add(loginPanel, "Login");
		scrnMgr.add(userPanel, "User");
		
		this.add(scrnMgr);
	}
	
	public JPanel buildLoginPanel() {
		LoginPanel loginPnl = new LoginPanel(ctrl, cl, scrnMgr);
		return loginPnl;
	}
	
	public JPanel buildSetupPanel() {
		SetupPanel setupPnl = new SetupPanel(ctrl, cl, scrnMgr);
		return setupPnl;
	}
	
	public JPanel buildErrorPanel() {
		ErrorPanel errorPnl = new ErrorPanel(ctrl, cl, scrnMgr);
		return errorPnl;
	}
	
	public JPanel buildUserPanel() {
		UserPanel userPnl = new UserPanel(ctrl, cl, scrnMgr);
		return userPnl;
	}
	
	public JPanel buildMngUserPanel() {
		MngUserPanel mngUserPnl = new MngUserPanel(ctrl, cl, scrnMgr);
		return mngUserPnl;
	}
	
	public JPanel buildMngPWPanel() {
		MngPWPanel mngPWPnl = new MngPWPanel(ctrl, cl, scrnMgr);
		return mngPWPnl;
	}
	
}
