package views;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

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

import java.awt.BorderLayout;

import controller.Controller;
import model.DbConnection;

public class UserPanel extends JPanel implements ComponentListener, ActionListener {

	JTextField usrField;
	JPasswordField pwField;
	JButton createUserBtn;
	JPanel scrnMgr;
	CardLayout cl;
	Controller ctrl;
	Utils u;
	String userName;
	JLabel userPnlLbl;
	JButton managePWBtn;
	JButton manageUsrBtn;
	JButton logOutButton;
	DbConnection conn;

	public UserPanel() {

	}
	
	public UserPanel(Controller ctrl, CardLayout cl, JPanel scrnMgr, DbConnection conn) {
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
		
		manageUsrBtn.addActionListener(this);
		managePWBtn.addActionListener(this);
		logOutButton.addActionListener(this);
		
		add(userPnlLbl);
		add(manageUsrBtn);
		add(managePWBtn);
		add(logOutButton);
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
			this.userName = conn.getCurrentUserName();
			userPnlLbl.setText("Welcome " + userName);
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