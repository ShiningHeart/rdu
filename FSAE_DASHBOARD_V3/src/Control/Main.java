/*	Main.java
 * 		Entry point of entire program. Program written for SFU Team Phamton dashboard
 * display project.
 * 
 *  Version: v3
 */


package Control;


public class Main {

	public static void main(String[] args) {

		
		CentralControl.init();
		CentralControl.appendMenuBar();
		
		CentralControl.loadStartScene();
		
		CentralControl.show(true);
		
		
		

	}

}
