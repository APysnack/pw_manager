package views;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import java.awt.GridLayout;
import java.awt.Insets;
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
	GridBagConstraints gr;
	LogoPanel logoPanel;

	public ErrorPanel() {

	}

	public ErrorPanel(Controller ctrl, CardLayout cl, JPanel scrnMgr, DbConnection conn) {
		this.conn = conn;
		this.cl = cl;
		this.scrnMgr = scrnMgr;
		this.ctrl = ctrl;
		u = new Utils();
		
		setLayout(new GridBagLayout());
		gr = new GridBagConstraints();
		logoPanel = new LogoPanel("User Management", 240);

		JLabel errorLbl = new JLabel(
				"There was an error with your request. Please ensure that you've entered valid input and try again.");
		JLabel errorLbl2 = new JLabel("Mobile number must be valid with area code. Passwords must be between 8 - 128 characters long");
		
		backButton = new JButton("Back");
		add(backButton);
		add(errorLbl);
		backButton.addActionListener(this);
		
		
		gr.gridx = 1;
		gr.gridy = 1;
		gr.insets = new Insets(0, 0, 10, 0);
		add(errorLbl, gr);
		gr.gridx = 1;
		gr.gridy = 2;
		gr.insets = new Insets(0, 0, 30, 0);
		add(errorLbl2, gr);
		gr.gridx = 1;
		gr.gridy = 3;
		gr.insets = new Insets(0, 0, 0, 0);
		add(logoPanel, gr);
		gr.gridx = 1;
		gr.gridy = 4;
		gr.insets = new Insets(0, 0, 0, 0);
		add(backButton, gr);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == backButton) {
			AppWindow window = (AppWindow) SwingUtilities.getWindowAncestor(this);
			window.rebuildApp();
		}
	}
}