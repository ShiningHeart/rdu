package View;

import java.awt.event.ActionEvent;

import javax.swing.*;

import Control.Listener.*;

public class MenuBarAppender {
	
	public static void appendMenuBar( JFrame jfWindow ) {
		
		
		JMenuBar menuBar = new JMenuBar();
		
		
		//Menu Contrl -----------------------------------------------------------------------------
        JMenu control = new JMenu("Control");
        
        
        JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener( (ActionEvent event) -> {
            System.exit(0);
        });
		
		
        
		
        control.add(exit);
        menuBar.add(control);
		
		
		//Menu Mode -----------------------------------------------------------------------------
        JMenu mode = new JMenu("Mode");
        
        ButtonGroup btnGroup = new ButtonGroup();

        JRadioButtonMenuItem btnRadioMenuItem1 = new JRadioButtonMenuItem("Race Mode");
        btnRadioMenuItem1.setSelected(true);
        btnRadioMenuItem1.addItemListener(new MenuModeSelectListener());

        JRadioButtonMenuItem btnRadioMenuItem2 = new JRadioButtonMenuItem("Debug Mode");
        btnRadioMenuItem2.addItemListener(new MenuModeSelectListener());

        JRadioButtonMenuItem btnRadioMenuItem3 = new JRadioButtonMenuItem("Sleep Mode");
        btnRadioMenuItem3.addItemListener(new MenuModeSelectListener());

        btnGroup.add(btnRadioMenuItem1);
        btnGroup.add(btnRadioMenuItem2);
        btnGroup.add(btnRadioMenuItem3);
        
        mode.add(btnRadioMenuItem1);
        mode.add(btnRadioMenuItem2);
        mode.add(btnRadioMenuItem3);
        
        menuBar.add(mode);
        
        
        //Menu About -----------------------------------------------------------------------------
        JMenu about = new JMenu("About");
        
        menuBar.add(about);
        
        
        
        //Pack
        jfWindow.setJMenuBar(menuBar);
        
		
		
		
		
		
		
	}
	
	
	

}
