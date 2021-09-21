package views;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
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

public class UserPanel extends JPanel implements ComponentListener {

	JTextField usrField;
	JPasswordField pwField;
	JButton createUserBtn;
	JPanel scrnMgr;
	CardLayout cl;
	Controller ctrl;
	Utils u;
	String userName;
	JLabel userPnlLbl;

	public UserPanel() {

	}
	
	public UserPanel(Controller ctrl, CardLayout cl, JPanel scrnMgr) {
		setName("userPanel");
		this.ctrl = ctrl;
		this.cl = cl;
		addComponentListener(this);
		userPnlLbl = new JLabel("Welcome " + userName);
		add(userPnlLbl);
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
}