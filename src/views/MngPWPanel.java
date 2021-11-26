package views;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.awt.Cursor;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;
import java.awt.Insets;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import controller.Controller;
import model.DbConnection;
import structures.Password;
import structures.PasswordSet;
import structures.User;

public class MngPWPanel extends JPanel
        implements ActionListener, FocusListener, ComponentListener, KeyListener, MouseListener {

    CardLayout cl;
    Controller ctrl;
    Utils u;
    DbConnection conn;

    JComboBox<String> appComboBox;
    JComboBox<String> userNameComboBox;
    JComboBox<Integer> randGenLength;
    JTextField appField;
    JTextField appUsrNameField;
    JTextField randomPWField;
    JTextField displayPWField;
    JPasswordField pwField;
    JPasswordField confirmPWField;
    JCheckBox randGenUpper;
    JCheckBox randGenLower;
    JCheckBox randGenNumber;
    JCheckBox randGenSymbol;
    JPanel randGenChkPnl;
    JPanel scrnMgr;
    JLabel lengthLbl;
    JLabel appUsrNameLbl;
    JLabel flashLbl;
    JLabel titleLbl;
    JLabel pwLbl;
    JLabel appNameLbl;
    JLabel confirmPWLbl;
    JLabel clipboardLbl;
    JButton addPWBtn;
    JButton backButton;
    JButton createNewPWBtn;
    JButton editPWBtn;
    JButton deletePWBtn;
    JButton generatePWBtn;
    JButton applyChangesBtn;
    boolean onSecondaryPage;
    String userName;
    LogoPanel logoPanel;
    JPanel appUsrNamePanel;
    JPanel appNamePanel;
    JPanel pwPanel;
    JPanel confirmPWPanel;

    GridBagConstraints gr;

    public MngPWPanel() {
    }

    // initializes panel view for managing password
    public MngPWPanel(Controller ctrl, CardLayout cl, JPanel scrnMgr, DbConnection conn) {
        setName("managePWPanel");
        this.conn = conn;
        this.cl = cl;
        this.scrnMgr = scrnMgr;
        this.ctrl = ctrl;
        u = new Utils();
        initializeMngPWPanel();
    }

    public void initializeMngPWPanel() {
        setLayout(new GridBagLayout());
        gr = new GridBagConstraints();
        onSecondaryPage = false;

        // clipboard for copying/pasting a randomly generated password
        String clipboard = "images/clipboard.png";
        clipboardLbl = u.generateImage(clipboard, 25, 25);
        clipboardLbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        clipboardLbl.setToolTipText("Click the clipboard to copy the randomly generated password");

        titleLbl = new JLabel("Showing passwords for " + userName);

        appUsrNameLbl = new JLabel("Username");
        appUsrNameField = new JTextField("Enter Username", 15);
        appUsrNamePanel = new JPanel();
        appUsrNamePanel.add(appUsrNameLbl);
        appUsrNamePanel.add(appUsrNameField);

        appNameLbl = new JLabel("Edit Application Name");
        appField = new JTextField("Enter App", 15);
        appNamePanel = new JPanel();
        appNamePanel.add(appNameLbl);
        appNamePanel.add(appField);

        pwLbl = new JLabel("Password");
        pwField = new JPasswordField("Enter Password", 15);
        pwPanel = new JPanel();
        pwPanel.add(pwLbl);
        pwPanel.add(pwField);

        confirmPWLbl = new JLabel("Confirm Password");
        confirmPWField = new JPasswordField("Enter Password", 15);
        confirmPWPanel = new JPanel();
        confirmPWPanel.add(confirmPWLbl);
        confirmPWPanel.add(confirmPWField);

        // various options for the random password generator
        randGenLower = new JCheckBox("a-z");
        randGenLower.setSelected(true);
        randGenUpper = new JCheckBox("A-Z");
        randGenUpper.setSelected(true);
        randGenNumber = new JCheckBox("0-9");
        randGenNumber.setSelected(true);
        randGenSymbol = new JCheckBox("@!%$...");
        randGenSymbol.setSelected(true);
        lengthLbl = new JLabel("Length");
        randGenLength = new JComboBox<Integer>();

        int minPasswordLen = 12;
        int maxPasswordLen = 128;
        int k = minPasswordLen;

        // outputs a dot for each letter in the user's password
        // the password is not decrypted for this purpose, it is just returning an int stored in the db
        // rationale behind this explained in the SPM readme file
        for(int i = 0; i <= (maxPasswordLen - minPasswordLen); i++) {
            randGenLength.insertItemAt(k, i);
            k++;
        }

        randGenLength.setSelectedIndex(4);


        Border rgBorder = BorderFactory.createTitledBorder("Randomization Settings");
        randGenChkPnl = new JPanel();
        randGenChkPnl.add(randGenLower);
        randGenChkPnl.add(randGenUpper);
        randGenChkPnl.add(randGenNumber);
        randGenChkPnl.add(randGenSymbol);
        randGenChkPnl.add(lengthLbl);
        randGenChkPnl.add(randGenLength);
        randGenChkPnl.setBorder(rgBorder);

        randomPWField = new JTextField("", 15);
        generatePWBtn = new JButton("Generate Random Password");


        appComboBox = new JComboBox<String>();
        userNameComboBox = new JComboBox<String>();
        displayPWField = new JTextField("", 15);

        addPWBtn = new JButton("Create New Password");
        editPWBtn = new JButton("Edit Password");
        deletePWBtn = new JButton("Delete Password");


        // flashLbl used to provide user with helpful prompts about their interactions
        flashLbl = new JLabel("");
        backButton = new JButton("Back");
        createNewPWBtn = new JButton("Add This Password");
        applyChangesBtn = new JButton("Apply changes");
        logoPanel = new LogoPanel("Managing Passwords", 200);

        addPWBtn.addActionListener(this);
        appField.addFocusListener(this);
        appUsrNameField.addFocusListener(this);
        pwField.addFocusListener(this);
        confirmPWField.addFocusListener(this);
        appComboBox.addActionListener(this);
        addPWBtn.addActionListener(this);
        createNewPWBtn.addActionListener(this);
        editPWBtn.addActionListener(this);
        backButton.addActionListener(this);
        deletePWBtn.addActionListener(this);
        generatePWBtn.addActionListener(this);
        clipboardLbl.addMouseListener(this);
        applyChangesBtn.addActionListener(this);
        userNameComboBox.addActionListener(this);
        addComponentListener(this);
    }

    // updates the panel differently according to the user's interactions
    public void updateLayout(String layoutName, boolean showFlash){
        removeAll();
        flashLbl.setVisible(showFlash);

        gr.gridx = 1;
        gr.gridy = 1;
        gr.insets = new Insets(0,0,20,0);
        add(flashLbl, gr);
        gr.gridy = 2;
        gr.insets = new Insets(0,0,0,0);
        add(logoPanel, gr);
        gr.gridy = 3;
        gr.insets = new Insets(0,0,10,0);
        add(titleLbl, gr);


        if(Objects.equals(layoutName, "noPasswords")) {
            gr.gridx = 1;
            gr.gridy = 4;
            gr.insets = new Insets(0,0,70,0);
            add(addPWBtn, gr);
            gr.gridx = 1;
            gr.gridy = 5;
            add(backButton, gr);
        }

        else if(Objects.equals(layoutName, "addPW") || Objects.equals(layoutName, "editPW")) {
            gr.gridx = 1;
            gr.gridy = 4;
            gr.insets = new Insets(0,0,5,0);
            add(randGenChkPnl, gr);

            gr.gridx = 1;
            gr.gridy = 5;
            gr.insets = new Insets(0,0,10,0);
            JPanel randomGenPanel = new JPanel();
            randomGenPanel.add(randomPWField);
            randomGenPanel.add(clipboardLbl);
            randomGenPanel.add(generatePWBtn);

            add(randomGenPanel, gr);

            gr.insets = new Insets(0,0,10,0);
            add(randomGenPanel, gr);

            gr.gridx = 1;
            gr.gridy = 6;
            gr.insets = new Insets(0,0,5,0);

            add(appNamePanel, gr);
            gr.gridx = 1;
            gr.gridy = 7;
            gr.insets = new Insets(0,0,5,0);

            add(appUsrNamePanel, gr);
            gr.gridx = 1;
            gr.gridy = 8;
            gr.insets = new Insets(0,0,5,0);
            add(pwPanel, gr);

            gr.gridx = 1;
            gr.gridy = 9;
            gr.insets = new Insets(0,0,5,0);
            add(confirmPWPanel, gr);

            JPanel actionPanel = new JPanel();
            if(layoutName.equals("editPW")) {
                actionPanel.add(applyChangesBtn);
            }
            else {
                actionPanel.add(createNewPWBtn);
            }

            actionPanel.add(backButton);

            gr.gridx = 1;
            gr.gridy = 10;
            gr.insets = new Insets(0,0,10,0);
            add(actionPanel, gr);

        }
        else if(Objects.equals(layoutName, "main")) {
            gr.gridx = 1;
            gr.gridy = 3;
            gr.insets = new Insets(0,0,30,0);
            add(titleLbl, gr);
            gr.gridx = 1;
            gr.gridy = 4;
            gr.insets = new Insets(0,0,10,0);
            JPanel dropDownPanel = new JPanel();
            dropDownPanel.add(appComboBox);
            dropDownPanel.add(userNameComboBox);
            dropDownPanel.add(displayPWField);
            dropDownPanel.add(clipboardLbl);

            add(dropDownPanel, gr);

            gr.gridx = 1;
            gr.gridy = 5;
            gr.insets = new Insets(0,0,200,0);
            JPanel mainActionPanel = new JPanel();
            mainActionPanel.add(addPWBtn);
            mainActionPanel.add(editPWBtn);
            mainActionPanel.add(deletePWBtn);
            mainActionPanel.add(backButton);
            add(mainActionPanel, gr);
        }

        repaint();
        revalidate();
    }

    // updates to the the main view of the password panel shown by default, assuming the user has passwords in the system
    public void mainView() {
        String selectedApp = (String) appComboBox.getSelectedItem();
        String selectedUserName = (String) userNameComboBox.getSelectedItem();
        populatePasswordData(selectedApp, selectedUserName);

        titleLbl.setText("Displaying all passwords for " + userName);
        onSecondaryPage = false;
        updateLayout("main", false);
    }

    // updates to the view that is shown when the user has not already created any secondary passwords
    public void noPasswordsView() {
        titleLbl.setText("No passwords for this user. Please add one");
        onSecondaryPage = false;
        updateLayout("noPasswords", false);
    }

    // updates to the view that is shown when the user is trying to edit a secondary password
    public void editPWView(String appName, String username) {
        titleLbl.setText("Editing password for " + appName);
        onSecondaryPage = true;
        populatePasswordData(appName, username);
        updateLayout("editPW", false);
    }

    // updates to the view shown when the user is trying to add in a new password
    public void addPWView() {
        titleLbl.setText("Creating a new password for " + userName);
        onSecondaryPage = true;
        appNameLbl.setText("App Name");
        clearPasswordData();
        updateLayout("addPW", false);
    }
    
    // initializes the combo box containing all of the user's secondary passwords and organizes them based on
    // application name. E.g. all accounts for Gmail are grouped together. 
    public void initializeComboBox() {
        appComboBox.removeAllItems();
        userNameComboBox.removeAllItems();
        ArrayList<PasswordSet> allPasswords = ctrl.getAllPasswords();
        if (allPasswords.size() > 0) {
            for (int i = 0; i < allPasswords.size(); i++) {
                appComboBox.insertItemAt(allPasswords.get(i).getAppName(), i);
            }

            populateAppUsersFor(allPasswords.get(0));
            appComboBox.setSelectedIndex(0);
            mainView();
        } else {
            noPasswordsView();
        }
    }

    // Removes all items currently in the dropdown box then populates it with all the of the user's secondary passwords
    public void populateAppUsersFor(PasswordSet passwordSet){
        userNameComboBox.removeAllItems();

        ArrayList<Password> passwordList = passwordSet.getPasswordList();

        for (int i = 0; i < passwordList.size(); i++) {
            userNameComboBox.insertItemAt(passwordList.get(i).getAppUserName(), i);
        }

        userNameComboBox.setSelectedIndex(0);
    }

    // once the user enters information for a password, restores the field so that it appears unaltered
    public void clearPasswordData() {
        appField.setText("Application Name");
        appUsrNameField.setText("Enter Username");
        pwField.setText("Enter Password");
        confirmPWField.setText("Enter Password");
    }

    // displays the password data for the associated account
    public void populatePasswordData(String selectedApp, String username) {
    	// field is reused depending on context. If onSecondaryPage is false, displays 
    	// the decrypted password for the currently selected application and username
        if (!onSecondaryPage) {
            if(selectedApp != null && username != null) {
                displayPWField.setText(ctrl.getDecryptedPassword(selectedApp, username));
            }
        // else, we are on a page where the password for thee selected app and username 
        // is being edited. does not display the decrypted password information, but uses
        //placeholder asterisks instead, primarily for psychological acceptability   
        } else {
            Password pwData = ctrl.getPasswordInfo(selectedApp, username);
            appField.setText(pwData.getAppName());
            appUsrNameField.setText(pwData.getAppUserName());
            int pwLen = pwData.getPasswordLength();
            StringBuilder pwStr = new StringBuilder();
            for (int i = 0; i < pwLen; i++) {
                pwStr.append("*");
            }
            pwField.setText(pwStr.toString());
            confirmPWField.setText(pwStr.toString());
        }

    }

    // clears the input fields if the user clicks into it
    public void passwordFieldGainFocus(JPasswordField pwField) {
        pwField.setEchoChar('*');
        boolean allAsterisks = String.valueOf(pwField.getPassword()).matches("[*]*");

        if (String.valueOf(pwField.getPassword()).equals("Enter Password") || allAsterisks) {
            pwField.setText("");
        }
    }

    // restores the input fields if the user clicks out of it
    public void passwordFieldLoseFocus(JPasswordField pwField) {
        if (String.valueOf(pwField.getPassword()).equals("")) {
            pwField.setText("Enter Password");
            pwField.setEchoChar((char) 0);
        }
    }

    public void backBtnBehavior() {
        // if the user is looking at the "edit" or "add" secondary pw views and clicks "back", remains on the current panel, 
        // but updates the view for the "main view'
        if (onSecondaryPage) {
            titleLbl.setText("Displaying passwords for " + userName);
            initializeComboBox();
            mainView();
            
        // else, the user is already on the "main view" and wants to go back to the 'home' panel
        } else {
            cl.show(scrnMgr, "Home");
        }
    }

    // displays the initialized password manager panel on entrance
    @Override
    public void componentShown(ComponentEvent e) {
        if (Objects.equals(((Component) e.getSource()).getName(), "managePWPanel")) {
            User user = conn.getCurrentUser();
            this.userName = user.getUsername();
            titleLbl.setText("Displaying passwords for " + userName);
            initializeComboBox();
        }

    }

    // clears input fields when the user clicks into them
    @Override
    public void focusGained(FocusEvent e) {
        Object source = e.getSource();
        if (source == appField) {
            if (appField.getText().equals("Application Name")) {
                appField.setText("");
            }
        }

        if (source == appUsrNameField) {
            if (appUsrNameField.getText().equals("Enter Username")) {
                appUsrNameField.setText("");
            }
        }

        else if (source == pwField) {
            passwordFieldGainFocus(pwField);
        }

        else if (source == confirmPWField) {
            passwordFieldGainFocus(confirmPWField);
        }
    }
    
    // restores the input fields when the user clicks out of them
    @Override
    public void focusLost(FocusEvent e) {
        Object source = e.getSource();
        if (source == appField) {
            if (appField.getText().isEmpty()) {
                appField.setText("Application Name");
            }
        } else if (source == appUsrNameField) {
            if (appUsrNameField.getText().isEmpty()) {
                appUsrNameField.setText("Enter Username");
            }
        }

        else if (source == pwField) {
            passwordFieldLoseFocus(pwField);
        } else if (source == confirmPWField) {
            passwordFieldLoseFocus(confirmPWField);
        }

    }

    // attempts to add a new secondary password to the database
    public void attemptAddPassword() {
        String appName = appField.getText();
        String appUserName = appUsrNameField.getText();

        // checks to see if this user already has a secondary pw associated with this appName and appUserName
        boolean collisionCaused = conn.checkPasswordCollision(appName, appUserName);
        
        // only adds password if there is no collision
        if(!collisionCaused) {
            char[] password = pwField.getPassword();
            char[] passwordConfirm = confirmPWField.getPassword();

            // confirms both the password and confirm password fields match
            if (Arrays.equals(password, passwordConfirm)) {
                String stringPW = String.valueOf(password);
                
                // attempts to add password and receive boolean depending on success or failure
                boolean addSuccessful = ctrl.addNewPassword(appName, appUserName, stringPW);
                if (addSuccessful) {
                    flashLbl.setText("Password successfully added to database!");
                    appField.setText("Application Name");
                    appUsrNameField.setText("Enter Username");
                    pwField.setText("Enter Password");
                    confirmPWField.setText("Enter Password");
                } else {
                    flashLbl.setText("Error in user input, password could not be added");
                }
            } else {
                flashLbl.setText("Passwords do not match, please try again");
            }
        }
        else {
            flashLbl.setText("Cannot use same username/application combination twice");
        }
        flashLbl.setVisible(true);
    }

    @Override
    // different responses to user input/buttons selected by the user
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        // if the user has clicked on "addPWBtn", perform addPWView function
        if (source == addPWBtn) {
            addPWView();
        }
        
        // if the user has clicked on "generate password", generates a random password according to their control settings
        else if (source == generatePWBtn) {
            boolean lowercase = randGenLower.isSelected();
            boolean uppercase = randGenUpper.isSelected();
            boolean numeric = randGenNumber.isSelected();
            boolean symbols = randGenSymbol.isSelected();

            int length = 0;
            if (randGenLength.getSelectedItem() != null){
                length = (int) randGenLength.getSelectedItem();
            }

            if(lowercase || uppercase || numeric || symbols) {
            	// generates a random password based on the selected settings
                String randomPW = ctrl.generateRandomPassword(lowercase, uppercase, numeric, symbols, length);
                
                // displays the random password in the field
                randomPWField.setText(randomPW);
            }
            else {
                JOptionPane.showMessageDialog(null, "At least one checkbox must be selected for password generation");
            }
        } else if (source == backButton) {
            backBtnBehavior();
        } else if (source == createNewPWBtn) {
            attemptAddPassword();

        }
        // listens for changes being made to the appComboBox
        else if (source == appComboBox) {
        	// gets all password information for the application the user has selected
            String selectedApp = (String) appComboBox.getSelectedItem();
            ArrayList<PasswordSet> allPasswords = ctrl.getAllPasswords();

            // fills the app username combo box with the usernames associated with the selected application
            for(int i = 0; i < allPasswords.size(); i++) {
                if (allPasswords.get(i).getAppName().equals(selectedApp)) {
                    populateAppUsersFor(allPasswords.get(i));
                }
            }

            String selectedUserName = (String) userNameComboBox.getSelectedItem();
            populatePasswordData(selectedApp, selectedUserName);
        }
        
        // listens for changes being made to the userNameComboBox
        else if (source == userNameComboBox) {
        	// displays password data based on the app and username selected
            String selectedApp = (String) appComboBox.getSelectedItem();
            String selectedUserName = (String) userNameComboBox.getSelectedItem();
            populatePasswordData(selectedApp, selectedUserName);
        }

        // listens for the user to select that they want to delete a secondary password
        else if (source == deletePWBtn) {
            String appName = (String) appComboBox.getSelectedItem();
            String userName = (String) userNameComboBox.getSelectedItem();
            
            // displays prompt if no passwords exist in the combo box
            if(appName == null || userName == null) {
                JOptionPane.showMessageDialog(null,
                        "There are currently no passwords to delete, please add one", "WARNING!!!", JOptionPane.WARNING_MESSAGE);
            }
            else {
            	// gives the user a dialog box to confirm that they wish to delete
                int response = JOptionPane.showConfirmDialog(null,
                        "This will delete the password for " + appName + " with the username " + userName + ". Do you still wish to continue?", "WARNING!!!",
                        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                
                // if the user selects yes, attempts deletion
                if (response == 0) {
                    boolean pwDeleted = ctrl.deletePassword(appName, userName);
                    if (pwDeleted) {
                    	// if password deletes successfully, reupdates the combobox with the remaining passwords in the db
                        flashLbl.setText("Password successfully deleted!");
                        displayPWField.setText("");
                        initializeComboBox();
                        updateLayout("main", true);

                    } else {
                        flashLbl.setText("ERROR: Password could not be deleted");
                    }
                }
            }
        } 
        
        // listens for the user to attempt to edit a password
        else if (source == editPWBtn) {
            String selectedApp = (String) appComboBox.getSelectedItem();
            String selectedUsername = (String) userNameComboBox.getSelectedItem();
            
            // if there are no passwords to edit, displays prompt
            if(selectedApp == null || selectedUsername == null) {
                JOptionPane.showMessageDialog(null,
                        "There are currently no passwords to edit, please add one", "WARNING!!!", JOptionPane.WARNING_MESSAGE);
            }
            else {
            	// else, redirects user to the edit password view
                editPWView(selectedApp, selectedUsername);
            }
        }

        // if user has selected to edit the password, attempts to do the update
        else if (source == applyChangesBtn) {
            attemptPWUpdate();
        }
    }

    // attempts to modify an existing secondary password in the database
    public void attemptPWUpdate() {
        String oldAppName = (String) appComboBox.getSelectedItem();
        String oldAppUserName = (String) userNameComboBox.getSelectedItem();

        // gives the user a prompt to confirm they wish to update this password
        int response = JOptionPane.showConfirmDialog(null,
                "You're about to make changes to " + oldAppName + ". Do you wish to continue?", "WARNING!!!",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (response == 0) {
            String newAppName = appField.getText();
            String newAppUserName = appUsrNameField.getText();

            // if appName and appUserName have not been changed, only the password is being modified
            // a collision is expected in this case. Otherwise, collisions should not be allowed
            if(oldAppName != null && newAppName != null && oldAppUserName != null && newAppUserName != null){
                boolean sameAppName = oldAppName.equalsIgnoreCase(newAppName);
                boolean sameAppUserName = oldAppUserName.equalsIgnoreCase(newAppUserName);
                boolean passwordChangeOnly = (sameAppName && sameAppUserName);
                boolean collisionCaused = conn.checkPasswordCollision(newAppName, newAppUserName);

                if(!collisionCaused || passwordChangeOnly) {
                    char[] password = pwField.getPassword();
                    char[] passwordConfirm = confirmPWField.getPassword();

                    // confirms that both password and confirm password fields match each other
                    if (Arrays.equals(password, passwordConfirm)) {
                        String stringPW = String.valueOf(password);
                        
                        // returns a boolean specifying if the edit was successful
                        boolean addSuccessful = ctrl.editPassword(oldAppName, oldAppUserName, newAppName, newAppUserName, stringPW);
                        if (addSuccessful) {
                            flashLbl.setText("Password successfully modified!");
                            appField.setText("Application Name");
                            pwField.setText("Enter Password");
                            appUsrNameField.setText("Enter Username");
                            confirmPWField.setText("Enter Password");
                        } else {
                            flashLbl.setText("Error in user input, password could not be added");
                        }
                    }
                    else {
                        flashLbl.setText("The passwords you entered do not match. Please try again");
                    }
                }
                else {
                    flashLbl.setText("Collision detected: cannot use the same application/username combination twice");
                }

            }
            else {
                flashLbl.setText("Error detected with input");
            }


            flashLbl.setVisible(true);

        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Object source = e.getSource();
        // if user clicks on the clipboard, copies the text in the field to their clipboard
        if (source == clipboardLbl) {
            String textToCopy = "";

            // if the user is on add or edit view, the text being copied is from the random generator
            if (onSecondaryPage) {
                textToCopy = randomPWField.getText();
            }
            
            // else, the text being copied is the decrypted password from the displayPWField
            else {
                textToCopy = displayPWField.getText();
            }
            StringSelection stringSel = new StringSelection(textToCopy);
            clipboardLbl.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            try {
                TimeUnit.MILLISECONDS.sleep(400);
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            
            // displays a prompt if the user is attempting to copy an empty value to the clipboard
            if (textToCopy.equals("")) {
                String message = "";
                if (onSecondaryPage) {
                    message = "Please generate a password first";
                } else {
                    message = "This field is empty";
                }
                JOptionPane.showMessageDialog(null, message);
            }
            clipboardLbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSel, null);
        }

    }

    @Override
    public void componentHidden(ComponentEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

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
        // TODO Auto-generated method stub

    }

    @Override
    public void componentResized(ComponentEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void componentMoved(ComponentEvent e) {
        // TODO Auto-generated method stub

    }

}
