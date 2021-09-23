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

public class MngPWPanel extends JPanel implements ActionListener, FocusListener, ComponentListener, KeyListener, MouseListener {

	CardLayout cl;
	Controller ctrl;
	Utils u;
	DbConnection conn;
	
	JComboBox<String> appComboBox;
	JTextField appField;
	JTextField randomPWField;
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
	JButton addApplicationBtn;
	JButton backButton;
	JButton createNewPWBtn;
	JButton editPWBtn;
	JButton deletePWBtn;
	JButton generatePWBtn;
	boolean onAddPasswordPage;
	
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
	
	public JLabel generateImage(String imgPath, int width, int height) {
		ImageIcon imgIcon = new ImageIcon(imgPath);
		Image image = imgIcon.getImage();
		Image scaledImage = image.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
		imgIcon = new ImageIcon(scaledImage);
		JLabel imgLabel = new JLabel(imgIcon);
		return imgLabel;
	}
	
	public void initializeMngPWPanel() {
		String clipboard = "images/clipboard.png";
		clipboardLbl = generateImage(clipboard, 25, 25);
		clipboardLbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		clipboardLbl.setToolTipText("Click the clipboard to copy the randomly generated password");
		
		onAddPasswordPage = false;
		titleLbl = new JLabel("Manage Applications");
		actionLbl = new JLabel("Select an application");
		appComboBox = new JComboBox<String>();
		addApplicationBtn = new JButton("Create New Password");

		flashLbl = new JLabel("");
		appNameLbl = new JLabel("Edit Application Name");
		appField = new JTextField("Enter App", 15);
		randomPWField = new JTextField("", 15);
		pwLbl = new JLabel("Edit Password");
		pwField = new JPasswordField("Enter Password", 15);
		confirmPWLbl = new JLabel("Confirm Password");
		confirmPWField = new JPasswordField("Confirm Password", 15);
		backButton = new JButton("Back");
		createNewPWBtn = new JButton("Create New Password");
		editPWBtn = new JButton("Apply Changes To Password");
		deletePWBtn = new JButton("Delete Password");
		generatePWBtn = new JButton("Generate Random Password");
		
		flashLbl.setVisible(false);
		randomPWField.setEditable(false);

		appField.addFocusListener(this);
		pwField.addFocusListener(this);
		confirmPWField.addFocusListener(this);
		appComboBox.addActionListener(this);
		addApplicationBtn.addActionListener(this);
		createNewPWBtn.addActionListener(this);
		editPWBtn.addActionListener(this);
		backButton.addActionListener(this);
		deletePWBtn.addActionListener(this);
		generatePWBtn.addActionListener(this);
		clipboardLbl.addMouseListener(this);
		addComponentListener(this);
		
		add(flashLbl);
		add(titleLbl);
		add(actionLbl);
		add(appComboBox);
		add(appNameLbl);
		add(appField);
		add(pwLbl);
		add(pwField);
		add(confirmPWLbl);
		add(confirmPWField);
		add(randomPWField);
		add(clipboardLbl);
		add(generatePWBtn);
		add(editPWBtn);
		add(addApplicationBtn);
		add(deletePWBtn);
		add(backButton);
	}

	public void attemptPWCreation() {
		//TODO
	}
	
	public void populatePasswordData() {
		String selectedApp = (String) appComboBox.getSelectedItem();
		Password pw = ctrl.getPasswordInfo(selectedApp);
		appField.setText(pw.getAppName());
		int pwLen = pw.getPasswordLength();
		String pwStr = "";
		for (int i = 0; i < pwLen; i++) {
			pwStr += "*";
		}
		pwField.setText(pwStr);
		confirmPWField.setText(pwStr);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == appComboBox) {
			populatePasswordData();
		} else if (source == addApplicationBtn) {
			onAddPasswordPage = true;
			editPWBtn.setVisible(false);
			deletePWBtn.setVisible(false);
			appField.setText("");
			pwField.setText("");
			confirmPWField.setText("");
			actionLbl.setText("Creating new password");
			addApplicationBtn.setVisible(false);
			appComboBox.setVisible(false);

			add(createNewPWBtn);
			
		} else if (source == backButton) {
			removeAll();
			repaint();
			revalidate();
			if(onAddPasswordPage == false) {
				initializeMngPWPanel();
				cl.show(scrnMgr, "User");
			}
			else {
				initializeMngPWPanel();
				initializeComboBox();
				cl.show(scrnMgr, "ManagePasswords");
			}
		}
		else if (source == createNewPWBtn) {
			attemptPWCreation();
		}
		else if(source == deletePWBtn) {
			String appName = (String) appComboBox.getSelectedItem();
			JFrame alert = new JFrame();
			int response = JOptionPane.showConfirmDialog(alert, "This will delete the password for " + appName + ". Do you still wish to continue?","WARNING!!!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if(response == 0) {
				boolean pwDeleted = ctrl.deletePassword(appName);
				if (pwDeleted == true) {
					flashLbl.setText("Password successfully deleted!");
					flashLbl.setVisible(true);
				}
				else {
					flashLbl.setText("ERROR: Password could not be deleted");
					flashLbl.setVisible(false);
				}
			}
		}
		else if (source == generatePWBtn) {
			String randomPW = ctrl.generateRandomPassword();
			randomPWField.setText(randomPW);
		}
		
		else if (source == editPWBtn) {
			String name = (String) appComboBox.getSelectedItem();
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
		if (source == appField) {
			if (appField.getText().equals("Enter Username")) {
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
				appField.setText("Enter Username");
			}
		} else if (source == pwField) {
			passwordFieldLoseFocus(pwField);
		}
		else if (source == confirmPWField) {
			passwordFieldLoseFocus(confirmPWField);
		}
	}

	public void initializeComboBox() {
		appComboBox.removeAllItems();
		ArrayList<String> allApps = conn.getAllUserApps();
		
		for (int i = 0; i < allApps.size(); i++) {
			appComboBox.insertItemAt(allApps.get(i), i);
		}
		appComboBox.setSelectedIndex(0);
	}
	
	@Override
	public void componentShown(ComponentEvent e) {
		if (((Component) e.getSource()).getName() == "managePWPanel") {
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
			attemptPWCreation();
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
