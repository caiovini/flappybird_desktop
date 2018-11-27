package screens;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import Database.GameDAO;
import Layouts.ModifiedFlowLayout;
import Models.GameConfiguration;
import screens.InitialScreen;

import com.applicationdevelopment.flappybird.FlappyBird;

public class InitialScreen extends JFrame implements KeyListener{
	
	
	/**
	 * This class shows the initial
	 * screen where user will have the options
	 * New game, Instructions and Configurations.
	 */
	private static final long serialVersionUID = 1L;
	
	private JButton newGameButton;
	private JButton configurationsGameButton;
	private JButton instructionsButton;
	private JButton myInfo;
	
	private Font font = new Font("TimesRoman", Font.BOLD, 15);
	
	private String playerName;
	private final int FLAG_MUTE = 2;
	private final int VOLUME = 3;
	private final int BRIGHTNESS = 4;

	public InitialScreen(String playerName ) {
		
		//Set initial settings
		addKeyListener(this);
		this.setLayout(new BorderLayout());
		this.setTitle(" Initial screen ");
		this.setFocusable(true);
		this.playerName = playerName;
		
		//Set background image
		JLabel background = new JLabel(new ImageIcon("images/initial_screen/background.gif"));
		
		/*
		 * ModifiedFlowLayout is a type of FlowLayout 
		 * modified to show components in different rows 
		 * instead of side by side. 
		 */
		background.setLayout(new ModifiedFlowLayout(FlowLayout.CENTER , 200 , 10));
		
		
		/*
		 * Add buttons and set border 
		 * initially for new game button only 	
		 * The Border will be used to fetch which
		 * option has been chosen by the user
		 * when one presses key ENTER
		 */
		newGameButton = createButtons("NEW GAME");
		newGameButton.setBorderPainted(true);
		
		
		configurationsGameButton = createButtons("CONFIGURATIONS");
		instructionsButton = createButtons("INSTRUCTIONS");
		myInfo = createButtons("MY INFO");
		
		//Add component buttons
		background.add(newGameButton , BorderLayout.NORTH);
		background.add(configurationsGameButton);
		background.add(instructionsButton);
		background.add(myInfo);
	
		
		this.add(background); //Add background image using label	
		this.setSize(550, 400);
		this.setLocationRelativeTo(null); // Center the frame
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	public void startNewGame() {
		
		GameDAO database = new GameDAO();
		String[] configGame = database.getPlayer(this.playerName).split(";"); 
		
		GameConfiguration gameConfiguration = new GameConfiguration(configGame[FLAG_MUTE].charAt(0) , 
                								   Integer.parseInt(configGame[VOLUME]) ,
                								   Integer.parseInt(configGame[BRIGHTNESS]));
		
		//Start game flappy bird
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new FlappyBird(gameConfiguration , this.playerName), config);
		this.dispose();
	}
	
	//Create customized buttons
	public JButton createButtons(String name) {
		
		JButton button = new JButton(name);		
		button.setOpaque(false);
		button.setBorder(BorderFactory.createLineBorder(Color.YELLOW));
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.setBorderPainted(false);
		button.setFocusable(false);
		button.setFocusPainted(false);
		button.setFont(font);
		
		return button;
	}
	
	//Close the screen
	private void terminateWindow() {
		
		System.exit(0);
	}

	@Override
	public void keyPressed(KeyEvent key) {
		
		
		/*
		 * Listener for key pressed by the user
		 * Check if user is pressing key UP or DOWN
		 * and set the painted border according to the previous button painted 
		 */
		
		switch(key.getKeyCode()) {
			
			
			case (KeyEvent.VK_UP):
				
				if(newGameButton.isBorderPainted()){
					
					newGameButton.setBorderPainted(false);
					myInfo.setBorderPainted(true);
					
				}else if(configurationsGameButton.isBorderPainted()){
					
					configurationsGameButton.setBorderPainted(false);
					newGameButton.setBorderPainted(true);
					
				}else if(instructionsButton.isBorderPainted()){
					
					instructionsButton.setBorderPainted(false);
					configurationsGameButton.setBorderPainted(true);
					
				}else if(myInfo.isBorderPainted()) {
					
					myInfo.setBorderPainted(false);
					instructionsButton.setBorderPainted(true);
					
				}else {
					
					//Do nothing
				}
				
			
				break;	
			
			case (KeyEvent.VK_DOWN):
				
				if(newGameButton.isBorderPainted()){
					
					newGameButton.setBorderPainted(false);
					configurationsGameButton.setBorderPainted(true);
					
				}else if(configurationsGameButton.isBorderPainted()){
					
					configurationsGameButton.setBorderPainted(false);
					instructionsButton.setBorderPainted(true);
					
				}else if(instructionsButton.isBorderPainted()){
					
					instructionsButton.setBorderPainted(false);
					myInfo.setBorderPainted(true);
					
				}else if(myInfo.isBorderPainted()) {
					
					myInfo.setBorderPainted(false);
					newGameButton.setBorderPainted(true);
					
				}else {
					
					//Do nothing
				}
				
				break;
			
			//Check which button is selected and call it's related screen
			case (KeyEvent.VK_ENTER):
				
				if(newGameButton.isBorderPainted()){
					
					startNewGame();
					
				}else if(configurationsGameButton.isBorderPainted()){
					
					ConfigurationsScreen co = new ConfigurationsScreen(this.playerName);
					co.setVisible(true);
					
				}else if(instructionsButton.isBorderPainted()){
					
					InstructionsScreen in = new InstructionsScreen();
					in.setVisible(true);
				
				}else if(myInfo.isBorderPainted()) {
					
					MyInfoScreen my = new MyInfoScreen(this.playerName);
					my.setVisible(true);
					
				}else {
					
					//Do nothing
				}
				
				
				break;
				
			case (KeyEvent.VK_ESCAPE):
				
				terminateWindow();
				
				break;
			
			default:
				
				//Do nothing
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

