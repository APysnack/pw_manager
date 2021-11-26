package views;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;


public class LogoPanel extends JPanel {

    Utils u;
    JPanel logoPanel;
    JLabel logo;
    JLabel logoTxt;
    JLabel subLbl;

    public LogoPanel() {

    }

    public LogoPanel(String subLabelText, int subLabelOffset) {
        u = new Utils();
        logo = new JLabel();
        logoTxt = new JLabel();
        subLbl = new JLabel();
        logoPanel = new JPanel();
        buildPanel(subLabelText, subLabelOffset);
    }

    public void buildPanel(String subLabelText, int xoffset) {
        setLayout(new GridBagLayout());
        GridBagConstraints grid = new GridBagConstraints();

        String lock = "images/lock.png";
        logo = u.generateImage(lock, 50, 50);
        logoTxt = new JLabel("Secure Password Manager");
        logoTxt.setFont(new Font("Baskerville", Font.BOLD, 24));

        logoPanel.add(logo);
        logoPanel.add(logoTxt);
        logoPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 0));

        Font font = new Font("Baskerville", Font.PLAIN, 11);
        subLbl = new JLabel(subLabelText);
        Map<TextAttribute, Object> attributes = new HashMap<TextAttribute, Object>();
        attributes.put(TextAttribute.TRACKING, 0.2);
        Font font2 = font.deriveFont(attributes);
        subLbl.setFont(font2);

        grid.insets = new Insets(0,0,0,0);
        grid.gridx = 1;
        grid.gridy = 1;
        add(logoPanel, grid);
        grid.insets = new Insets(0,xoffset,55,0);
        grid.gridx = 1;
        grid.gridy = 2;
        add(subLbl, grid);

    }
}
