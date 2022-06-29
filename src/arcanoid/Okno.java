package arcanoid;

import java.awt.Dimension;
import javax.swing.JFrame;


@SuppressWarnings("serial")
public class Okno extends JFrame {	

	private final String title = "The Arcanoid Supreme";
	
	Dimension size = new Dimension();
	
	//Initializer
	public void initComponents() {
		this.setTitle(title);
		this.setSize(700, 600);
		//this.setBounds(550, 300, 700, 600);
		this.setResizable(false);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
	}
	
	//Constructor
		public Okno() {
			initComponents();
		}
}
