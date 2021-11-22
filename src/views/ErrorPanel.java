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
	JButton backButton;
	JPanel scrnMgr;
	CardLayout cl;
	Controller ctrl;
	Utils u;
	DbConnection conn;

	public ErrorPanel() {

	}

	public ErrorPanel(Controller ctrl, CardLayout cl, JPanel scrnMgr, DbConnection conn) {
		this.conn = conn;
		this.cl = cl;
		this.scrnMgr = scrnMgr;
		this.ctrl = ctrl;
		u = new Utils();

		JLabel errorLbl = new JLabel(
				"There was an error with your request. Please try again.");
		backButton = new JButton("Back");
		add(backButton);
		add(errorLbl);
		backButton.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == backButton) {
			cl.show(scrnMgr, "Login");
		}
	}
}