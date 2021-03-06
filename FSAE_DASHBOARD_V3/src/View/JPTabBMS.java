package View;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Model.SystemDataTable;

public class JPTabBMS {


	private static JPanel jpTabBMS = new JPanel();
	private static boolean hasInit = false;
	

	private static JButton btn_prev, btn_next;
	private static JLabel[] tags, vals;
	
	private static int item_count = 3;
	
	
	private JPTabBMS() {}
	
	
	public static JPanel getInstance() {
		
		if( hasInit == false ) {
			
			init();
			hasInit = true;
			
		}
		
		return jpTabBMS;
		
	}
	
	
	
	private static void init() {

		
		jpTabBMS.setLayout(null);
		jpTabBMS.setBackground( new Color(35, 8, 122) );
		jpTabBMS.setBounds(0, 0, SystemDataTable.SCREEN_WIDTH, SystemDataTable.SCREEN_HEIGHT);		//REVISIT
		
		
		/*Design Phase --------------------------------------------------------------------------------*/
		
		//Add 7 labels on each display
		
		tags = new JLabel[7];
		
		for(int i = 0; i < 7; ++i) {
			tags[i] = new JLabel();
			tags[i].setFont(new Font("Dialog", Font.PLAIN, 22) );
			tags[i].setForeground(Color.white);
			tags[i].setBounds(48, 42 + 31 * i + i * 20, 335, 31);
			tags[i].setText(""); 
			jpTabBMS.add(tags[i]);
		}
		
		vals = new JLabel[7];
		
		for(int i = 0; i < 7; ++i) {
			
			vals[i] = new JLabel();
			vals[i].setFont(new Font("Dialog", Font.ITALIC, 20) );
			vals[i].setForeground(Color.white);
			vals[i].setBounds(486, 42 + 31 * i + i * 20, 222, 31);
			vals[i].setText("");
			jpTabBMS.add(vals[i]);
		}
		
		
		
			//Add 2 buttons
				btn_prev = new JButton();
				btn_prev.setText("Prev");
				btn_prev.setBounds(481, 414, 92, 25);
				//btn_prev.setBorder( BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()) );
				btn_prev.setOpaque(false);
				
				
				jpTabBMS.add(btn_prev);
				
				
				btn_next = new JButton();
				btn_next.setText("Next");
				btn_next.setBounds(623, 414, 92, 22);
				//btn_next.setBorder( BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()) );
				btn_next.setOpaque(false);
				
				jpTabBMS.add(btn_next);
				
				if(item_count <= 7) {
					btn_prev.setEnabled(false);
					btn_next.setEnabled(false);
				}
				
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
		
public static void updateItem(int num, String key, String val) {
		
		tags[num].setText(key);
		vals[num].setText(val);
		
	}
		
	
}
