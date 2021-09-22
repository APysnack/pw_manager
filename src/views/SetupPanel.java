package views;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import java.awt.GridLayout;

import java.awt.BorderLayout;

import controller.Controller;
import model.DbConnection;

public class SetupPanel extends JPanel implements FocusListener, ActionListener {

	JTextField usrField;
	JPasswordField pwField;
	JButton createUserBtn;
	JPanel scrnMgr;
	CardLayout cl;
	Controller ctrl;
	Utils u;
	DbConnection conn;

	public SetupPanel() {
		
	}
	
	public SetupPanel(Controller ctrl, CardLayout cl, JPanel scrnMgr, DbConnection conn) {

		Utils u = new Utils();
		this.u = u;
		this.ctrl = ctrl;
		this.cl = cl;
		this.scrnMgr = scrnMgr;
		this.conn = conn;
		
		setLayout(new BorderLayout());

		JPanel welcomePnl = buildWelcomePanel();
		JPanel disclaimerPnl = buildDisclaimerPanel();
		JPanel checkBoxPnl = buildCheckBoxPanel();
		JPanel userInfoPnl = buildUserInfoPanel();

		JPanel innerPnl = new JPanel(new GridLayout(4, 1, 0, 3));
		
		innerPnl.add(welcomePnl);
		innerPnl.add(userInfoPnl);
		innerPnl.add(checkBoxPnl);
		innerPnl.add(disclaimerPnl);
		
		JPanel mainPnl = new JPanel(new BorderLayout());
		
		JPanel padN = u.pad(30, 30, 30, 30);
		JPanel padW = u.pad(70, 70, 70, 70);
		JPanel padE = u.pad(70, 70, 70, 70);
		JPanel padS = u.pad(30, 30, 30, 30);
		
		mainPnl.add(padN, BorderLayout.NORTH);
		mainPnl.add(padW, BorderLayout.WEST);
		mainPnl.add(padE, BorderLayout.EAST);
		mainPnl.add(padS, BorderLayout.SOUTH);
		mainPnl.add(innerPnl, BorderLayout.CENTER);
		
		add(mainPnl);
	}

	public JPanel buildWelcomePanel() {
		// creates and center top label
		JLabel welcomeLbl = new JLabel(
				"Welcome! Since this is your first time using this app, you must create a username and password");
		welcomeLbl.setHorizontalAlignment(JLabel.CENTER);
		JPanel welcomePnl = new JPanel(new GridLayout(4, 1, 0, 3));
		JPanel welcomePnlPad = u.pad(10, 10, 10, 10);
		welcomePnl.add(welcomePnlPad);
		welcomePnl.add(welcomeLbl);

		return welcomePnl;
	}

	public JPanel buildDisclaimerPanel() {
		// creates and centers bottom labels
		JLabel disclaimerLbl = new JLabel("WARNING: This user will be able to add, delete, and modify other users");
		JLabel disclaimerLbl2 = new JLabel(
				"Use a SECURE password, as this will control access to all your other passwords");
		disclaimerLbl.setHorizontalAlignment(JLabel.CENTER);
		disclaimerLbl2.setHorizontalAlignment(JLabel.CENTER);
		JPanel disclaimerPnl = new JPanel(new GridLayout(4, 1, 0, 3));
		disclaimerPnl.add(disclaimerLbl);
		disclaimerPnl.add(disclaimerLbl2);
		
		JButton createUserBtn = new JButton("I understand. Create this user");
		createUserBtn.addActionListener(this);
		this.createUserBtn = createUserBtn;
		disclaimerPnl.add(createUserBtn);
		
		return disclaimerPnl;
	}

	public JPanel buildCheckBoxPanel() {
		// creates checkbox
		JCheckBox addUsersCheck = new JCheckBox("Add Users", true);
		JCheckBox editUsersCheck = new JCheckBox("Edit Users", true);
		JCheckBox deleteUsersCheck = new JCheckBox("Delete Users", true);
		JCheckBox managePWsCheck = new JCheckBox("Manage Passwords", true);
		JPanel checkBoxPnl = new JPanel();
		Border checkBoxBorder = BorderFactory.createTitledBorder("User Privileges");
		checkBoxPnl.add(addUsersCheck);
		checkBoxPnl.add(editUsersCheck);
		checkBoxPnl.add(deleteUsersCheck);
		checkBoxPnl.add(managePWsCheck);
		checkBoxPnl.setBorder(checkBoxBorder);

		addUsersCheck.setEnabled(false);
		editUsersCheck.setEnabled(false);
		deleteUsersCheck.setEnabled(false);
		managePWsCheck.setEnabled(false);

		return checkBoxPnl;
	}

	public JPanel buildUserInfoPanel() {
		// label/input box for username
		JLabel userLbl = new JLabel("Username");
		JTextField userTxtFld = new JTextField("Enter Username", 15);
		Box userNameBox = Box.createHorizontalBox();
		userNameBox.add(userLbl);
		userNameBox.add(Box.createHorizontalStrut(5));
		userNameBox.add(userTxtFld);

		// label/input box for password
		JLabel pwLbl = new JLabel("Password");
		JPasswordField pwTxtFld = new JPasswordField("Enter Password", 15);
		Box pwBox = Box.createHorizontalBox();
		pwBox.add(pwLbl);
		pwBox.add(Box.createHorizontalStrut(6));
		pwBox.add(pwTxtFld);
		
		userTxtFld.addFocusListener(this);
		pwTxtFld.addFocusListener(this);
		
		this.usrField = userTxtFld;
		this.pwField = pwTxtFld;

		JPanel innerPnl = new JPanel(new GridLayout(3, 1, 0, 1));
		innerPnl.add(userNameBox);
		innerPnl.add(pwBox);
		
		return innerPnl;
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
		if(source == createUserBtn) {
			String userName = usrField.getText();
			String password = String.valueOf(pwField.getPassword());
			
			boolean addSuccessful = ctrl.addUser(userName, password, true, true, true);
			
			if(addSuccessful == true) {
				AppWindow window = (AppWindow) SwingUtilities.getWindowAncestor(this);
				window.rebuildApp();
			}
			else {
				cl.show(scrnMgr, "Error");
			}
		}

	}
}