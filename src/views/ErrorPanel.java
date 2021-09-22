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

public class ErrorPanel extends JPanel implements ActionListener {

	JTextField usrField;
	JPasswordField pwField;
	JButton createUserBtn;
	JPanel scrnMgr;
	CardLayout cl;
	Controller ctrl;
	Utils u;

	public ErrorPanel() {

	}
	
	public ErrorPanel(Controller ctrl, CardLayout cl, JPanel scrnMgr, DbConnection conn) {
		JLabel errorLbl = new JLabel("There was an error with your request. Please close out the application and try again.");
		add(errorLbl);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}