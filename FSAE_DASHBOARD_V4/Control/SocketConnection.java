package Control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import Model.*;


public class SocketConnection extends Thread{
	
	
	private ServerSocket sock_server = null;
	private Socket sock_client = null;
	private BufferedReader input;
	private PrintWriter output;
	private String readLine = "";
	private int readByte;
	
	//NOTES : 
	// 1. IPv4 only
	// 2. One client only
	public void init( int port ) throws Exception  {
		
		sock_server = new ServerSocket(port);		//Will not check the validity of address
		sock_client = sock_server.accept();
		
		input =  new BufferedReader(new InputStreamReader(sock_client.getInputStream()));
		output = new PrintWriter(sock_client.getOutputStream(), true);
		
		if(input == null || output == null) {
			throw new Exception();
		}
		
	}
	
	
	public void handler() throws Exception{
		
		int st = 0;
		
		while(true) {
			
			readLine = input.readLine();
			DataTable.Throttle_input = (int)(Float.parseFloat(readLine) / 5 * 100);
			
			
			
			
			readLine = input.readLine();
			DataTable.VCU_BatteryVoltage = Float.parseFloat(readLine);
			
			readLine = input.readLine();
			DataTable.VCU_BatteryLowCurrent = Float.parseFloat(readLine);
			
			
			readLine = input.readLine();
			st = Integer.parseInt(readLine);
			switch(st) {
			
			case 0:
				DataTable.state = "LOCKED";
				break;
			case 1:
				DataTable.state = "UNLOCKED";
				break;
			case 2:
				DataTable.state = "STANDBY";
				break;
			case 3:
				DataTable.state = "PRECHARGE";
				break;
			case 4:
				DataTable.state = "HIGH_VOLTAGE";
				break;
			case 5:
				DataTable.state = "RUNNING";
				break;
			case 6:
				DataTable.state = "SHUTDOWN";
				break;
			default:
					DataTable.state = "ERROR";	
			
			}
			
			
			readLine = input.readLine();
			DataTable.MD_EN =  (Integer.parseInt(readLine) == 0 ) ? false : true;
			
			readLine = input.readLine();
			DataTable.MD_RUN =  (Integer.parseInt(readLine) == 0 ) ? false : true;
			
		}
		
		
		
	}
	
	
	
	
	public void run() {
		try {
			
			init( GlobalConfiguration.SOCK_PORT );
			handler();
			
			
		}catch(Exception ex) {
			
			System.err.println("Socket error, exit for this version");
			return ;
			
		}finally {
			
			try {
				sock_server.close();
				sock_client.close();
            } catch (IOException e) {
                System.err.println("Close socket unproperly");
            }
			
			
			
		}
		
		
		
		
		
		
		
		
	}
	
	
	

}
