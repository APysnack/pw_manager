package views;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;
import java.awt.GridLayout;

import java.awt.BorderLayout;

import controller.Controller;
import model.DbConnection;
import pw_manager.User;

public class MngUserPanel extends JPanel implements ActionListener, FocusListener, ComponentListener, KeyListener {

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
	JLabel flashLbl;
	JLabel titleLbl;
	JLabel unameLbl;
	JLabel pwLbl;
	JLabel confirmPWLbl;
	JLabel actionLbl;
	JButton addUserBtn;
	JButton backButton;
	JButton createUsrBtn;
	JButton editUsrBtn;
	JButton deleteUsrBtn;
	boolean onAddUserPage;
	
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
		onAddUserPage = false;
		titleLbl = new JLabel("Manage Users");
		actionLbl = new JLabel("Select a user to modify");
		userComboBox = new JComboBox<String>();
		addUserBtn = new JButton("Add New User");

		JCheckBox addPermission = new JCheckBox("Add Users");
		JCheckBox editPermission = new JCheckBox("Edit Users");
		JCheckBox deletePermission = new JCheckBox("Delete Users");
		
		Border checkBoxBorder = BorderFactory.createTitledBorder("User Privileges");
		checkBoxPnl = new JPanel();
		checkBoxPnl.add(addPermission);
		checkBoxPnl.add(editPermission);
		checkBoxPnl.add(deletePermission);
		checkBoxPnl.setBorder(checkBoxBorder);

		flashLbl = new JLabel("");
		unameLbl = new JLabel("Edit Username");
		usrField = new JTextField("Enter Username", 15);
		pwLbl = new JLabel("Edit Password");
		pwField = new JPasswordField("Enter Password", 15);
		confirmPWLbl = new JLabel("Confirm Password");
		confirmPWField = new JPasswordField("Confirm Password", 15);
		backButton = new JButton("Back");
		createUsrBtn = new JButton("Create User");
		editUsrBtn = new JButton("Apply Changes To User");
		deleteUsrBtn = new JButton("Delete User");
		
		checkBoxes = new ArrayList<JCheckBox>();
		checkBoxes.add(addPermission);
		checkBoxes.add(editPermission);
		checkBoxes.add(deletePermission);
		
		flashLbl.setVisible(false);

		usrField.addFocusListener(this);
		pwField.addFocusListener(this);
		confirmPWField.addFocusListener(this);
		userComboBox.addActionListener(this);
		addUserBtn.addActionListener(this);
		createUsrBtn.addActionListener(this);
		editUsrBtn.addActionListener(this);
		backButton.addActionListener(this);
		deleteUsrBtn.addActionListener(this);
		addComponentListener(this);
		
		add(flashLbl);
		add(titleLbl);
		add(actionLbl);
		add(userComboBox);
		add(unameLbl);
		add(usrField);
		add(pwLbl);
		add(pwField);
		add(confirmPWLbl);
		add(confirmPWField);
		add(checkBoxPnl);
		add(editUsrBtn);
		add(addUserBtn);
		add(deleteUsrBtn);
		add(backButton);
	}

	public void attemptUserCreation() {
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
				flashLbl.setText("User added successfully!");
			}
			else {
				flashLbl.setText("User could not be added. Please check your input and try again.");
			}
		}
		
		else {
			flashLbl.setText("The passwords you've entered do not match. Please try again");
		}
		
		flashLbl.setVisible(true);
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
			onAddUserPage = true;
			editUsrBtn.setVisible(false);
			deleteUsrBtn.setVisible(false);
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
			if(onAddUserPage == false) {
				initializeMngUserPanel();
				cl.show(scrnMgr, "User");
			}
			else {
				initializeMngUserPanel();
				initializeComboBox();
				cl.show(scrnMgr, "ManageUsers");
			}
		}
		else if (source == createUsrBtn) {
			attemptUserCreation();
		}
		else if(source == deleteUsrBtn) {
			String name = (String) userComboBox.getSelectedItem();
			JFrame alert = new JFrame();
			int response = JOptionPane.showConfirmDialog(alert, "This will delete all passwords for " + name + ". Do you wish to continue?","WARNING!!!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if(response == 0) {
				boolean userDeleted = ctrl.deleteUser(name);
				if (userDeleted == true) {
					flashLbl.setText("User successfully deleted!");
					flashLbl.setVisible(true);
				}
				else {
					flashLbl.setText("ERROR: User could not be deleted");
					flashLbl.setVisible(false);
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

	public void initializeComboBox() {
		userComboBox.removeAllItems();
		ArrayList<String> allUsers = conn.getAllUserNames();
		
		System.out.println(allUsers);
		
		for (int i = 0; i < allUsers.size(); i++) {
			userComboBox.insertItemAt(allUsers.get(i), i);
		}
		userComboBox.setSelectedIndex(0);
	}
	
	@Override
	public void componentShown(ComponentEvent e) {
		if (((Component) e.getSource()).getName() == "manageUserPanel") {
			initializeComboBox();
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
			attemptUserCreation();
		}
		
	}
}
