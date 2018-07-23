package View;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import Model.* ;

import Model.SystemConfigTable;

public class JPRace extends JPanel{
	

	private static final long serialVersionUID = 1L;
	private static JPRace jpRace = null;
	private static boolean hasInit = false;
	
	private static MyLabel status, timer, con_en, con_run, con_mtr, con_bms, con_vcu, con_sock, battery, battery_v, battery_c, bms, bms_battery_capacity;

	private static JTextArea tf; 
	
	private static Graphics2D g2;
	private static Point topLeft = null, buttomRight = null; 
	
	
	
	
	
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
		
		jpRace.setLayout( null );
		jpRace.setBackground( GlobalConfiguration.THEME_COLOR );
		jpRace.setBounds(0, 0, JFMainWindow.getWidth(), JFMainWindow.getHeight() - SystemConfigTable.MENU_BAR_HEIGHT );
		
		// Graphic config
		topLeft = new Point( (int)(jpRace.getWidth() * 1.0 / GlobalConfiguration.GRID_HORIZONTAL_NUM * 5) ,
				(int)(jpRace.getHeight() * 1.0 / GlobalConfiguration.GRID_VERTICAL_NUM * 5));
		buttomRight = new Point( (int)(jpRace.getWidth() * 1.0 / GlobalConfiguration.GRID_HORIZONTAL_NUM * (GlobalConfiguration.GRID_HORIZONTAL_NUM - 5)) ,
				(int)(jpRace.getHeight() * 1.0 / GlobalConfiguration.GRID_VERTICAL_NUM * (GlobalConfiguration.GRID_VERTICAL_NUM - 5)));
		
		
		
		
		
		
		
		/*Design Phase --------------------------------------------------------------------------------*/
		
		//Add the SFU logo on top right
		int img_width = (int)(GlobalConfiguration.ratio_x * 8) , 
				img_height = (int)(GlobalConfiguration.ratio_y * 8);
		
		BufferedImage pic = null;
		try {
			pic = ImageIO.read(new File(GlobalConfiguration.TP_LOGO_PATH));
		} catch (IOException e) {
			
			System.err.println("JPRace.java: cannot access file " + GlobalConfiguration.TP_LOGO_PATH);
		}
		
		if(pic != null) {
			
			JLabel picLabel = new JLabel(new ImageIcon(pic.getScaledInstance(img_width, img_height,Image.SCALE_SMOOTH)));
			
			picLabel.setBounds( (int)(GlobalConfiguration.ratio_x * 6) , (int)(GlobalConfiguration.ratio_y * 6), img_width, img_height);
			
			jpRace.add(picLabel);
			
		}
		
		
		
		
		//Add the status text on top left
		
		status = new MyLabel("AAAAAAAA", JLabel.LEFT,
				new Rectangle((int)(GlobalConfiguration.ratio_x * 55.0 + GlobalConfiguration.ratio_x) ,
				(int)(GlobalConfiguration.ratio_y * 7.0),
				(int)(GlobalConfiguration.ratio_x * 22.0),
				(int)(GlobalConfiguration.ratio_y * 4.0)));
		
		
		status.setForeground(Color.GREEN);
		
		status.setText("UNLOCK");
		jpRace.add(status);
		
		
				
		//Add timer at the centre 
		
		timer = new MyLabel("00 : 00 : 00", JLabel.LEFT,
				new Rectangle((int)(GlobalConfiguration.ratio_x * 21.0) ,
						(int)(GlobalConfiguration.ratio_y * 13.0),
						(int)(GlobalConfiguration.ratio_x * 39.0),
						(int)(GlobalConfiguration.ratio_y * 8.0))
				);
		
		timer.setForeground(Color.WHITE);
		timer.setText("00 : 00 : 00");
		timer.setOpaque(false);
		
		jpRace.add(timer);
		
		
		//Add severity message box area at the bottom
		JTextArea tf = new JTextArea();
		tf.setText("Sev 1: (not implement for both visual and logic)\n\n\nSev 2: (not implement for both visual and logic)");
		
		//tf.setBorder(null);
		tf.setOpaque(false);
		tf.setEditable(false);
		
