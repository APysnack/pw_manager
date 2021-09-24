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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.awt.Cursor;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
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
import java.awt.Image;
import java.awt.BorderLayout;

import controller.Controller;
import model.DbConnection;
import structures.User;

public class MngUserPanel extends JPanel implements ActionListener, FocusListener, ComponentListener, KeyListener, MouseListener {

	CardLayout cl;
	Controller ctrl;
	Utils u;
	DbConnection conn;
	
	List<JCheckBox> checkBoxes;
	JComboBox<String> userComboBox;
	JTextField usrField;
	JTextField randomPWField;
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
	JLabel clipboardLbl;
	JButton addUserBtn;
	JButton backButton;
	JButton createUsrBtn;
	JButton editUsrBtn;
	JButton deleteUsrBtn;
	JButton generatePWBtn;
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
	
	public JLabel generateImage(String imgPath, int width, int height) {
		ImageIcon imgIcon = new ImageIcon(imgPath);
		Image image = imgIcon.getImage();
		Image scaledImage = image.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
		imgIcon = new ImageIcon(scaledImage);
		JLabel imgLabel = new JLabel(imgIcon);
		return imgLabel;
	}
	
	public void initializeMngUserPanel() {
		String clipboard = "images/clipboard.png";
		clipboardLbl = generateImage(clipboard, 25, 25);
		clipboardLbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		clipboardLbl.setToolTipText("Click the clipboard to copy the randomly generated password");
		
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
		randomPWField = new JTextField("", 15);
		pwLbl = new JLabel("Edit Password");
		pwField = new JPasswordField("Enter Password", 15);
		confirmPWLbl = new JLabel("Confirm Password");
		confirmPWField = new JPasswordField("Confirm Password", 15);
		backButton = new JButton("Back");
		createUsrBtn = new JButton("Create User");
		editUsrBtn = new JButton("Apply Changes To User");
		deleteUsrBtn = new JButton("Delete User");
		generatePWBtn = new JButton("Generate Random Password");
		
		checkBoxes = new ArrayList<JCheckBox>();
		checkBoxes.add(addPermission);
		checkBoxes.add(editPermission);
		checkBoxes.add(deletePermission);
		
		flashLbl.setVisible(false);
		randomPWField.setEditable(false);

		usrField.addFocusListener(this);
		pwField.addFocusListener(this);
		confirmPWField.addFocusListener(this);
		userComboBox.addActionListener(this);
		addUserBtn.addActionListener(this);
		createUsrBtn.addActionListener(this);
		editUsrBtn.addActionListener(this);
		backButton.addActionListener(this);
		deleteUsrBtn.addActionListener(this);
		generatePWBtn.addActionListener(this);
		clipboardLbl.addMouseListener(this);
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
		add(randomPWField);
		add(clipboardLbl);
		add(generatePWBtn);
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
				usrField.setText("");
				pwField.setText("");
				randomPWField.setText("");
				confirmPWField.setText("");
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
			int numUsers = conn.getRowCountFromTable("users");
			if (numUsers < 2){
				JOptionPane.showMessageDialog(null, "There must be at least one user in the database");
			}
			else {
				String name = (String) userComboBox.getSelectedItem();
				int response = JOptionPane.showConfirmDialog(null, "This will delete the user " + name + " and all of their passwords. Do you wish to continue?","WARNING!!!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
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
		else if (source == generatePWBtn) {
			String randomPW = ctrl.generateRandomPassword();
			randomPWField.setText(randomPW);
		}
		
		else if (source == editUsrBtn) {
			String name = (String) userComboBox.getSelectedItem();
			int response = JOptionPane.showConfirmDialog(null, "You're about to make changes to " + name + ". Do you wish to continue?","WARNING!!!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if(response == 0) {
				boolean userDeleted = ctrl.deleteUser(name);
				if (userDeleted == true) {
					flashLbl.setText("User successfully modified!");
					flashLbl.setVisible(true);
				}
				else {
					flashLbl.setText("ERROR: User could not be modified");
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

	@Override
	public void mouseClicked(MouseEvent e) {
		Object source = e.getSource();
		if (source == clipboardLbl) {
			String randomPW = randomPWField.getText();
			StringSelection stringSel = new StringSelection(randomPW);
			clipboardLbl.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			try {
				TimeUnit.MILLISECONDS.sleep(400);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(randomPW.equals("")) {
				JOptionPane.showMessageDialog(null, "Please generate a password first");
			}
			clipboardLbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(stringSel, null);
		}
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
