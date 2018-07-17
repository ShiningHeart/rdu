package View;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;


import Model.SystemDataTable;

public class JPRace extends JPanel{
	

	private static final long serialVersionUID = 1L;
	private static JPRace jpRace = null;
	private static boolean hasInit = false;
	
	private static JLabel status, timer, con_mtr, con_bms, con_vcu, battery_v, battery_c;
	private static JTextArea tf; 
	
	
	
	
	
	
	
	
	
	
	private JPRace() { super(); }
	
	public static JPRace getInstance() {
		
		if( hasInit == false ) {
			
			init();
			hasInit = true;
			
		}
		
		return jpRace;
	}
	
	
	private static void init() {
		
		jpRace = new JPRace();
		
		jpRace.setLayout(null);
		jpRace.setBackground( new Color(35, 8, 122) );
		jpRace.setBounds(0, 0, SystemDataTable.SCREEN_WIDTH, SystemDataTable.SCREEN_HEIGHT);
		
		
		
		
		
		
		/*Design Phase --------------------------------------------------------------------------------*/
		
		//Add the SFU logo on top right
		int img_width = 70, img_height = 90;				//Size comes from design file
		int img_x = 35, img_y = 30;
		
		BufferedImage pic = null;
		try {
			pic = ImageIO.read(new File("resource/image/race-sfuLogo.png"));
		} catch (IOException e) {
			
			System.out.println("JPRace.java: initialize image file failed");
			
		}
		
		JLabel picLabel = new JLabel(new ImageIcon(pic.getScaledInstance(img_width, img_height,Image.SCALE_SMOOTH)));
		picLabel.setBounds(img_x, img_y, img_width, img_height);
		
		jpRace.add(picLabel);
		
		
		//Add the status text on top left
		status = new JLabel();
		status.setBounds(625, 28, 140, 42);
		status.setForeground(Color.GREEN);
		status.setFont( new Font("Dialog", Font.BOLD, 28) );
		 
		status.setText("UNLOCK");
		
		jpRace.add(status);
		
				
		//Add timer at the centre 
		
		timer = new JLabel();
		timer.setBounds(208, 114, 433, 135);
		timer.setForeground(Color.white);
		timer.setFont( new Font("Dialog", Font.BOLD, 58) );
		
		timer.setText("00 : 00 : 00");
		
		jpRace.add(timer);
		
		
		//Add severity message box area at the bottom
		JTextArea tf = new JTextArea();
		tf.setText("(no messages)");
		
		//tf.setBorder(null);
		tf.setOpaque(false);
		tf.setEditable(false);
		tf.setBounds(195, 278, 451, 173);
		tf.setForeground(Color.ORANGE);
		tf.setFont( new Font("Dialog", Font.PLAIN, 16) );
		
		jpRace.add(tf);
		
		
		//Add connectivity info on the left side
		con_mtr = new JLabel();
		con_mtr.setBounds(685, 187, 433, 37);
		con_mtr.setForeground(Color.RED);
		con_mtr.setFont( new Font("Dialog", Font.BOLD, 25) );
		con_mtr.setText("MTR");
		
		jpRace.add(con_mtr);
		
		con_bms = new JLabel();
		con_bms.setBounds(685, 252, 300, 37);
		con_bms.setForeground(Color.RED);
		con_bms.setFont( new Font("Dialog", Font.BOLD, 25) );
		con_bms.setText("BMS");
		
		jpRace.add(con_bms);
		
		con_vcu = new JLabel();
		con_vcu.setBounds(685, 314, 300, 37);
		con_vcu.setForeground(Color.RED);
		con_vcu.setFont( new Font("Dialog", Font.BOLD, 25) );
		con_vcu.setText("VCU");
		
		jpRace.add(con_vcu);
		
		
		
		//Add battery info on the right side
		
		battery_v = new JLabel();
		battery_v.setBounds(30, 227, 131, 41);
		battery_v.setForeground(Color.GREEN);
		battery_v.setFont( new Font("Dialog", Font.BOLD, 25) );
		battery_v.setText("0.0 V");
		
		jpRace.add(battery_v);
		
		battery_c = new JLabel();
		battery_c.setBounds(30, 304, 131, 41);
		battery_c.setForeground(Color.GREEN);
		battery_c.setFont( new Font("Dialog", Font.BOLD, 25) );
		battery_c.setText("0.0 A");
		
		jpRace.add(battery_c);
		
		
		
	}
	
	
	
	
	@Override
	  protected void paintComponent(Graphics g) {
	    super.paintComponent(g);

	    g.setColor( Color.WHITE );
	    //.setStroke(new BasicStroke(2));
	    
	    
	    Graphics2D g2 = (Graphics2D) g;
	    g2.setStroke(new BasicStroke(1));
	    
	    g2.drawLine(20, 20, 20, 460);
	    g2.drawLine(20, 20, 780, 20);
	    g2.drawLine(20, 460, 780, 460);
	    g2.drawLine(780, 20, 780, 460);
	    
	    
	  }
	
	
	public static void updateStatus(String str) {
		
		//I will not check anything, such like null or str is not proper string and so on, no need if you are careful
		status.setText(str);
		
	}
	
	public static void updateBatteryVoltage(float v) {
		battery_v.setText( String.valueOf(v) + " V" );
	}
	
	public static void updateBatteryCurrent(float c) {
		battery_c.setText( String.valueOf(c) + " A");
	}
	
	
	public static void updateConnVCU(boolean b) {
		con_vcu.setForeground( (b) ? Color.GREEN : Color.RED);
	}
	
	public static void updateConnBMS(boolean b) {
		con_bms.setForeground( (b) ? Color.GREEN : Color.RED);
	}
	
	public static void updateConnMTR(boolean b) {
		con_mtr.setForeground( (b) ? Color.GREEN : Color.RED);
	}
	
	public static void updateTimer(long l) {
		
		long min = 0, sec = 0, mili = 0;
		
		mili = l % 100;
		sec = l / 100 % 60;
		min = l / 6000;
		
		timer.setText( (( min < 10) ? ("0" + min) : String.valueOf(min) ) + " : " + 
						((sec < 10) ? ("0" + sec) : String.valueOf(sec)) + " : " + 
						((mili < 10) ? ("0" + mili) : String.valueOf(mili))
				);
		//System.out.println("I am calling");
	}
	
	public static void updateMsg(String str) {
		
		tf.insert(str + "\n", 0);
		
	}

	public static void callRepaint() {
		jpRace.repaint();
	}
	
	
}
