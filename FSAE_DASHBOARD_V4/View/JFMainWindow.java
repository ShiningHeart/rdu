package View;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.*;

import Model.SystemConfigTable;

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
		jfWindow.setLayout( null );
		jfWindow.setBounds(0, 0, SystemConfigTable.SCREEN_WIDTH, SystemConfigTable.SCREEN_HEIGHT );
		jfWindow.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		
		jfWindow.setUndecorated(true);
		jfWindow.getContentPane().setBackground( Color.WHITE );
		
	}
	
	public static int getWidth() {
		return jfWindow.getWidth();
	}

	public static int getHeight() {
		return jfWindow.getHeight();
	}
	
	
}
