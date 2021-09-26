package views;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Utils {
	

	public Utils() {
		
	}
	
	public JPanel pad(int b, int l, int r, int t) {
		JPanel pad = new JPanel();
		pad.setBorder(new EmptyBorder(b, l, r, t));
		
		return pad;
	}
	
	public JLabel generateImage(String imgPath, int width, int height) {
		ImageIcon imgIcon = new ImageIcon(imgPath);
		Image image = imgIcon.getImage();
		Image scaledImage = image.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
		imgIcon = new ImageIcon(scaledImage);
		JLabel imgLabel = new JLabel(imgIcon);
		return imgLabel;
	}
	
}
