package views;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.BorderLayout;

import controller.Controller;
import model.DbConnection;
import structures.User;

public class HomePanel extends JPanel implements ComponentListener, ActionListener {

	JTextField usrField;
	JPasswordField pwField;
	JButton createUserBtn;
	JPanel scrnMgr;
	JPanel optionsPanel;
	CardLayout cl;
	Controller ctrl;
	Utils u;
	String userName;
	JLabel userPnlLbl;
	JButton managePWBtn;
	JButton manageUsrBtn;
	JButton logOutButton;
	DbConnection conn;
	JLabel logoLbl;
	JLabel logoTxt;
	JLabel subLbl;
	LogoPanel logoPanel;
	
	public HomePanel() {

	}
	
	public HomePanel(Controller ctrl, CardLayout cl, JPanel scrnMgr, DbConnection conn) {
		setName("userPanel");
		this.ctrl = ctrl;
		this.cl = cl;
		this.scrnMgr = scrnMgr;
		this.conn = conn;
		addComponentListener(this);
		userPnlLbl = new JLabel("Welcome " + userName);
		logOutButton = new JButton("Log Out");
		manageUsrBtn = new JButton("Manage Users");
		managePWBtn = new JButton("Manage Passwords");
		logoPanel = new LogoPanel("ADMINISTRATIVE DASHBOARD", 170);
		managePWBtn.addActionListener(this);
		logOutButton.addActionListener(this);
		homePanelLayout();
	}

	public void homePanelLayout() {
		setLayout(new GridBagLayout());
		GridBagConstraints grid = new GridBagConstraints();
		
		grid.gridx = 3;
		grid.gridy = 1;
		add(logoPanel, grid);

		
		grid.insets = new Insets(0,0,20,0);
		grid.gridx = 3;
		grid.gridy = 3;
		add(userPnlLbl, grid);

		grid.insets = new Insets(0,0,10,0);
		grid.gridx = 3;
		grid.gridy = 4;
		add(managePWBtn, grid);
		
		grid.insets = new Insets(0,0,70,0);
		grid.gridx = 3;
		grid.gridy = 5;
		add(manageUsrBtn, grid);
		
		grid.gridx = 3;
		grid.gridy = 6;
		add(logOutButton, grid);
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
		if(((Component) e.getSource()).getName() == "userPanel") {
			User currentUser = conn.getCurrentUser();
			this.userName = currentUser.getUsername();
			userPnlLbl.setText("Welcome " + userName);
			boolean userIsAdmin = false;
			ArrayList<Boolean> userPermissions = currentUser.getUserPermissions();
			if(userPermissions.size() > 0) {
				for(int i = 0; i < userPermissions.size(); i++) {
					if(userPermissions.get(i) == true) {
						userIsAdmin = true;
						break;
					}
				}
				if(userIsAdmin == true) {
					manageUsrBtn.setVisible(true);
					manageUsrBtn.addActionListener(this);
				}
				else {
						manageUsrBtn.setVisible(false);
				}
			}
		}
	}

	@Override
	public void componentHidden(ComponentEvent e) {
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == manageUsrBtn) {
			cl.show(scrnMgr, "ManageUsers");
		}
		else if(e.getSource() == managePWBtn) {
			cl.show(scrnMgr, "ManagePasswords");
		}
		else if(e.getSource() == logOutButton) {
			AppWindow window = (AppWindow) SwingUtilities.getWindowAncestor(this);
			window.rebuildApp();
		}
	}
}