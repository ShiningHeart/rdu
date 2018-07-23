package View;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JLabel;

import Model.GlobalConfiguration;


public class MyLabel extends JLabel {

	private static final long serialVersionUID = 1L;
	
	private static final int MIN_FONT_SIZE = 3;
    private static final int MAX_FONT_SIZE = 100;
    
    private Rectangle bounds = null;
    Graphics g;
    
    public MyLabel(String text, int align ,Rectangle bounds) {
        super(text);
        this.bounds = bounds;
        this.setHorizontalAlignment(align);
        init();
    }
 
    private void init() {
    	
    	
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                adaptLabelFont( MyLabel.this );
            }
        });
        
        this.setBounds( bounds );
        
    }
 
    private void adaptLabelFont(MyLabel l) {
    	
        if (g == null) {
        	System.out.println("Font error, later will move to sev msg area");
            return;
        }
        
        Rectangle r = l.getBounds();
        
        
        int fontSize = MIN_FONT_SIZE;
        
        Font f = new Font("Dialog", Font.BOLD, fontSize );
 
        Rectangle r1 = new Rectangle();
        Rectangle r2 = new Rectangle();
        
        r1.setLocation( (int)(this.bounds.getLocation().getX()), (int)(bounds.getLocation().getY()) );
    	r2.setLocation( (int)(this.bounds.getLocation().getX()), (int)(bounds.getLocation().getY()) );
        
        while (fontSize < MAX_FONT_SIZE) {
            
        	r1.setSize( getTextSize(l, f.deriveFont(f.getStyle(), fontSize)));
            r2.setSize( getTextSize(l, f.deriveFont(f.getStyle(),fontSize + 1)));
        	
            if (r.contains(r1) && ! r.contains(r2)) {
                break;
            }
            
            fontSize++;
        }
        
 
        setFont( f.deriveFont(f.getStyle(),fontSize) );
        repaint();
    }
 
    private Dimension getTextSize(JLabel l, Font f) {
    	
        Dimension size = new Dimension();
        g.setFont(f);
        FontMetrics fm = g.getFontMetrics(f);
        size.width = fm.stringWidth(l.getText());
        size.height = fm.getHeight();
 
        return size;
    }
 
    protected void paintComponent(Graphics g) {
        
    	super.paintComponent(g);
        this.g = g;
    }


}
