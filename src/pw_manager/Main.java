package pw_manager;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import views.LoginWindow;
import model.DbConnection;

import javax.swing.UnsupportedLookAndFeelException;
import java.util.ArrayList;


public class Main {
	public static void main(String args[]) {
		LoginWindow login = new LoginWindow();
		login.setSize(900, 550);
		login.setVisible(true);
	}
}
