package views;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Insets;
import java.util.Objects;

import controller.Controller;
import model.DbConnection;
import structures.User;

// home panel that either directs the user to the user management or password management panels
public class HomePanel extends JPanel implements ComponentListener, ActionListener {

    JPanel scrnMgr;
    CardLayout cl;
    Controller ctrl;
    String userName;
    JLabel userPnlLbl;
    JButton managePWBtn;
    JButton manageUsrBtn;
    JButton logOutButton;
    DbConnection conn;
    LogoPanel logoPanel;

    public HomePanel() {

    }

    public HomePanel(Controller ctrl, CardLayout cl, JPanel scrnMgr, DbConnection conn) {
        setName("userPanel");
        this.ctrl = ctrl;
        this.cl = cl;
        this.scrnMgr = scrnMgr;
        this.conn = conn;
        addComponentListener(this);
        userPnlLbl = new JLabel("Welcome " + userName);
        logOutButton = new JButton("Log Out");
        manageUsrBtn = new JButton("Manage Users");
        managePWBtn = new JButton("Manage Passwords");
        logoPanel = new LogoPanel("ADMINISTRATIVE DASHBOARD", 170);
        managePWBtn.addActionListener(this);
        logOutButton.addActionListener(this);
        homePanelLayout();
    }

    // only displays the button to access user management if the user had admin privileges
    // always displays the button to manage passwords for the current user
    // gridbag layout places on object beneath the next
    public void homePanelLayout() {
        setLayout(new GridBagLayout());
        GridBagConstraints grid = new GridBagConstraints();

        grid.gridx = 3;
        grid.gridy = 1;
        add(logoPanel, grid);


        grid.insets = new Insets(0,0,20,0);
        grid.gridy = 3;
        add(userPnlLbl, grid);

        grid.insets = new Insets(0,0,10,0);
        grid.gridy = 4;
        add(managePWBtn, grid);

        grid.insets = new Insets(0,0,70,0);
        grid.gridy = 5;
        add(manageUsrBtn, grid);

        grid.gridy = 6;
        add(logOutButton, grid);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void componentMoved(ComponentEvent e) {
        // TODO Auto-generated method stub

    }
    
    // only displays the button to access user management if the user had admin privileges
    @Override
    public void componentShown(ComponentEvent e) {
        if(Objects.equals(((Component) e.getSource()).getName(), "userPanel")) {
            User currentUser = conn.getCurrentUser();
            this.userName = currentUser.getUsername();
            userPnlLbl.setText("Welcome " + userName);
            boolean userIsAdmin = false;
            ArrayList<Boolean> userPermissions = currentUser.getUserPermissions();
            if(userPermissions.size() > 0) {
                for(int i = 0; i < userPermissions.size(); i++) {
                    if(userPermissions.get(i)) {
                        userIsAdmin = true;
                        break;
                    }
                }
                if(userIsAdmin) {
                    manageUsrBtn.setVisible(true);
                    manageUsrBtn.addActionListener(this);
                }
                else {
                    manageUsrBtn.setVisible(false);
                }
            }
        }
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    // redirects user depending on which button they click on
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == manageUsrBtn) {
            cl.show(scrnMgr, "ManageUsers");
        }
        else if(e.getSource() == managePWBtn) {
            cl.show(scrnMgr, "ManagePasswords");
        }
        else if(e.getSource() == logOutButton) {
            AppWindow window = (AppWindow) SwingUtilities.getWindowAncestor(this);
            window.rebuildApp();
        }
    }
}