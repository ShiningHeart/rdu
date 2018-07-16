package Control.Listener;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JRadioButtonMenuItem;

import Control.CentralControl;

public class MenuModeSelectListener implements ItemListener{

	@Override
	public void itemStateChanged(ItemEvent e) {

		JRadioButtonMenuItem item = (JRadioButtonMenuItem)(e.getSource());
		
		if( item.isSelected() == true  ) {
			
			if(item.getText() == "Race Mode") {
				
				CentralControl.loadRaceScene();
				
			}else if( item.getText() == "Debug Mode" ) {
				
				CentralControl.loadDebugScene();
				
			}else if( item.getText() == "Sleep Mode" ) {
				
			}
			
			
			
		}
		
		
		
		
		
	}
	
	
	
	
	

}