		tf.setBounds(
				(int)(GlobalConfiguration.ratio_x * 20.0) ,
				(int)(GlobalConfiguration.ratio_y * 28.0),
				(int)(GlobalConfiguration.ratio_x * 40.0),
				(int)(GlobalConfiguration.ratio_y * 15.0)
				);
		
		tf.setForeground(Color.ORANGE);
		tf.setFont( new Font("Dialog", Font.PLAIN, 16) );
		
		jpRace.add(tf);
		
		
		
		//Add throttle in the middle
		
		
		int img_width2 = (int)(GlobalConfiguration.ratio_x * 39.0) , 
				img_height2 = (int)(GlobalConfiguration.ratio_y * 3.0);
		
		BufferedImage pic2 = null;
		try {
			pic2 = ImageIO.read(new File(GlobalConfiguration.THROTTLE_ICON_PATH));
		} catch (IOException e) {
			
			System.err.println("JPRace.java: cannot access file " + GlobalConfiguration.THROTTLE_ICON_PATH);
		}
		
		if(pic2 != null) {
			
			JLabel picLabel2 = new JLabel(new ImageIcon(pic2.getScaledInstance(img_width2, img_height2, Image.SCALE_SMOOTH)));
			
			picLabel2.setBounds( (int)(GlobalConfiguration.ratio_x * 21) , (int)(GlobalConfiguration.ratio_y * 22) , img_width2, img_height2);
			
			jpRace.add(picLabel2);
			
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		//Add connectivity info on the left side

		double base = 16.0;
		double shift = 4.2;
		
		con_en = new MyLabel("MD_EN", JLabel.RIGHT,
				new Rectangle((int)(GlobalConfiguration.ratio_x * 60.0) ,
						(int)(GlobalConfiguration.ratio_y * base),
						(int)(GlobalConfiguration.ratio_x * 15.0),
						(int)(GlobalConfiguration.ratio_y * 3.0))
				);
		
		con_en.setForeground(Color.RED);
		con_en.setText("MD_EN");				
		
		jpRace.add(con_en);
		
	
		
		
		con_run = new MyLabel("MD_RUN", JLabel.RIGHT,
				new Rectangle((int)(GlobalConfiguration.ratio_x * 60.0) ,
						(int)(GlobalConfiguration.ratio_y * (base + shift)),
						(int)(GlobalConfiguration.ratio_x * 15.0),
						(int)(GlobalConfiguration.ratio_y * 3.0))
				);
		
		con_run.setForeground(Color.RED);		
		
		jpRace.add(con_run);
		
		
		
		con_mtr = new MyLabel("MOTOR", JLabel.RIGHT,
				new Rectangle((int)(GlobalConfiguration.ratio_x * 60.0) ,
						(int)(GlobalConfiguration.ratio_y * (base + shift * 2)),
						(int)(GlobalConfiguration.ratio_x * 15.0),
						(int)(GlobalConfiguration.ratio_y * 3.0))
				);
		
		con_mtr.setForeground(Color.RED);
		
		jpRace.add(con_mtr);
		
		
		
		
		con_bms = new MyLabel("BMS", JLabel.RIGHT,
				new Rectangle((int)(GlobalConfiguration.ratio_x * 60.0) ,
						(int)(GlobalConfiguration.ratio_y * (base + shift * 3)),
						(int)(GlobalConfiguration.ratio_x * 15.0),
						(int)(GlobalConfiguration.ratio_y * 3.0))
				);
		
		con_bms.setForeground(Color.RED);		
		
		jpRace.add(con_bms);
		
		
		
		
		con_vcu = new MyLabel("VCU", JLabel.RIGHT,
				new Rectangle((int)(GlobalConfiguration.ratio_x * 60.0) ,
						(int)(GlobalConfiguration.ratio_y * (base + shift * 4)),
						(int)(GlobalConfiguration.ratio_x * 15.0),
						(int)(GlobalConfiguration.ratio_y * 3.0))
				);
		
		con_vcu.setForeground(Color.RED);
		
		jpRace.add(con_vcu);
		
		
		
		
		con_sock = new MyLabel("TCP/IP", JLabel.RIGHT,
				new Rectangle((int)(GlobalConfiguration.ratio_x * 60.0) ,
						(int)(GlobalConfiguration.ratio_y * (base + shift * 5)),
						(int)(GlobalConfiguration.ratio_x * 15.0),
						(int)(GlobalConfiguration.ratio_y * 3.0))
				);
		
		con_sock.setForeground(Color.RED);
		
		jpRace.add(con_sock);
		
		
		
		
		
		
		
		
		//Add BMS info on the right side
		
		double bms_base = 18.0;
		double bms_shift = 3.8;
		
		
		bms = new MyLabel("BMS", JLabel.LEFT,
				new Rectangle((int)(GlobalConfiguration.ratio_x * 6.0) ,
						(int)(GlobalConfiguration.ratio_y * (bms_base)),
						(int)(GlobalConfiguration.ratio_x * 11.0),
						(int)(GlobalConfiguration.ratio_y * 3.0))
				);
		
		bms.setForeground(Color.PINK);
		
		jpRace.add(bms);
		
		
		
		int img_width1 = (int)(GlobalConfiguration.ratio_x * 7) , 
				img_height1 = (int)(GlobalConfiguration.ratio_y * 3);
		
		BufferedImage pic1 = null;
		try {
			pic1 = ImageIO.read(new File(GlobalConfiguration.BATTERY_CAPACITY_ICON_PATH));
		} catch (IOException e) {
			
			System.err.println("JPRace.java: cannot access file " + GlobalConfiguration.BATTERY_CAPACITY_ICON_PATH);
		}
		
		if(pic1 != null) {
			
			JLabel picLabel1 = new JLabel(new ImageIcon(pic1.getScaledInstance(img_width1, img_height1,Image.SCALE_SMOOTH)));
			
			picLabel1.setBounds( (int)(GlobalConfiguration.ratio_x * 6) , (int)(GlobalConfiguration.ratio_y * (bms_base + bms_shift)), img_width1, img_height1);
			
			jpRace.add(picLabel1);
			
		}
		
		
		
		
		bms_battery_capacity = new MyLabel("100%", JLabel.LEFT,
				new Rectangle((int)(GlobalConfiguration.ratio_x * 14.0) ,
						(int)(GlobalConfiguration.ratio_y * (bms_base + shift - 0.5)),
						(int)(GlobalConfiguration.ratio_x * 6.0),
						(int)(GlobalConfiguration.ratio_y * 3.0))
				);
		
		bms_battery_capacity.setForeground(Color.white);
		bms_battery_capacity.setText("0%");
		
		jpRace.add(bms_battery_capacity);
		
		
		
		
		
		
		
		//Add battery info on the right side
		
		double vcu_base = 30.0;
		double vcu_shift = 3.8;
		
		battery = new MyLabel("VCU", JLabel.LEFT,
				new Rectangle((int)(GlobalConfiguration.ratio_x * 6.0) ,
						(int)(GlobalConfiguration.ratio_y * (vcu_base)),
						(int)(GlobalConfiguration.ratio_x * 11.0),
						(int)(GlobalConfiguration.ratio_y * 3.0))
				);
		
		battery.setForeground(Color.PINK);
		
		jpRace.add(battery);
		
		
		

		battery_v = new MyLabel("999.99 V", JLabel.LEFT,
				new Rectangle((int)(GlobalConfiguration.ratio_x * 6.0) ,
						(int)(GlobalConfiguration.ratio_y * (vcu_base + vcu_shift)),
						(int)(GlobalConfiguration.ratio_x * 11.0),
						(int)(GlobalConfiguration.ratio_y * 3.0))
				);
		
		battery_v.setForeground(Color.white);
		battery_v.setText("0 V");
		
		jpRace.add(battery_v);
		

		battery_c = new MyLabel("999.99 A", JLabel.LEFT,
				new Rectangle((int)(GlobalConfiguration.ratio_x * 6.0) ,
						(int)(GlobalConfiguration.ratio_y * (vcu_base + vcu_shift * 2)),
						(int)(GlobalConfiguration.ratio_x * 11.0),
						(int)(GlobalConfiguration.ratio_y * 3.0))
				);
		
		battery_c.setForeground(Color.WHITE);
		
		jpRace.add(battery_c);
		
		
		
	}
	
	
	private Color th_low = new Color(161, 188, 224);
	private Color th_high = new Color(51, 196, 58);
	
