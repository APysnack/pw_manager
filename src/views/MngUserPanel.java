package views;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class MngUserPanel extends JPanel implements ActionListener, FocusListener {

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
		this.conn = conn;
		this.cl = cl;
		this.scrnMgr = scrnMgr;
		this.ctrl = ctrl;
		
		JLabel titleLbl = new JLabel("Manage Users");
		
		JLabel modifyLbl = new JLabel("Select a user to modify");
		ArrayList<String> allUsers = conn.getAllUserNames();
		String[] userList = allUsers.toArray(new String[allUsers.size()]);
		userComboBox = new JComboBox<String>(userList);
		addUserBtn = new JButton("Add new user");
		
		JCheckBox addPermission = new JCheckBox("Add Users");
		JCheckBox editPermission = new JCheckBox("Edit Users");
		JCheckBox deletePermission = new JCheckBox("Delete Users");
		
		JLabel unameLbl = new JLabel("Username");
		usrField = new JTextField("Enter Username");
		JLabel pwLbl = new JLabel("Password");
		pwField = new JPasswordField("Enter Password");
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
		
		add(titleLbl);
		add(modifyLbl);
		add(unameLbl);
		add(usrField);
		add(pwLbl);
		add(pwField);
		add(userComboBox);
		add(addUserBtn);
		for(int i = 0; i < checkBoxes.size(); i++) {
			add(checkBoxes.get(i));
		}
		add(backButton);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if(source == userComboBox) {
			String selectedUser = (String) userComboBox.getSelectedItem();
			ctrl.getUserInfo(selectedUser);
		}
		else if(source == addUserBtn) {
			System.out.println("add user");
		}
		else if(source == backButton) {
			cl.previous(scrnMgr);
		}
		
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
}
