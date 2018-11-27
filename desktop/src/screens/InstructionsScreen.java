package screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class InstructionsScreen extends JFrame implements KeyListener{
	
	private Font font = new Font("TimesRoman", Font.BOLD, 13);
	
	/**
	 * This class is used to explain the user how to
	 * control the character on the screen
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ImageIcon img;

	public InstructionsScreen() {
		
		/*
		 * Set all configurations for this screen 
		 * in the constructor
		 * The caller will only define visibility to true
		 */
		
		this.addKeyListener(this);
		this.setLayout(new GridLayout(4 , 2 , 10 , 10));
		this.setTitle(" Instructions ");
		this.getContentPane().setBackground(Color.BLACK);
		this.setFocusable(true);
		this.setResizable(false);
		this.setSize(550, 600);
		this.setLocationRelativeTo(null); // Center the frame
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		/*
		 * Create the icon for the labels using 
		 * the attribute img and scaling them 
		 * to a smaller size.
		 * These labels use html formatting in order to break the line.
		 */
		
		//First row
		img = new ImageIcon(new ImageIcon("images/instructions_screen/press_enter.png").getImage().getScaledInstance(200, 160, Image.SCALE_DEFAULT));
		this.add(createLabels("<html><p align='center'>Press key <u>ENTER</u> to start. Press key <u>UP</u> to allow your bird to fly and to start the game.</p> </html>"));
		this.add(new JLabel(img));
		
		//Second row
		img = new ImageIcon(new ImageIcon("images/instructions_screen/middle_pipes.png").getImage().getScaledInstance(200, 160, Image.SCALE_DEFAULT));
		this.add(createLabels("<html><p align='center'>Stay in the middle of the pipes. This is the main objective of the game. If you hit a pipe or reach the screen borders, the game ends. The farther you go the faster the bird flies.</p></html>"));
		this.add(new JLabel(img));
		
		//Third row
		img = new ImageIcon(new ImageIcon("images/instructions_screen/pause_game.png").getImage().getScaledInstance(200, 160, Image.SCALE_DEFAULT));
		this.add(createLabels("<html><p align='center'>To pause the game, press key <u>ESC</u>. This will give the option to resume the game or quit, press kesy <u>UP</u> and <u>DOWN</u> to navigate between them and key <u>ENTER</u> to select.</p></html>"));
		this.add(new JLabel(img));
		
		//Fourth row
		img = new ImageIcon(new ImageIcon("images/instructions_screen/game_over.png").getImage().getScaledInstance(200, 160, Image.SCALE_DEFAULT));
		this.add(createLabels("<html><p align='center'>If the game is over, in order to restart, press key <u>HOME</u>. The score will be set to zero and the bird will go back to the initial position.</p></html>"));
		this.add(new JLabel(img));
		
		
		this.addWindowFocusListener(new WindowFocusListener() {
			
			//Close window when focus of the window is lost
			
			@Override
			public void windowLostFocus(WindowEvent arg0) {
				
				terminateWindow();
			}
			
			@Override
			public void windowGainedFocus(WindowEvent arg0) {
				
			}
		});
		
	}
	
	//Close window
	private void terminateWindow() {
		
		this.dispose();
	}
	
	
	/*
	 * Create customized labels for
	 * the instructions defining borders,
	 * color and font
	 */
	private JLabel createLabels(String content) {
		
		JLabel labels = new JLabel(content);
		labels.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));
		labels.setForeground(Color.WHITE);
		labels.setFont(font);
		return labels;
	}
	

	@Override
	public void keyPressed(KeyEvent key) {
		// TODO Auto-generated method stub
		
		switch(key.getKeyCode()) {
		
		
			case (KeyEvent.VK_ESCAPE):
				
				terminateWindow();
				
				break;
		}
		
		
	}

	@Override
	public void keyReleased(KeyEvent key) {
		
		//Do nothing 
		
	}

	@Override
	public void keyTyped(KeyEvent key) {
		
		//Do nothing
		
	}

}
