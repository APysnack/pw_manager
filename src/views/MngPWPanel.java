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
import pw_manager.Password;
import pw_manager.User;

public class MngPWPanel extends JPanel
		implements ActionListener, FocusListener, ComponentListener, KeyListener, MouseListener {

	CardLayout cl;
	Controller ctrl;
	Utils u;
	DbConnection conn;

	JComboBox<String> appComboBox;
	JTextField appField;
	JTextField randomPWField;
	JTextField displayPWField;
	JPasswordField pwField;
	JPasswordField confirmPWField;
	JPanel scrnMgr;
	JLabel flashLbl;
	JLabel titleLbl;
	JLabel appNameLbl;
	JLabel pwLbl;
	JLabel confirmPWLbl;
	JLabel actionLbl;
	JLabel clipboardLbl;
	JButton addPWBtn;
	JButton backButton;
	JButton createNewPWBtn;
	JButton editPWBtn;
	JButton deletePWBtn;
	JButton generatePWBtn;
	JButton applyChangesBtn;
	boolean onSecondaryPage;
	String userName;

	public MngPWPanel() {
	}

	public MngPWPanel(Controller ctrl, CardLayout cl, JPanel scrnMgr, DbConnection conn) {
		setName("managePWPanel");
		this.conn = conn;
		this.cl = cl;
		this.scrnMgr = scrnMgr;
		this.ctrl = ctrl;
		initializeMngPWPanel();
	}

	public void initializeMngPWPanel() {
		onSecondaryPage = false;
		String clipboard = "images/clipboard.png";
		clipboardLbl = generateImage(clipboard, 25, 25);
		clipboardLbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		clipboardLbl.setToolTipText("Click the clipboard to copy the randomly generated password");
		titleLbl = new JLabel("Showing passwords for " + userName);

		actionLbl = new JLabel("Select an application");
		appComboBox = new JComboBox<String>();
		addPWBtn = new JButton("Create New Password");
		flashLbl = new JLabel("");
		appNameLbl = new JLabel("Edit Application Name");
		appField = new JTextField("Enter App", 15);
		randomPWField = new JTextField("", 15);
		pwLbl = new JLabel("Edit Password");
		pwField = new JPasswordField("Enter Password", 15);
		confirmPWLbl = new JLabel("Confirm Password");
		confirmPWField = new JPasswordField("Enter Password", 15);
		backButton = new JButton("Back");
		createNewPWBtn = new JButton("Add This Password");
		editPWBtn = new JButton("Edit Password");
		deletePWBtn = new JButton("Delete Password");
		generatePWBtn = new JButton("Generate Random Password");
		displayPWField = new JTextField("", 15);
		applyChangesBtn = new JButton("Apply changes");

		addPWBtn.addActionListener(this);
		appField.addFocusListener(this);
		pwField.addFocusListener(this);
		confirmPWField.addFocusListener(this);
		appComboBox.addActionListener(this);
		addPWBtn.addActionListener(this);
		createNewPWBtn.addActionListener(this);
		editPWBtn.addActionListener(this);
		backButton.addActionListener(this);
		deletePWBtn.addActionListener(this);
		generatePWBtn.addActionListener(this);
		clipboardLbl.addMouseListener(this);
		applyChangesBtn.addActionListener(this);
		addComponentListener(this);

		add(flashLbl);
		add(titleLbl);
		add(actionLbl);
		add(appComboBox);
		add(displayPWField);
		add(appNameLbl);
		add(appField);
		add(pwLbl);
		add(pwField);
		add(confirmPWLbl);
		add(confirmPWField);
		add(randomPWField);
		add(clipboardLbl);
		add(generatePWBtn);
		add(createNewPWBtn);
		add(editPWBtn);
		add(addPWBtn);
		add(applyChangesBtn);
		add(deletePWBtn);
		add(backButton);
	}

	public void mainView() {
		flashLbl.setVisible(false);
		onSecondaryPage = false;
		titleLbl.setText("Displaying passwords for " + userName);
		displayPWField.setVisible(true);
		appComboBox.setVisible(true);
		addPWBtn.setVisible(true);
		editPWBtn.setVisible(true);
		deletePWBtn.setVisible(true);
		pwLbl.setVisible(false);
		pwField.setVisible(false);
		confirmPWLbl.setVisible(false);
		confirmPWField.setVisible(false);
		randomPWField.setVisible(false);
		generatePWBtn.setVisible(false);
		clipboardLbl.setVisible(true);
		appNameLbl.setVisible(false);
		appField.setVisible(false);
		createNewPWBtn.setVisible(false);
		applyChangesBtn.setVisible(false);
	}

	public void noPasswordsView() {
		flashLbl.setVisible(false);
		onSecondaryPage = false;
		displayPWField.setVisible(false);
		editPWBtn.setVisible(false);
		deletePWBtn.setVisible(false);
		pwLbl.setVisible(false);
		pwField.setVisible(false);
		confirmPWLbl.setVisible(false);
		confirmPWField.setVisible(false);
		randomPWField.setVisible(false);
		generatePWBtn.setVisible(false);
		clipboardLbl.setVisible(false);
		appNameLbl.setVisible(false);
		appField.setVisible(false);
		createNewPWBtn.setVisible(false);
		applyChangesBtn.setVisible(false);
	}

	// foo
	public void editPWView(String appName) {
		flashLbl.setVisible(false);
		onSecondaryPage = true;
		populatePasswordData(appName);
		actionLbl.setVisible(false);
		addPWBtn.setVisible(false);
		createNewPWBtn.setVisible(false);
		displayPWField.setVisible(false);
		flashLbl.setVisible(false);
		editPWBtn.setVisible(false);
		applyChangesBtn.setVisible(true);
		deletePWBtn.setVisible(false);
		pwLbl.setVisible(true);
		pwField.setVisible(true);
		confirmPWLbl.setVisible(true);
		confirmPWField.setVisible(true);
		randomPWField.setVisible(true);
		generatePWBtn.setVisible(true);
		clipboardLbl.setVisible(true);
		appNameLbl.setVisible(true);
		appField.setVisible(true);
		appComboBox.setVisible(false);
		titleLbl.setVisible(false);
	}

	public void addPWView() {
		flashLbl.setVisible(false);
		onSecondaryPage = true;
		addPWBtn.setVisible(false);
		createNewPWBtn.setVisible(true);
		displayPWField.setVisible(false);
		actionLbl.setText("Creating a new password for " + userName);
		appField.setText("Application Name");
		flashLbl.setVisible(false);
		editPWBtn.setVisible(false);
		deletePWBtn.setVisible(false);
		pwLbl.setVisible(true);
		pwField.setVisible(true);
		confirmPWLbl.setVisible(true);
		applyChangesBtn.setVisible(false);
		confirmPWField.setVisible(true);
		randomPWField.setVisible(true);
		generatePWBtn.setVisible(true);
		clipboardLbl.setVisible(true);
		appNameLbl.setVisible(true);
		appField.setVisible(true);
		appComboBox.setVisible(false);
		titleLbl.setVisible(false);
	}

	public void initializeComboBox() {
		appComboBox.removeAllItems();
		ArrayList<String> allApps = conn.getAllUserApps();
		if (allApps.size() > 0) {
			for (int i = 0; i < allApps.size(); i++) {
				appComboBox.insertItemAt(allApps.get(i), i);
			}
			appComboBox.setSelectedIndex(0);
			mainView();
		} else {
			flashLbl.setText("No passwords for this user. Please add one");
			flashLbl.setVisible(true);
			noPasswordsView();
		}
	}

	public JLabel generateImage(String imgPath, int width, int height) {
		ImageIcon imgIcon = new ImageIcon(imgPath);
		Image image = imgIcon.getImage();
		Image scaledImage = image.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
		imgIcon = new ImageIcon(scaledImage);
		JLabel imgLabel = new JLabel(imgIcon);
		return imgLabel;
	}

	public void populatePasswordData(String selectedApp) {
		if (onSecondaryPage == false) {
			displayPWField.setText(ctrl.getDecryptedPassword(selectedApp));
		} else {
			Password pwData = ctrl.getPasswordInfo(selectedApp);
			appField.setText(pwData.getAppName());
			int pwLen = pwData.getPasswordLength();
			String pwStr = "";
			for (int i = 0; i < pwLen; i++) {
				pwStr += "*";
			}
			pwField.setText(pwStr);
			confirmPWField.setText(pwStr);
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

	public void backBtnBehavior() {
		if (onSecondaryPage == true) {
			mainView();
		} else {
			cl.show(scrnMgr, "User");
		}
	}

	@Override
	public void componentShown(ComponentEvent e) {
		if (((Component) e.getSource()).getName() == "managePWPanel") {
			this.userName = conn.getCurrentUserName();
			titleLbl.setText("Displaying passwords for " + userName);
			initializeComboBox();
		}

	}

	@Override
	public void focusGained(FocusEvent e) {
		Object source = e.getSource();
		if (source == appField) {
			if (appField.getText().equals("Application Name")) {
				appField.setText("");
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
		if (source == appField) {
			if (appField.getText().isEmpty()) {
				appField.setText("Application Name");
			}
		} else if (source == pwField) {
			passwordFieldLoseFocus(pwField);
		} else if (source == confirmPWField) {
			passwordFieldLoseFocus(confirmPWField);
		}

	}

	public void attemptAddPassword() {
		String appName = appField.getText();
		char[] password = pwField.getPassword();
		char[] passwordConfirm = confirmPWField.getPassword();

		if (Arrays.equals(password, passwordConfirm) == true) {
			String stringPW = String.valueOf(password);
			boolean addSuccessful = ctrl.addNewPassword(appName, stringPW);
			if (addSuccessful) {
				flashLbl.setText("Password successfully added to database!");
				appField.setText("Application Name");
				pwField.setText("Enter Password");
				confirmPWField.setText("Enter Password");
			} else {
				flashLbl.setText("Error in user input, password could not be added");
			}
		} else {
			flashLbl.setText("Passwords do not match, please try again");
		}

		flashLbl.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == addPWBtn) {
			addPWView();
		} else if (source == generatePWBtn) {
			String randomPW = ctrl.generateRandomPassword();
			randomPWField.setText(randomPW);
		} else if (source == backButton) {
			backBtnBehavior();
		} else if (source == createNewPWBtn) {
			attemptAddPassword();

		} else if (source == appComboBox) {
			String selectedApp = (String) appComboBox.getSelectedItem();
			populatePasswordData(selectedApp);
		} else if (source == deletePWBtn) {
			String appName = (String) appComboBox.getSelectedItem();
			JFrame alert = new JFrame();
			int response = JOptionPane.showConfirmDialog(alert,
					"This will delete the password for " + appName + ". Do you still wish to continue?", "WARNING!!!",
					JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if (response == 0) {
				boolean pwDeleted = ctrl.deletePassword(appName);
				if (pwDeleted == true) {
					flashLbl.setText("Password successfully deleted!");
					flashLbl.setVisible(true);
				} else {
					flashLbl.setText("ERROR: Password could not be deleted");
					flashLbl.setVisible(false);
				}
			}
		} else if (source == editPWBtn) {
			String selectedApp = (String) appComboBox.getSelectedItem();
			editPWView(selectedApp);
		}

		else if (source == applyChangesBtn) {
			String name = (String) appComboBox.getSelectedItem();

			int response = JOptionPane.showConfirmDialog(null,
					"You're about to make changes to " + name + ". Do you wish to continue?", "WARNING!!!",
					JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if (response == 0) {
				String newAppName = appField.getText();
				char[] password = pwField.getPassword();
				char[] passwordConfirm = confirmPWField.getPassword();

				if (Arrays.equals(password, passwordConfirm) == true) {
					String stringPW = String.valueOf(password);
					boolean addSuccessful = ctrl.editPassword(name, newAppName, stringPW);
					if (addSuccessful) {
						flashLbl.setText("Password successfully modified!");
						appField.setText("Application Name");
						pwField.setText("Enter Password");
						confirmPWField.setText("Enter Password");
					} else {
						flashLbl.setText("Error in user input, password could not be added");
					}
				}
				
				flashLbl.setVisible(true);

			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Object source = e.getSource();
		if (source == clipboardLbl) {
			String textToCopy = "";

			if (onSecondaryPage == true) {
				textToCopy = randomPWField.getText();
			} else {
				textToCopy = displayPWField.getText();
			}
			StringSelection stringSel = new StringSelection(textToCopy);
			clipboardLbl.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			try {
				TimeUnit.MILLISECONDS.sleep(400);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (textToCopy.equals("")) {
				String message = "";
				if (onSecondaryPage == true) {
					message = "Please generate a password first";
				} else {
					message = "This field is empty";
				}
				JOptionPane.showMessageDialog(null, message);
			}
			clipboardLbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(stringSel, null);
		}

	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

	}

	@Override
	public void componentResized(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

}
