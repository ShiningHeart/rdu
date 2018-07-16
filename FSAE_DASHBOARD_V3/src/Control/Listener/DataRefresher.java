package Control.Listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Control.CentralControl;
import Model.DataTable;
import View.JPRace;
import View.JPTabBMS;
import View.JPTabMTR;
import View.JPTabVCU;

public class DataRefresher implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		
		
		switch( CentralControl.currentScene ) {
		
		case RACE:
			
			JPRace.updateConnBMS(DataTable.conn_BMS);
			JPRace.updateConnMTR(DataTable.conn_MTR);
			JPRace.updateConnVCU(DataTable.conn_VCU);
			
			JPRace.updateTimer(DataTable.time);
			
			JPRace.updateStatus(DataTable.state);
			
			JPRace.updateBatteryVoltage(DataTable.VCU_BatteryVoltage);
			JPRace.updateBatteryCurrent(DataTable.VCU_BatteryHighCurrent);
			//JPRace.callRepaint();
			
			
			break;
		
		case DEBUG_VCU:
		case DEBUG_BMS:
		case DEBUG_MTR:			//There are lots of place can optimize, not just here
			
			JPTabVCU.updateItem(0, "VCU Connectivity", String.valueOf(DataTable.conn_VCU));
			JPTabVCU.updateItem(1, "VCU Battery Voltage", String.valueOf(DataTable.VCU_BatteryVoltage) + " V");
			JPTabVCU.updateItem(2, "VCU Battery Low Current", String.valueOf(DataTable.VCU_BatteryLowVoltage) + " A");
			JPTabVCU.updateItem(3, "VCU Battery High Current", String.valueOf(DataTable.VCU_BatteryHighCurrent) + " A");
			
			
			JPTabBMS.updateItem(0, "BMS Connectivity", String.valueOf(DataTable.conn_BMS));
			JPTabBMS.updateItem(1, "BMS Battery Voltage", String.valueOf(DataTable.BMS_BatteryVoltage) + " V");
			JPTabBMS.updateItem(2, "BMS Battery Low Current", String.valueOf(DataTable.BMS_BatteryCurrent) + " A");
			

			JPTabMTR.updateItem(0, "MTR Connectivity", String.valueOf(DataTable.conn_MTR));
			JPTabMTR.updateItem(1, "MTR Battery Voltage", String.valueOf(DataTable.MTR_BatteryVoltage) + " V");
			JPTabMTR.updateItem(2, "MTR Battery Low Current", String.valueOf(DataTable.MTR_BatteryCurrent) + " A");


			break;
		
		
		default:
			break;
		
		}
		

		
		
		
		++DataTable.time;
		
		
	}
	
	
	
}
