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
import java.awt.Insets;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
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
	JComboBox<Integer> randGenLength;
	JCheckBox addPermission;
	JCheckBox editPermission;
	JCheckBox deletePermission;
	JCheckBox randGenUpper;
	JCheckBox randGenLower;
	JCheckBox randGenNumber;
	JCheckBox randGenSymbol;
	JTextField usrField;
	JTextField randomPWField;
	JPasswordField pwField;
	JPasswordField confirmPWField;
	JPanel randGenChkPnl;
	JPanel scrnMgr;
	JPanel checkBoxPnl;
	JLabel flashLbl;
	JLabel titleLbl;
	JLabel lengthLbl;
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
	GridBagConstraints gr;
	LogoPanel logoPanel;
	
	public MngUserPanel() {

	}

	public MngUserPanel(Controller ctrl, CardLayout cl, JPanel scrnMgr, DbConnection conn) {
		setName("manageUserPanel");
		this.conn = conn;
		this.cl = cl;
		this.scrnMgr = scrnMgr;
		this.ctrl = ctrl;
		u = new Utils();
		initializeMngUserPanel();
	}
	
	public void initializeMngUserPanel() {
		String clipboard = "images/clipboard.png";
		clipboardLbl = u.generateImage(clipboard, 25, 25);
		clipboardLbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		clipboardLbl.setToolTipText("Click the clipboard to copy the randomly generated password");
		
		onAddUserPage = false;
		titleLbl = new JLabel("Manage Users");
		userComboBox = new JComboBox<String>();
		addUserBtn = new JButton("Add New User");

		addPermission = new JCheckBox("Add Users");
		editPermission = new JCheckBox("Edit Users");
		deletePermission = new JCheckBox("Delete Users");
		
		Border checkBoxBorder = BorderFactory.createTitledBorder("User Privileges");
		checkBoxPnl = new JPanel();
		checkBoxPnl.add(addPermission);
		checkBoxPnl.add(editPermission);
		checkBoxPnl.add(deletePermission);
		checkBoxPnl.setBorder(checkBoxBorder);
		
		randGenLower = new JCheckBox("a-z");
		randGenUpper = new JCheckBox("A-Z");
		randGenNumber = new JCheckBox("0-9");
		randGenSymbol = new JCheckBox("@!%$...");
		lengthLbl = new JLabel("Length");
		randGenLength = new JComboBox<Integer>();
		
		int minPasswordLen = 12;
		int maxPasswordLen = 128;
		int k = minPasswordLen;
		
		for(int i = 0; i <= (maxPasswordLen - minPasswordLen); i++) {
			randGenLength.insertItemAt(k, i);
			k++;
		}
		
		randGenLength.setSelectedIndex(4);
		
		
		Border rgBorder = BorderFactory.createTitledBorder("Randomization Settings");
		randGenChkPnl = new JPanel();
		randGenChkPnl.add(randGenLower);
		randGenChkPnl.add(randGenUpper);
		randGenChkPnl.add(randGenNumber);
		randGenChkPnl.add(randGenSymbol);
		randGenChkPnl.add(lengthLbl);
		randGenChkPnl.add(randGenLength);
		randGenChkPnl.setBorder(rgBorder);

		flashLbl = new JLabel("");
		unameLbl = new JLabel("Edit Username");
		usrField = new JTextField("Enter Username", 15);
		randomPWField = new JTextField("", 15);
		pwLbl = new JLabel("Edit Password");
		pwField = new JPasswordField("Enter Password", 15);
		confirmPWLbl = new JLabel("Confirm New Password");
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
		
		setLayout(new GridBagLayout());
		gr = new GridBagConstraints();
		logoPanel = new LogoPanel("User Management", 240);
		
		updateLayout("main");

	}

	public void updateLayout(String layoutName) {
		JPanel userPanel = new JPanel();
		JPanel pwPanel = new JPanel();
		JPanel confirmPWPanel = new JPanel();
		JPanel randomGenPanel = new JPanel();
		JPanel actionPanel = new JPanel();
		
		randomGenPanel.add(randomPWField);
		randomGenPanel.add(clipboardLbl);
		randomGenPanel.add(generatePWBtn);
		
		userPanel.add(userComboBox);
		userPanel.add(unameLbl);
		userPanel.add(usrField);
		
		pwPanel.add(pwLbl);
		pwPanel.add(pwField);
		
		confirmPWPanel.add(confirmPWLbl);
		confirmPWPanel.add(confirmPWField);
		
		if(layoutName == "main") {
			actionPanel.add(addUserBtn);
			actionPanel.add(editUsrBtn);
			actionPanel.add(deleteUsrBtn);
		}
		else {
			actionPanel.add(createUsrBtn);
		}
		
		actionPanel.add(backButton);
		
		gr.gridx = 1;
		gr.gridy = 1;
		gr.insets = new Insets(10,0,0,0);
		add(flashLbl, gr);
		gr.gridx = 1;
		gr.gridy = 2;
		gr.insets = new Insets(0,0,0,0);
		add(logoPanel, gr);
		gr.gridx = 1;
		gr.gridy = 3;
		gr.insets = new Insets(0,0,10,0);
		add(randGenChkPnl, gr);
		gr.gridx = 1;
		gr.gridy = 4;
		gr.insets = new Insets(0,0,10,0);
		add(randomGenPanel, gr);
		gr.gridx = 1;
		gr.gridy = 5;
		gr.insets = new Insets(0,0,10,0);
		add(userPanel, gr);
		gr.gridx = 1;
		gr.gridy = 6;
		gr.insets = new Insets(0,0,10,0);
		add(pwPanel, gr);
		gr.gridx = 1;
		gr.gridy = 7;
		gr.insets = new Insets(0,0,10,0);
		add(confirmPWPanel, gr);
		gr.gridx = 1;
		gr.gridy = 8;
		gr.insets = new Insets(0,0,10,0);
		add(checkBoxPnl, gr);
		gr.gridx = 1;
		gr.gridy = 9;
		gr.insets = new Insets(0,0,10,0);
		add(actionPanel, gr);
		
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
				flashLbl.setText("User could not be added. Please check your input. Note you cannot create users with permissions you do not have");
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
			User user = conn.getCurrentUser();
			if(user.getAddPermission() == true) {
				onAddUserPage = true;
				editUsrBtn.setVisible(false);
				deleteUsrBtn.setVisible(false);
				usrField.setText("");
				pwField.setText("");
				confirmPWField.setText("");
				addUserBtn.setVisible(false);
				userComboBox.setVisible(false);
				for(int i = 0; i < checkBoxes.size(); i++) {
					checkBoxes.get(i).setSelected(false);
				}
				updateLayout("addUser");
			}
			else {
				JOptionPane.showMessageDialog(null, "This user does not have permission to add users to the database");
			}

			
		} else if (source == backButton) {
			removeAll();
			repaint();
			revalidate();
			if(onAddUserPage == false) {
				initializeMngUserPanel();
				cl.show(scrnMgr, "Home");
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
			
			User user = conn.getCurrentUser();
			if(user.getDeletePermission() == true) {
				int numUsers = conn.getRowCountFromTable("users");
				if (numUsers < 2){
					JOptionPane.showMessageDialog(null, "User cannot be deleted. There must be at least one user with add permissions in the database");
				}
				else {
					String name = (String) userComboBox.getSelectedItem();
					int response = JOptionPane.showConfirmDialog(null, "This will delete the user " + name + " and all of their passwords. Do you wish to continue?","WARNING!!!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					if(response == 0) {
						boolean userDeleted = ctrl.deleteUser(name);
						if (userDeleted == true) {
							flashLbl.setText("User successfully deleted!");
							flashLbl.setVisible(true);
							initializeComboBox();
							updateLayout("main");
						}
						else {
							flashLbl.setText("ERROR: User could not be deleted");
							flashLbl.setVisible(false);
						}
					}
				}
			}
			else {
				JOptionPane.showMessageDialog(null, "This user does not have permission to delete users from the database");
			}
			
		}
		else if (source == generatePWBtn) {
			boolean lowercase = randGenLower.isSelected();
			boolean uppercase = randGenUpper.isSelected();
			boolean numeric = randGenNumber.isSelected();
			boolean symbols = randGenSymbol.isSelected();
			int length = (int) randGenLength.getSelectedItem();
			if(lowercase || uppercase || numeric || symbols) {
				String randomPW = ctrl.generateRandomPassword(lowercase, uppercase, numeric, symbols, length);
				randomPWField.setText(randomPW);
			}
			else {
				JOptionPane.showMessageDialog(null, "At least one checkbox must be selected for password generation");
			}

		}
		
		else if (source == editUsrBtn) {
			User user = conn.getCurrentUser();
			if(user.getEditPermission() == true) {
				String name = (String) userComboBox.getSelectedItem();
				int response = JOptionPane.showConfirmDialog(null, "You're about to make changes to " + name + ". Do you wish to continue?","WARNING!!!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if(response == 0) {
					String newUserName = usrField.getText();
					boolean canAdd = addPermission.isSelected();
					boolean canEdit = editPermission.isSelected();
					boolean canDelete = deletePermission.isSelected();
					char[] password = pwField.getPassword();
					char[] passwordConfirm = confirmPWField.getPassword();

					if (Arrays.equals(password, passwordConfirm) == true) {
						String stringPW = String.valueOf(password);
						boolean userEdited = ctrl.editUser(name, newUserName, stringPW, canAdd, canEdit, canDelete);
						if (userEdited == true) {
							flashLbl.setText("User successfully modified!");
						}
						else {
							flashLbl.setText("ERROR: User could not be modified");
						}
					}
					else {
						flashLbl.setText("The passwords you entered do not match.");
					}
					flashLbl.setVisible(true);
					initializeComboBox();
					updateLayout("main");
				}
			}
			else {
				JOptionPane.showMessageDialog(null, "This user does not have permission to modify users");
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
