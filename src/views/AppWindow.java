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
		buildLoginWindow();
		conn = new DbConnection();
		cl = new CardLayout();
		Controller ctrl = new Controller(conn);
		
		this.ctrl = ctrl;
		this.scrnMgr = new JPanel(cl);
		
		JPanel loginPanel = buildLoginPanel();
		JPanel setupPanel = buildSetupPanel();
		JPanel errorPanel = buildErrorPanel();
		
		scrnMgr.add(errorPanel, "Error");
		scrnMgr.add(setupPanel, "Setup");
		scrnMgr.add(loginPanel, "Login");
		
		int numUsers = conn.getRowCountFromTable("users");
		
		if(numUsers == 0) {
			cl.show(scrnMgr, "Setup");
		}
		else {
			cl.show(scrnMgr, "Login");
		}
		
		this.add(scrnMgr);
		this.pack();
	}
	
	public void buildLoginWindow() {
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
	
	
}
