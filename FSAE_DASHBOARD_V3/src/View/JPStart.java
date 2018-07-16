package View;


import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import Model.*;

public class JPStart{

	private static JPanel jpStart = null;
	private static boolean hasInit = false;
	
	private JPStart() {}
	
	public static JPanel getInstance() {
		
		if( hasInit == false ) {
			
			init();
			hasInit = true;
			
		}
		
		return jpStart;
	}
	
	
	private static void init() {
		
		jpStart = new JPanel();
		
		jpStart.setLayout(null);
		jpStart.setBounds(0, 0, SystemDataTable.SCREEN_WIDTH, SystemDataTable.SCREEN_HEIGHT);
		
		jpStart.setBackground(Color.WHITE);
		
		BufferedImage pic = null;
		
		try {
			pic = ImageIO.read(new File("resource/image/sfu-logo.jpg"));
		} catch (IOException e) {
			
			System.out.println("JPStart.java: initialize image file failed");
			System.exit(-1);
			
		}
		
		JLabel picLabel = new JLabel(new ImageIcon(pic.getScaledInstance(SystemDataTable.SCREEN_WIDTH, SystemDataTable.SCREEN_HEIGHT,Image.SCALE_SMOOTH)));
		picLabel.setBounds(0, 0, SystemDataTable.SCREEN_WIDTH, SystemDataTable.SCREEN_HEIGHT);
		
		
		jpStart.add(picLabel);
		
	}
	
	
	
	

}
