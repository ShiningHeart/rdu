package View;


import java.awt.Color;

import javax.swing.*;

import Model.GlobalConfiguration;





public class JPDebug {
	
	private static JTabbedPane jpDebug = new JTabbedPane();
	private static boolean hasInit = false;
	
	
	private JPDebug() {}
	
	
	public static JTabbedPane getInstance() {
	
		if(hasInit == false) {
			
			init();
			hasInit = true;
			
		}
		
		return jpDebug;
	}
	
	
	public static void init() {
		
		
		jpDebug.setLayout(null);
		jpDebug.setBounds(0, 0, 800, 480);
		
		jpDebug.setBackground( GlobalConfiguration.THEME_COLOR );
		
		jpDebug.addTab("VCU", JPTabVCU.getInstance());
		jpDebug.addTab("BMS", JPTabBMS.getInstance());
		jpDebug.addTab("MTR", JPTabMTR.getInstance());
		
		jpDebug.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		jpDebug.setVisible(true);
		
		
		
	}
	
	
	
	

}