	private static Point th_pos = new Point( (int)(GlobalConfiguration.ratio_x * 21.5) , (int)(GlobalConfiguration.ratio_y * 22.5) );
	//private static Point th_pos = new Point( (int)(GlobalConfiguration.ratio_x * 21.5) , 100 );
	private static int th_height = (int)(GlobalConfiguration.ratio_y * 1.8);
	
	private static Point bat_pos = new Point( (int)(GlobalConfiguration.ratio_x * 6.5) , (int)(GlobalConfiguration.ratio_y * 22.5) );
	private static int bat_height = (int)(GlobalConfiguration.ratio_y * 1.5);
	
	
	@Override
	  protected void paintComponent(Graphics g) {
		
/*
	    Graphics2D item = (Graphics2D)this.getGraphics();
	    if( DataTable.Throttle_input > 50 ) {
	    	item.setColor(th_high);
	    }else {
	    	item.setColor( th_low );
	    }
	    
	    item.fillRect(th_pos.x, th_pos.y, (int)( GlobalConfiguration.ratio_y * 38.8 / 100 *  DataTable.Throttle_input  )  , th_height);
	    */
		
		
		
	    super.paintComponent(g);

	    if(topLeft != null && buttomRight != null) {
	    
	    	g.setColor( Color.WHITE );
	    	
	    	g2 = (Graphics2D) g;
		    g2.setStroke(new BasicStroke(1));
		    
		    g2.drawLine(topLeft.x , topLeft.y, buttomRight.x, topLeft.y);
		    g2.drawLine(topLeft.x , topLeft.y, topLeft.x, buttomRight.y);
		    g2.drawLine(buttomRight.x , topLeft.y, buttomRight.x, buttomRight.y);
		    g2.drawLine(topLeft.x , buttomRight.y, buttomRight.x, buttomRight.y);
		    

		    
	    }
	    
		
	    if( DataTable.Throttle_input > 50 ) {
	    	
	    	g.setColor(th_high);
	    }else {
	    	g.setColor( th_low );
	    }
	    
	    g.fillRect((int)th_pos.getX(), (int)th_pos.getY(), (int)( GlobalConfiguration.ratio_y * 38.8 / 100.0 *  DataTable.Throttle_input  )  , th_height);
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	  }
	
	
	//@Override
	public void paint(Graphics g) {
		/*
		Graphics item = this.getGraphics();
		
		if( item == null) {
			return;
		}
		
	    if( DataTable.Throttle_input > 50 ) {
	    	
	    	item.setColor(th_high);
	    }else {
	    	item.setColor( th_low );
	    }
	    
	    item.fillRect((int)th_pos.getX(), (int)th_pos.getY(), (int)( GlobalConfiguration.ratio_y * 38.8 / 100.0 *  DataTable.Throttle_input  )  , th_height);
		*/
		
		super.paint(g);
		
		
		
		Graphics item = this.getGraphics();
		
		if( item == null) {
			return;
		}
		item.setColor( GlobalConfiguration.THEME_COLOR );
		item.fillRect((int)th_pos.getX(), (int)th_pos.getY(), (int)( GlobalConfiguration.ratio_y * 38.8 )  , th_height);
		
	    if( DataTable.Throttle_input > 50 ) {
	    	
	    	item.setColor(th_high);
	    }else {
	    	item.setColor( th_low );
	    }
	    
	    item.fillRect((int)th_pos.getX(), (int)th_pos.getY(), (int)( GlobalConfiguration.ratio_y * 38.8 / 100.0 *  DataTable.Throttle_input  )  , th_height);
	    
	    
	    
	    item.setColor( GlobalConfiguration.THEME_COLOR );
	    item.fillRect((int)bat_pos.getX(), (int)bat_pos.getY(), (int)( GlobalConfiguration.ratio_y * 6.0 )  , bat_height);
	    
	    item.setColor( th_high );
	    item.fillRect((int)bat_pos.getX(), (int)bat_pos.getY(), (int)( GlobalConfiguration.ratio_y * 6.0 / 100.0 * DataTable.BMS_BatteryCapacity )  , bat_height);
	    
	    
	    
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
	
	
	public static void updateBatteryCapacity(int val) {
		bms_battery_capacity.setText(val + "%");			//Validity check at data refresher
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
