package arcanoid;

import java.awt.BorderLayout;

public class MainGame {

	public static void main(String[] args) {
			 
		Okno window = new Okno();
		PanelGry panel = new PanelGry();
		
		window.add(panel, BorderLayout.CENTER);
		window.pack();
		
		panel.setFocusable(true);
		panel.requestFocus();	
		
	}
}
