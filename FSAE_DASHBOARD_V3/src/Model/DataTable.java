package Model;

public class DataTable {

	
	//GLOBAL
	public static long time = 0;
	public static String state = "UNLOCK";
	
	
	
	
	
	
	
	
	
	//VCU
	public static boolean conn_VCU = false;
	public static float VCU_BatteryVoltage = 0;
	public static float VCU_BatteryHighCurrent = 0;
	public static float VCU_BatteryLowVoltage = 0;
	
	
	//BMS
	public static boolean conn_BMS = false;
	public static float BMS_BatteryVoltage = 0;
	public static float BMS_BatteryCurrent = 0;
	
	
	//MTR
	public static boolean conn_MTR = false;
	public static float MTR_BatteryVoltage = 0;
	public static float MTR_BatteryCurrent = 0;
	
	
	
	
	
	
}
