package views;

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
	
}
