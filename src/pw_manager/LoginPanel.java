package pw_manager;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel implements FocusListener, ActionListener {
	
	JTextField usrField;
	JPasswordField pwField;
	JButton loginBtn;
	JPanel scrnMgr;
	CardLayout cl;
	Controller ctrl;
	
	
	LoginPanel(Controller ctrl, CardLayout cl, JPanel scrnMgr){
		
		setLayout(new GridLayout(4, 1, 2, 2));
		
		JButton lgnBtn = new JButton("Log In");
		JLabel lgnLbl = new JLabel("Please Enter your Login Information");
		JTextField usrField = new JTextField("Enter Username", 15);
		JPasswordField pwField = new JPasswordField("Enter Password", 15);
		JPanel ctrLblPnl = new JPanel();
		
		this.ctrl = ctrl;
		this.scrnMgr = scrnMgr;
		this.cl = cl;
		this.loginBtn = lgnBtn;
		this.usrField = usrField;
		this.pwField = pwField;
		
		add(lgnBtn);
		ctrLblPnl.add(lgnLbl);
		add(ctrLblPnl);
		add(usrField);
		add(pwField);
		add(lgnBtn);
		
		usrField.addFocusListener(this);
		pwField.addFocusListener(this);
		lgnBtn.addActionListener(this);
	}
	
	@Override
	public void focusGained(FocusEvent e) {
		Object source = e.getSource();
		if(source == usrField) {
			if (usrField.getText().equals("Enter Username")) {
				usrField.setText("");
			}
		}
		
		else if(source == pwField) {
			pwField.setEchoChar('*');
			if (String.valueOf(pwField.getPassword()).equals("Enter Password")) {
				pwField.setText("");
			}
		}
	}

	@Override
	public void focusLost(FocusEvent e) {
		Object source = e.getSource();
		if(source == usrField) {
			if (usrField.getText().isEmpty()) {
				usrField.setText("Enter Username");
			}
		}
		else if(source == pwField) {
			if (String.valueOf(pwField.getPassword()).equals("")) {
				pwField.setText("Enter Password");
				pwField.setEchoChar((char) 0);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if(source == loginBtn) {
			String userName = usrField.getText();
			String password = String.valueOf(pwField.getPassword());
			
			if (ctrl.authenticateUser(userName, password) == true) {
				cl.show(scrnMgr, "Assignments");
			}
			
		}
	}
	
}
