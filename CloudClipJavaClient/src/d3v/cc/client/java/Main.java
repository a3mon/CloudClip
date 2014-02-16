package d3v.cc.client.java;

import java.util.logging.Logger;

import javax.swing.JFrame;


public class Main {
	
	private static final Logger log = Logger.getLogger(Main.class.getName());
	

	public static void main(String[] args) {
		log.entering(Main.class.getName(), "main");
		
		Controller controller = new Controller() ;
		new JFrame().setVisible(true);

	}

}
