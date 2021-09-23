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
import java.util.Arrays;
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

	CardLayout cl;
	Controller ctrl;
	Utils u;
	DbConnection conn;
	
	List<JCheckBox> checkBoxes;
	JComboBox<String> userComboBox;
	JTextField usrField;
	JPasswordField pwField;
	JPasswordField confirmPWField;
	JPanel scrnMgr;
	JPanel checkBoxPnl;
	JLabel titleLbl;
	JLabel unameLbl;
	JLabel pwLbl;
	JLabel confirmPWLbl;
	JLabel actionLbl;
	JButton addUserBtn;
	JButton backButton;
	JButton createUsrBtn;
	
	public MngUserPanel() {

	}

	public MngUserPanel(Controller ctrl, CardLayout cl, JPanel scrnMgr, DbConnection conn) {
		setName("manageUserPanel");
		this.conn = conn;
		this.cl = cl;
		this.scrnMgr = scrnMgr;
		this.ctrl = ctrl;
		initializeMngUserPanel();
	}
	
	public void initializeMngUserPanel() {
		titleLbl = new JLabel("Manage Users");
		actionLbl = new JLabel("Select a user to modify");
		userComboBox = new JComboBox<String>();
		addUserBtn = new JButton("Add new user");

		JCheckBox addPermission = new JCheckBox("Add Users");
		JCheckBox editPermission = new JCheckBox("Edit Users");
		JCheckBox deletePermission = new JCheckBox("Delete Users");
		
		Border checkBoxBorder = BorderFactory.createTitledBorder("User Privileges");
		checkBoxPnl = new JPanel();
		checkBoxPnl.add(addPermission);
		checkBoxPnl.add(editPermission);
		checkBoxPnl.add(deletePermission);
		checkBoxPnl.setBorder(checkBoxBorder);

		unameLbl = new JLabel("Username");
		usrField = new JTextField("Enter Username", 15);
		pwLbl = new JLabel("Password");
		pwField = new JPasswordField("Enter Password", 15);
		confirmPWLbl = new JLabel("Confirm Password");
		confirmPWField = new JPasswordField("Confirm Password", 15);
		backButton = new JButton("Back");
		createUsrBtn = new JButton("Create User");

		checkBoxes = new ArrayList<JCheckBox>();
		checkBoxes.add(addPermission);
		checkBoxes.add(editPermission);
		checkBoxes.add(deletePermission);
		

		usrField.addFocusListener(this);
		pwField.addFocusListener(this);
		confirmPWField.addFocusListener(this);
		userComboBox.addActionListener(this);
		addUserBtn.addActionListener(this);
		createUsrBtn.addActionListener(this);
		backButton.addActionListener(this);
		addComponentListener(this);

		add(titleLbl);
		add(actionLbl);
		add(unameLbl);
		add(usrField);
		add(pwLbl);
		add(pwField);
		add(confirmPWLbl);
		add(confirmPWField);
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
		confirmPWField.setText(pwStr);
		
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
			usrField.setText("");
			pwField.setText("");
			confirmPWField.setText("");
			actionLbl.setText("Creating new user");
			addUserBtn.setVisible(false);
			userComboBox.setVisible(false);
			for(int i = 0; i < checkBoxes.size(); i++) {
				checkBoxes.get(i).setSelected(false);
			}
			add(createUsrBtn);
			
		} else if (source == backButton) {
			removeAll();
			repaint();
			revalidate();
			initializeMngUserPanel();
			cl.show(scrnMgr, "User");
		}
		else if (source == createUsrBtn) {
			String username = usrField.getText();
			char[] password  = pwField.getPassword();
			char[] passwordConfirm = confirmPWField.getPassword();
			
			if (Arrays.equals(password, passwordConfirm) == true) {
				String stringPW = String.valueOf(password);
				boolean addPermission = checkBoxes.get(0).isSelected();
				boolean editPermission = checkBoxes.get(1).isSelected();
				boolean deletePermission = checkBoxes.get(2).isSelected();
				
				boolean userAdded = ctrl.addUser(username, stringPW, addPermission, editPermission, deletePermission);
				if(userAdded) {
					//TODO: successful add.
				}
				else {
					//TODO: unsuccessful add.
				}
			}
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
			passwordFieldGainFocus(pwField);
		}
		
		else if (source == confirmPWField) {
			passwordFieldGainFocus(confirmPWField);
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
			passwordFieldLoseFocus(pwField);
		}
		else if (source == confirmPWField) {
			passwordFieldLoseFocus(confirmPWField);
		}
	}

	@Override
	public void componentShown(ComponentEvent e) {
		if (((Component) e.getSource()).getName() == "manageUserPanel") {
			userComboBox.removeAllItems();
			ArrayList<String> allUsers = conn.getAllUserNames();
			for (int i = 0; i < allUsers.size(); i++) {
				userComboBox.insertItemAt(allUsers.get(i), i);
			}
			userComboBox.setSelectedIndex(0);
		}
	}
	
	public void passwordFieldGainFocus(JPasswordField pwField) {
		pwField.setEchoChar('*');
		boolean allAsterisks = false;

		if (String.valueOf(pwField.getPassword()).matches("[*]*")) {
			allAsterisks = true;
		}

		if (String.valueOf(pwField.getPassword()).equals("Enter Password") || allAsterisks == true) {
			pwField.setText("");
		}
	}
	
	public void passwordFieldLoseFocus(JPasswordField pwField) {
		if (String.valueOf(pwField.getPassword()).equals("")) {
			pwField.setText("Enter Password");
			pwField.setEchoChar((char) 0);
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
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub

	}
}
