package View;

import java.awt.Color;

import javax.swing.*;

public class JFMainWindow {
	
	private static JFrame jfWindow = null;
	private static boolean hasInit = false;
	
	
	private JFMainWindow() {}
	
	
	public static JFrame getInstance() {
		
		if( hasInit == false ) {
			
			init();
			hasInit = true;
		}
		
		return jfWindow;
	}
	
	
	
	private static void init() {
		
		jfWindow = new JFrame();

		jfWindow.setTitle("Test Version 3");
		jfWindow.setLayout(null);
		jfWindow.setBounds(0, 0, 800, 500);					//REVISIT
		jfWindow.setLocationRelativeTo(null);
		jfWindow.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		
		jfWindow.setUndecorated(true);		
		jfWindow.getContentPane().setBackground( new Color(35, 8, 122) );
		//jfWindow.getContentPane().setBackground( Color.WHITE );
		
		
	}
	
	

}
