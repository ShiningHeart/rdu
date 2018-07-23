/*	Main.java
 * 		Entry point of entire program. Program written for SFU Team Phamton dashboard
 * display project.
 * 
 *  Version: v3
 */


package Control;

import Model.SystemConfigTable;
import View.JFMainWindow;
import View.JPRace;
import View.MenuBarAppender;

public class Main {

	public static void main(String[] args) {
		
		
		CentralControl.init();
		CentralControl.enableSocket();
		CentralControl.appendMenuBar();		
		CentralControl.loadStartScene();
		
		CentralControl.show(true);
		
		MenuBarAppender.updateHeight();
		
		
		
		
		

	}
}
