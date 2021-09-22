package views;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import model.DbConnection;

public class MngPWPanel extends JPanel implements ActionListener {

	JTextField usrField;
	JPasswordField pwField;
	JPanel scrnMgr;
	CardLayout cl;
	Controller ctrl;
	Utils u;
	DbConnection conn;
	JButton backButton;

	public MngPWPanel() {

	}
	
	public MngPWPanel(Controller ctrl, CardLayout cl, JPanel scrnMgr, DbConnection conn) {
		this.cl = cl;
		this.conn = conn;
		this.scrnMgr = scrnMgr;
		
		JLabel errorLbl = new JLabel("Manage Passwords");
		add(errorLbl);
		
		backButton = new JButton("Back");
		backButton.addActionListener(this);
		
		add(backButton);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == backButton) {
			cl.show(scrnMgr, "User");
		}
		
	}
}
