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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;
import java.awt.GridLayout;

import java.awt.BorderLayout;

import controller.Controller;

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

	public UserPanel() {

	}
	
	public UserPanel(Controller ctrl, CardLayout cl, JPanel scrnMgr) {
		setName("userPanel");
		this.ctrl = ctrl;
		this.cl = cl;
		this.scrnMgr = scrnMgr;
		
		addComponentListener(this);
		userPnlLbl = new JLabel("Welcome " + userName);
		
		manageUsrBtn = new JButton("Manage Users");
		managePWBtn = new JButton("Manage Passwords");
		
		manageUsrBtn.addActionListener(this);
		managePWBtn.addActionListener(this);
		
		add(userPnlLbl);
		add(manageUsrBtn);
		add(managePWBtn);
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
			this.userName = ctrl.getUserName();
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
		
	}
}