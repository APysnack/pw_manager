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

import controller.Controller;
import model.DbConnection;

public class SetupPanel extends JPanel implements FocusListener, ActionListener {

    JTextField usrField;
    JPasswordField pwField;
    JTextField mobileField;
    JButton createUserBtn;
    JPanel scrnMgr;
    JPanel welcomePnl;
    JPanel disclaimerPnl;
    JPanel checkBoxPnl;
    JPanel userInfoPnl;
    CardLayout cl;
    Controller ctrl;
    Utils u;
    DbConnection conn;
    LogoPanel logoPanel;

    public SetupPanel() {

    }

    public SetupPanel(Controller ctrl, CardLayout cl, JPanel scrnMgr, DbConnection conn) {

        u = new Utils();
        this.ctrl = ctrl;
        this.cl = cl;
        this.scrnMgr = scrnMgr;
        this.conn = conn;

        logoPanel = new LogoPanel("SETTING UP YOUR FIRST ACCOUNT", 140);
        welcomePnl = buildWelcomePanel();
        disclaimerPnl = buildDisclaimerPanel();
        checkBoxPnl = buildCheckBoxPanel();
        userInfoPnl = buildUserInfoPanel();

        buildLayout();
    }

    public void buildLayout() {
        setLayout(new GridBagLayout());
        GridBagConstraints grid = new GridBagConstraints();
        grid.weightx=1.;
        grid.fill=GridBagConstraints.HORIZONTAL;

        grid.insets = new Insets(120,0,0,0);
        grid.gridx = 1;
        grid.gridy = 1;
        add(logoPanel, grid);
        grid.insets = new Insets(0,50,20,50);
        grid.gridy = 2;
        add(welcomePnl, grid);
        grid.insets = new Insets(0,220,0,220);
        grid.gridy = 3;
        add(userInfoPnl, grid);
        grid.insets = new Insets(0,70,30,70);
        grid.gridy = 4;
        add(checkBoxPnl, grid);
        grid.gridy = 5;
        grid.insets = new Insets(0,120,100,120);
        add(disclaimerPnl, grid);
    }

    public JPanel buildWelcomePanel() {
        // creates and center top label
        JLabel welcomeLbl = new JLabel(
                "Welcome! Since this is your first time using this app, you must create a username and password");
        welcomeLbl.setHorizontalAlignment(JLabel.CENTER);
        JPanel welcomePnl = new JPanel();
        welcomePnl.add(welcomeLbl);

        return welcomePnl;
    }

    public JPanel buildDisclaimerPanel() {
        // creates and centers bottom labels
        JLabel disclaimerLbl = new JLabel("WARNING: This user will be able to add, delete, and modify other users");
        JLabel disclaimerLbl2 = new JLabel(
                "Use a SECURE password, as this will control access to all your other passwords");
        disclaimerLbl.setHorizontalAlignment(JLabel.CENTER);
        disclaimerLbl2.setHorizontalAlignment(JLabel.CENTER);
        JPanel disclaimerPnl = new JPanel(new GridLayout(4, 1, 0, 3));
        disclaimerPnl.add(disclaimerLbl);
        disclaimerPnl.add(disclaimerLbl2);

        createUserBtn = new JButton("I understand. Create this user");
        createUserBtn.addActionListener(this);
        disclaimerPnl.add(createUserBtn);

        return disclaimerPnl;
    }

    public JPanel buildCheckBoxPanel() {
        // creates checkbox
        JCheckBox addUsersCheck = new JCheckBox("Add Users", true);
        JCheckBox editUsersCheck = new JCheckBox("Edit Users", true);
        JCheckBox deleteUsersCheck = new JCheckBox("Delete Users", true);
        JCheckBox managePWsCheck = new JCheckBox("Manage Passwords", true);
        JPanel checkBoxPnl = new JPanel();
        Border checkBoxBorder = BorderFactory.createTitledBorder("User Privileges");
        checkBoxPnl.add(addUsersCheck);
        checkBoxPnl.add(editUsersCheck);
        checkBoxPnl.add(deleteUsersCheck);
        checkBoxPnl.add(managePWsCheck);
        checkBoxPnl.setBorder(checkBoxBorder);

        addUsersCheck.setEnabled(false);
        editUsersCheck.setEnabled(false);
        deleteUsersCheck.setEnabled(false);
        managePWsCheck.setEnabled(false);

        return checkBoxPnl;
    }

    public JPanel buildUserInfoPanel() {
        // label/input box for username
        JLabel userLbl = new JLabel("Username");
        JTextField userTxtFld = new JTextField("Enter Username", 15);
        Box userNameBox = Box.createHorizontalBox();
        userNameBox.add(userLbl);
        userNameBox.add(Box.createHorizontalStrut(5));
        userNameBox.add(userTxtFld);

        // label/input box for password
        JLabel pwLbl = new JLabel("Password");
        JPasswordField pwTxtFld = new JPasswordField("Enter Password", 15);
        Box pwBox = Box.createHorizontalBox();
        pwBox.add(pwLbl);
        pwBox.add(Box.createHorizontalStrut(6));
        pwBox.add(pwTxtFld);

        // label/input box for password
        JLabel mobileLbl = new JLabel("Mobile #");
        JTextField mobileTxtFld = new JTextField("Enter Mobile Number", 15);
        Box mobileBox = Box.createHorizontalBox();
        mobileBox.add(mobileLbl);
        mobileBox.add(Box.createHorizontalStrut(17));
        mobileBox.add(mobileTxtFld);

        userTxtFld.addFocusListener(this);
        pwTxtFld.addFocusListener(this);
        mobileTxtFld.addFocusListener(this);

        this.usrField = userTxtFld;
        this.pwField = pwTxtFld;
        this.mobileField = mobileTxtFld;

        JPanel innerPnl = new JPanel(new GridLayout(3, 1, 0, 1));
        innerPnl.add(userNameBox);
        innerPnl.add(pwBox);
        innerPnl.add(mobileBox);

        return innerPnl;
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
        else if(source == mobileField) {
            if (mobileField.getText().equals("Enter Mobile Number")) {
                mobileField.setText("");
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
        else if(source == mobileField) {
            if (mobileField.getText().isEmpty()) {
                mobileField.setText("Enter Mobile Number");
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source == createUserBtn) {
            String userName = usrField.getText();
            String password = String.valueOf(pwField.getPassword());
            String mobileNumber = mobileField.getText();

            boolean addSuccessful = ctrl.addUser(userName, password, true, true, true, mobileNumber);

            if(addSuccessful) {
                AppWindow window = (AppWindow) SwingUtilities.getWindowAncestor(this);
                window.rebuildApp();
            }
            else {
                cl.show(scrnMgr, "Error");
            }
        }

    }
}