package views;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import controller.Controller;
import model.DbConnection;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.CardLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;


// initial login page
public class LoginPanel extends JPanel implements FocusListener, ActionListener, KeyListener {

    final JPanel scrnMgr;
    final CardLayout cl;
    final Controller ctrl;
    final Utils u;
    final DbConnection conn;
    final JLabel flashLbl;
    final JLabel usrLbl;
    final JLabel pwLbl;
    final JTextField usrField;
    final JPasswordField pwField;
    final JButton lgnBtn;
    final LogoPanel logoPanel;
    final JPanel userPanel;
    final JPanel pwPanel;
    int failedAttemptSimulator = 1;

    public LoginPanel(Controller ctrl, CardLayout cl, JPanel scrnMgr, DbConnection conn){
        u = new Utils();

        this.conn = conn;
        this.ctrl = ctrl;
        this.scrnMgr = scrnMgr;
        this.cl = cl;

        logoPanel = new LogoPanel("PLEASE LOG IN", 270);
        flashLbl = new JLabel("");
        lgnBtn = new JButton("Log In");
        usrField = new JTextField("Enter Username", 15);
        usrLbl = new JLabel("Username");
        pwField = new JPasswordField("Enter Password", 15);
        pwLbl = new JLabel("Password");
        userPanel = new JPanel();
        pwPanel = new JPanel();

        loginPanelLayout();

        usrField.addKeyListener(this);
        pwField.addKeyListener(this);
        usrField.addFocusListener(this);
        pwField.addFocusListener(this);
        lgnBtn.addActionListener(this);
    }

    // gridbag layout places each panel below the next with nested panels
    public void loginPanelLayout() {
        setLayout(new GridBagLayout());
        GridBagConstraints grid = new GridBagConstraints();

        grid.gridx = 3;
        grid.gridy = 1;
        add(flashLbl, grid);

        grid.insets = new Insets(15,0,0,0);
        grid.gridy = 2;
        add(logoPanel, grid);

        userPanel.add(usrLbl);
        userPanel.add(usrField);

        grid.gridy = 3;
        add(userPanel, grid);

        pwPanel.add(pwLbl);
        pwPanel.add(pwField);

        grid.gridy = 4;
        add(pwPanel, grid);

        grid.insets = new Insets(50,165,0,0);
        grid.gridy = 5;
        add(lgnBtn, grid);
    }

    // clears the input fields if the user clicks on them
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

    // restores the input fields if the user clicks out of them
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
        if(source == lgnBtn) {
            attemptLogin();
        }
    }

    // attempts to log in a user by checking that the password they entered matches the output hash
    // 2fa has been disabled during debugging to make development easier
    public void attemptLogin() {
        String userName = usrField.getText();
        String password = String.valueOf(pwField.getPassword());
        boolean userAuthenticated = ctrl.authenticateUser(userName, password, "login");
        if(userAuthenticated) {
            ctrl.setCurrentUser(userName, password);
            cl.show(scrnMgr, "Home");
// 			replace the 2 lines of code above with the code below and change variables in CUtils to enable 2fa
//			int nonce = CUtils.generateVerificationNumber();
//			ctrl.sendMessage(userName, password, nonce);
//			String responseStr = JOptionPane.showInputDialog ("Please enter the 6 digit number sent to your phone");
//			if(responseStr.length() == 6) {
//				int response = Integer.parseInt(responseStr);
//				if(response == nonce) {
//					ctrl.setCurrentUser(userName, password);
//					cl.show(scrnMgr, "Home");
//				}
//				else {
//					flashLbl.setText("The number you've entered was incorrect, please try again");
//				}
//			}
//			else {
//				flashLbl.setText("Your response should be exactly 6 numbers long, please try again.");
//			}
        }
        // user has not been authenticated, outputs a message to inform them why
        else {
            if(password.length() < 8 || password.length() > 128) {
                flashLbl.setText("Passwords must be between 8 - 128 characters");
            }
            else {
            	// if the user is trying to log into an actual account found in the database, begins lockout sequence
                int failedAttempts = conn.getFailedLogins(userName);
                if(failedAttempts == -1) {
                    if(failedAttemptSimulator < 4) {
                        flashLbl.setText("Login failed. After " + (4 - failedAttemptSimulator) + " more failed attempts, your account will be temporarily locked out");
                        failedAttemptSimulator++;
                    }
                    else {
                        flashLbl.setText("Your account is temporarily locked out. Try again tomorrow.");
                    }

                }
                // simulates lockout sequence for accounts that do not exist in the database to prevent giving an attacker more information
                else {
                    if(failedAttempts < 4) {
                        flashLbl.setText("Login failed. After " + (4 - failedAttempts) + " more failed attempts, your account will be temporarily locked out");
                    }
                    else {
                        flashLbl.setText("Your account is temporarily locked out. Try again tomorrow.");
                    }


                }
            }
        }
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
            attemptLogin();
        }

    }

}
