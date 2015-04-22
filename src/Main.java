import javax.swing.JFrame;

import ui.*;

public class Main {
	public static void main(String[] args) {
		JFrame f = new JFrame();
		Canvas c = new Canvas(1, 100);
		
		f.setContentPane(c);
		f.setSize(800, 600);
		f.setVisible(true);
	}
}
