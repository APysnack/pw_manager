package pw_manager;

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

public class LoginWindow extends JFrame {
	
	CardLayout cl;
	JPanel scrnMgr;
	JPanel lgnPanel;
	DbConnection conn;
	String userName;
	Controller ctrl;
	
	LoginWindow() {
		buildLoginWindow();
		conn = new DbConnection();
		cl = new CardLayout();
		Controller ctrl = new Controller(conn);
		
		this.ctrl = ctrl;
		this.scrnMgr = new JPanel(cl);
		
		JPanel loginPanel = buildLoginPanel();
		JPanel assignments = buildAssignments();
		scrnMgr.add(loginPanel, "Login Panel");
		scrnMgr.add(assignments, "Assignments");
		
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
		JPanel mainPanel = new JPanel(new BorderLayout());
		JPanel pad = new JPanel();
		pad.setBorder(new EmptyBorder(140, 140, 140, 140));
		JPanel pad2 = new JPanel();
		pad2.setBorder(new EmptyBorder(140, 140, 140, 140));
		JPanel pad3 = new JPanel();
		pad3.setBorder(new EmptyBorder(95, 95, 95, 95));
		JPanel pad4 = new JPanel();
		pad4.setBorder(new EmptyBorder(95, 95, 95, 95));
		JPanel midPanel = new JPanel(new BorderLayout());

		LoginPanel lgnPnl = new LoginPanel(ctrl, cl, scrnMgr);

		midPanel.add(lgnPnl, BorderLayout.CENTER);
		midPanel.add(pad, BorderLayout.WEST);
		midPanel.add(pad2, BorderLayout.EAST);
		midPanel.add(pad3, BorderLayout.NORTH);
		midPanel.add(pad4, BorderLayout.SOUTH);
		mainPanel.add(midPanel, BorderLayout.CENTER);
		return mainPanel;
	}
	
	public JPanel buildAssignments() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		JPanel pad = new JPanel();
		pad.setBorder(new EmptyBorder(140, 140, 140, 140));
		JPanel pad2 = new JPanel();
		pad2.setBorder(new EmptyBorder(140, 140, 140, 140));
		JPanel pad3 = new JPanel();
		pad3.setBorder(new EmptyBorder(95, 95, 95, 95));
		JPanel pad4 = new JPanel();
		pad4.setBorder(new EmptyBorder(95, 95, 95, 95));
		JPanel midPanel = new JPanel(new BorderLayout());
		midPanel.add(pad, BorderLayout.WEST);
		midPanel.add(pad2, BorderLayout.EAST);
		midPanel.add(pad3, BorderLayout.NORTH);
		midPanel.add(pad4, BorderLayout.SOUTH);
		mainPanel.add(midPanel, BorderLayout.CENTER);
		return mainPanel;
	}
}
