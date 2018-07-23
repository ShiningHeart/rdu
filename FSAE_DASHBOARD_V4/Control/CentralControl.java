package Control;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import Control.ActionListener.DataRefresher;
import Model.SystemConfigTable;
import View.*;

public class CentralControl {

	private static JFrame jfWindow = null;
	private static JPanel jpStart = null;
	private static JPRace jpRace = null;
	private static JTabbedPane jpDebug = null;
	
	private static Timer dataUpdater = null;
	
	private static SocketConnection sock_conn = new SocketConnection();
	
	public static enum Scenes{
		NONE,
		START,
		RACE,
		DEBUG_VCU,
		DEBUG_BMS,
		DEBUG_MTR
	}  
	
	public static Scenes currentScene = Scenes.NONE;
	
	
	public static void init() {
		
		//Init the system config
		java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = screenSize.width;
		int screenHeight = screenSize.height;
		
		/* SOLUTION 1
		if(screenWidth > 900 && screenHeight > 540) {
			SystemConfigTable.SCREEN_WIDTH = 800;
			SystemConfigTable.SCREEN_HEIGHT = 480;
			
			
		}else {
			SystemConfigTable.SCREEN_WIDTH = screenSize.width;
			SystemConfigTable.SCREEN_HEIGHT = screenSize.height;
		}
		*/
		
		//SOLUTION 2
		/*SystemConfigTable.SCREEN_WIDTH = (int)(800 * (screenHeight - 20) / 480 );
		SystemConfigTable.SCREEN_HEIGHT = (screenHeight - 20);
		*/
		
		
		
		//SOLUTION 3
		SystemConfigTable.SCREEN_WIDTH = screenWidth;
		SystemConfigTable.SCREEN_HEIGHT = (screenHeight - 20);
		
		
		
		//Load main window
		jfWindow = JFMainWindow.getInstance();
		
		
	}
	
	public static void loadStartScene() {
		
		jpStart = JPStart.getInstance();
		currentScene = Scenes.START;
		jfWindow.getContentPane().add(jpStart);
		

		
		int countDown = 4000;						//In milliseconds 
		
		new Timer(countDown, new ActionListener() {
		    public void actionPerformed(ActionEvent evt) {
		    	
		    	jpStart.setVisible(false);
		    	
		    	currentScene = Scenes.RACE;
		    	
		        CentralControl.loadRaceScene();
		        CentralControl.loadDebugScene();
		        
		        CentralControl.loadRaceScene();
		        startUpdateData();
		    	//CentralControl.loadDebugScene();
		        ((Timer)(evt.getSource())).stop();
		        
		    }    
		}).start();
		
		
	}
	
	
	
	
	
	
	public static void loadRaceScene() {
		
		if( jpRace == null ) {
			
			jpRace = JPRace.getInstance();
			jfWindow.getContentPane().add(jpRace);
			
		}
		if(jpDebug != null) {
			jpDebug.setVisible(false);
		}
		
		currentScene = Scenes.RACE;
		
		jpRace.setVisible(true);
		jpRace.setLocation(0,  0);
		
	}
	
	
	public static void loadDebugScene() {
		
		if(jpDebug == null) {
			
			jpDebug = JPDebug.getInstance();
			jfWindow.getContentPane().add(jpDebug);
			
		}
		
		if(jpRace != null) {
			jpRace.setVisible(false);
		}
		
		currentScene = Scenes.DEBUG_VCU;
		
		jpDebug.setVisible(true);
		jpDebug.setLocation(0, 0);
		
	}
	
	
	
	
	
	public static void show(boolean val) {
		
		jfWindow.setVisible(val);
	}
	
	
	public static void appendMenuBar() {
		
		MenuBarAppender.appendMenuBar(jfWindow);
		
	}
	
	public static void startNetwork() {
		
	}
	
	
	public static void startUpdateData() {
		if(dataUpdater == null) {
			dataUpdater = new Timer(10, new DataRefresher());
		}
		
		dataUpdater.start();
	}
	
	public static void enableSocket() {
		
		//THREAD UNSAFE : NEED MODIFY FOR NEXT VERSION
		sock_conn.start();
		
	}
	
	public static void disableSocket() {
		
		sock_conn.interrupt();
		
	}
	
	
	
}
