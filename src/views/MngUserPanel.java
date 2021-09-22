package views;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;
import java.awt.GridLayout;

import java.awt.BorderLayout;

import controller.Controller;
import model.DbConnection;
import pw_manager.User;

public class MngUserPanel extends JPanel implements ActionListener, FocusListener, ComponentListener {

	JTextField usrField;
	JPasswordField pwField;
	JButton createUserBtn;
	JPanel scrnMgr;
	CardLayout cl;
	Controller ctrl;
	Utils u;
	DbConnection conn;
	JComboBox<String> userComboBox;
	JButton addUserBtn;
	List<JCheckBox> checkBoxes;
	JButton backButton;

	public MngUserPanel() {

	}

	public MngUserPanel(Controller ctrl, CardLayout cl, JPanel scrnMgr, DbConnection conn) {
		setName("manageUserPanel");
		this.conn = conn;
		this.cl = cl;
		this.scrnMgr = scrnMgr;
		this.ctrl = ctrl;

		JLabel titleLbl = new JLabel("Manage Users");

		JLabel modifyLbl = new JLabel("Select a user to modify");
		userComboBox = new JComboBox<String>();
		addUserBtn = new JButton("Add new user");

		JCheckBox addPermission = new JCheckBox("Add Users");
		JCheckBox editPermission = new JCheckBox("Edit Users");
		JCheckBox deletePermission = new JCheckBox("Delete Users");
		
		Border checkBoxBorder = BorderFactory.createTitledBorder("User Privileges");
		JPanel checkBoxPnl = new JPanel();
		checkBoxPnl.add(addPermission);
		checkBoxPnl.add(editPermission);
		checkBoxPnl.add(deletePermission);
		checkBoxPnl.setBorder(checkBoxBorder);

		JLabel unameLbl = new JLabel("Username");
		usrField = new JTextField("Enter Username", 15);
		JLabel pwLbl = new JLabel("Password");
		pwField = new JPasswordField("Enter Password", 15);
		backButton = new JButton("Back");

		checkBoxes = new ArrayList<JCheckBox>();
		checkBoxes.add(addPermission);
		checkBoxes.add(editPermission);
		checkBoxes.add(deletePermission);
		

		usrField.addFocusListener(this);
		pwField.addFocusListener(this);
		userComboBox.addActionListener(this);
		addUserBtn.addActionListener(this);
		backButton.addActionListener(this);
		addComponentListener(this);

		add(titleLbl);
		add(modifyLbl);
		add(unameLbl);
		add(usrField);
		add(pwLbl);
		add(pwField);
		add(userComboBox);
		add(addUserBtn);
		add(checkBoxPnl);
		add(backButton);
	}

	public void populateUserData() {
		String selectedUser = (String) userComboBox.getSelectedItem();
		User user = ctrl.getUserInfo(selectedUser);
		usrField.setText(user.getUsername());
		int userPWLen = user.getPasswordLength();
		String pwStr = "";
		for (int i = 0; i < userPWLen; i++) {
			pwStr += "*";
		}
		pwField.setText(pwStr);
		
		List<Boolean> permissionList = user.getUserPermissions();
		
		for (int i = 0; i < permissionList.size(); i++) {
			checkBoxes.get(i).setSelected(permissionList.get(i));
		}
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == userComboBox) {
			populateUserData();
		} else if (source == addUserBtn) {
			System.out.println("add user");
		} else if (source == backButton) {
			cl.show(scrnMgr, "User");
		}

	}

	@Override
	public void focusGained(FocusEvent e) {
		Object source = e.getSource();
		if (source == usrField) {
			if (usrField.getText().equals("Enter Username")) {
				usrField.setText("");
			}
		}

		else if (source == pwField) {
			pwField.setEchoChar('*');
			boolean allAsterisks = false;

			if (String.valueOf(pwField.getPassword()).matches("[*]*")) {
				allAsterisks = true;
			}

			if (String.valueOf(pwField.getPassword()).equals("Enter Password") || allAsterisks == true) {
				pwField.setText("");
			}
		}
	}

	@Override
	public void focusLost(FocusEvent e) {
		Object source = e.getSource();
		if (source == usrField) {
			if (usrField.getText().isEmpty()) {
				usrField.setText("Enter Username");
			}
		} else if (source == pwField) {
			if (String.valueOf(pwField.getPassword()).equals("")) {
				pwField.setText("Enter Password");
				pwField.setEchoChar((char) 0);
			}
		}
	}

	@Override
	public void componentResized(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentShown(ComponentEvent e) {
		if (((Component) e.getSource()).getName() == "manageUserPanel") {
			ArrayList<String> allUsers = conn.getAllUserNames();
			for (int i = 0; i < allUsers.size(); i++) {
				userComboBox.insertItemAt(allUsers.get(i), i);
			}
			userComboBox.setSelectedIndex(0);
		}
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub

	}
}
