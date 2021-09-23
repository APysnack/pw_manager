package views;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import controller.Controller;
import model.DbConnection;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

public class LoginPanel extends JPanel implements FocusListener, ActionListener, KeyListener {
	
	JTextField usrField;
	JPasswordField pwField;
	JButton loginBtn;
	JPanel scrnMgr;
	CardLayout cl;
	Controller ctrl;
	Utils u;
	DbConnection conn;
	
	public LoginPanel(Controller ctrl, CardLayout cl, JPanel scrnMgr, DbConnection conn){
		
		setLayout(new BorderLayout());
		
		JButton lgnBtn = new JButton("Log In");
		JLabel lgnLbl = new JLabel("Please Enter your Login Information");
		JTextField usrField = new JTextField("Enter Username", 15);
		JPasswordField pwField = new JPasswordField("Enter Password", 15);
		JPanel ctrLblPnl = new JPanel();
		
		Utils u = new Utils();
		
		this.conn = conn;
		this.u = u;
		this.ctrl = ctrl;
		this.scrnMgr = scrnMgr;
		this.cl = cl;
		this.loginBtn = lgnBtn;
		this.usrField = usrField;
		this.pwField = pwField;
		
		JPanel innerPanel = new JPanel(new GridLayout(4, 1, 2, 2));
		innerPanel.add(lgnBtn);
		add(ctrLblPnl);
		innerPanel.add(ctrLblPnl);
		innerPanel.add(usrField);
		innerPanel.add(pwField);
		innerPanel.add(lgnBtn);
		
		usrField.addFocusListener(this);
		pwField.addFocusListener(this);
		lgnBtn.addActionListener(this);
		
		
		JPanel midPanel = new JPanel(new BorderLayout());
		JPanel pad = u.pad(140, 140, 140, 140);
		JPanel pad2 = u.pad(140, 140, 140, 140);
		JPanel pad3 = u.pad(95, 95, 95, 95);
		JPanel pad4 = u.pad(95, 95, 95, 95);

		midPanel.add(innerPanel, BorderLayout.CENTER);
		midPanel.add(pad, BorderLayout.WEST);
		midPanel.add(pad2, BorderLayout.EAST);
		midPanel.add(pad3, BorderLayout.NORTH);
		midPanel.add(pad4, BorderLayout.SOUTH);
		add(midPanel, BorderLayout.CENTER);
		
		usrField.addKeyListener(this);
		pwField.addKeyListener(this);
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
		if(source == loginBtn) {
			attemptLogin();
		}
	}

	public void attemptLogin() {
		String userName = usrField.getText();
		String password = String.valueOf(pwField.getPassword());
		
		boolean userAuthenticated = ctrl.authenticateUser(userName, password);
		if(userAuthenticated == true) {
			cl.show(scrnMgr, "User");
		}
		else {
			cl.show(scrnMgr, "Error");
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
