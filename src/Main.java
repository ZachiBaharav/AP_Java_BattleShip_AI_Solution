/**
 *
 * @author Zachi
 */

import javax.swing.JFrame;


public class Main 
{
	
	public static void main(String[] args)
	{
		final int DISPLAY_WIDTH = 600 ;
		final int DISPLAY_HEIGHT = 680 ;
		
		JFrame frame = new JFrame();
		
		frame.setSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
		frame.setTitle("Battle Ship AI Challenge");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.setLayout(null); // we are using absolute location
		
		Display display = new Display(DISPLAY_WIDTH, DISPLAY_HEIGHT);
		
		frame.add(display);				
		frame.setVisible(true);
		
	}
	
	
}
