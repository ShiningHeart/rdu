package Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketConnection extends Thread{
	
	
	private Socket sock = null;
	private BufferedReader input;
	
	
	//IPv4 Only, if need dual stack, tell me
	public void init( String addr, int port ) throws UnknownHostException, IOException  {
		
		sock = new Socket(addr, port);		//Will not check the validity of address
		
        input = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		
        //String answer = input.readLine();

		
	}
	
	public void run() {
		try {
			
			init("127.0.0.1", 12345);		//
			
		}catch(Exception ex) {
			
			return;
		}
		
		while( true ) {
			
			
			
			
			
			
			
		}
		
		
	}
	
	
	

}
