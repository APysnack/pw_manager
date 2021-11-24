package views;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import controller.Controller;
import model.DbConnection;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.CardLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.awt.font.TextAttribute;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;




public class LoginPanel extends JPanel implements FocusListener, ActionListener, KeyListener {
	
	JPanel scrnMgr;
	CardLayout cl;
	Controller ctrl;
	Utils u;
	DbConnection conn;
	JLabel flashLbl;
	JLabel logoLbl;
	JLabel logoTxt;
	JLabel lgnLbl;
	JLabel usrLbl;
	JLabel pwLbl;
	JTextField usrField;
	JPasswordField pwField;
	JButton lgnBtn;
	LogoPanel logoPanel;
	JPanel userPanel;
	JPanel pwPanel;
	int failedAttemptSimulator = 1;
	
	public LoginPanel(Controller ctrl, CardLayout cl, JPanel scrnMgr, DbConnection conn){
		u = new Utils();
		
		this.conn = conn;
		this.ctrl = ctrl;
		this.scrnMgr = scrnMgr;
		this.cl = cl;
		
		logoPanel = new LogoPanel("PLEASE LOG IN", 270);
		flashLbl = new JLabel("");
		lgnBtn = new JButton("Log In");
		usrField = new JTextField("Enter Username", 15);
		usrLbl = new JLabel("Username");
		pwField = new JPasswordField("Enter Password", 15);
		pwLbl = new JLabel("Password");
		userPanel = new JPanel();
		pwPanel = new JPanel();
		
		loginPanelLayout();
		
		usrField.addKeyListener(this);
		pwField.addKeyListener(this);
		usrField.addFocusListener(this);
		pwField.addFocusListener(this);
		lgnBtn.addActionListener(this);
	}
	
	public void loginPanelLayout() {
		setLayout(new GridBagLayout());
		GridBagConstraints grid = new GridBagConstraints();
		
		grid.gridx = 3;
		grid.gridy = 1;
		add(flashLbl, grid);
		
		grid.insets = new Insets(15,0,0,0);
		grid.gridx = 3;
		grid.gridy = 2;
		add(logoPanel, grid);
		
		userPanel.add(usrLbl);
		userPanel.add(usrField);
		
		grid.gridx = 3;
		grid.gridy = 3;
		add(userPanel, grid);
		
		pwPanel.add(pwLbl);
		pwPanel.add(pwField);
		
		grid.gridx = 3;
		grid.gridy = 4;
		add(pwPanel, grid);
		
		grid.insets = new Insets(50,165,0,0);
		grid.gridx = 3;
		grid.gridy = 5;
		add(lgnBtn, grid);
	}
	
	@Override
	public void focusGained(FocusEvent e) {
		Object source = e.getSource();
		if(source == usrField) {
			if (usrField.getText().equals("Enter Username")) {
				usrField.setText("");
			}
		}
		
		else if(source == pwField) {
			pwField.setEchoChar('*');
			if (String.valueOf(pwField.getPassword()).equals("Enter Password")) {
				pwField.setText("");
			}
		}
	}

	@Override
	public void focusLost(FocusEvent e) {
		Object source = e.getSource();
		if(source == usrField) {
			if (usrField.getText().isEmpty()) {
				usrField.setText("Enter Username");
			}
		}
		else if(source == pwField) {
			if (String.valueOf(pwField.getPassword()).equals("")) {
				pwField.setText("Enter Password");
				pwField.setEchoChar((char) 0);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if(source == lgnBtn) {
			attemptLogin();
		}
	}

	public void attemptLogin() {
		String userName = usrField.getText();
		String password = String.valueOf(pwField.getPassword());
		boolean userAuthenticated = ctrl.authenticateUser(userName, password, "login");
		if(userAuthenticated == true) {
			cl.show(scrnMgr, "Home");
		}
		else {
			if(password.length() < 8 || password.length() > 128) {
				flashLbl.setText("Passwords must be between 8 - 128 characters");
			}
			else {
				int failedAttempts = conn.getFailedLogins(userName);
				if(failedAttempts == -1) {
					if(failedAttemptSimulator < 4) {
						flashLbl.setText("Login failed. After " + (4 - failedAttemptSimulator) + " more failed attempts, your account will be temporarily locked out");
						failedAttemptSimulator++;
					}
					else {
						flashLbl.setText("Your account is temporarily locked out. Try again tomorrow.");
					}
					
				}
				else {
					if(failedAttempts < 4) {
						flashLbl.setText("Login failed. After " + (4 - failedAttempts) + " more failed attempts, your account will be temporarily locked out");
					}
					else {
						flashLbl.setText("Your account is temporarily locked out. Try again tomorrow.");
					}
					
					
				}
			}
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == 10) {
			attemptLogin();
		}
		
	}
	
}
