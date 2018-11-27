package screens;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Database.GameDAO;
import Models.GameConfiguration;
import Models.Player;



public class ConfigurationsScreen extends JFrame implements KeyListener , ItemListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final String WING_SOUND_PATH = "sounds/wing.wav";
	private Font font = new Font("TimesRoman", Font.BOLD, 15);
	
	private JToggleButton muteButton;
	private JSlider volume;
	private JSlider brightness;
	private AlphaPanel imgPanel;
	private String playerName;
	
	//Image to change the brightness
	private ImageIcon img;
	private static final String NAME = "images/instructions_screen/press_enter.png";
	
	private final int FLAG_MUTE = 2;
	private final int VOLUME = 3;
	private final int BRIGHTNESS = 4;
	
	private Player player;
	private GameConfiguration gameConfiguration;
	private GameDAO database;
	

	public ConfigurationsScreen(String playerName) {
		
		/*
		 * Set all configurations for this screen 
		 * in the constructor
		 * The caller will only define visibility to true
		 */
		
		this.playerName = playerName;
		player = new Player(this.playerName);
		
		database = new GameDAO();
		String[] config = database.getPlayer(this.playerName).split(";");
		gameConfiguration = new GameConfiguration(config[FLAG_MUTE].charAt(0) , 
				                                  Integer.parseInt(config[VOLUME]) ,
				                                  Integer.parseInt(config[BRIGHTNESS]));
		
		/* config[0] = Player name
		 * config[1] = Player nick
		 * config[2] = Configuration flag mute
		 * config[3] = Configuration volume
		 * config[4] = Configuration brightness 
		 */
		
		addKeyListener(this);
		this.setLayout(new GridLayout( 6 , 1 , 0 , 0));
		this.setTitle(" Configurations ");
		this.setFocusable(true);
		this.setSize(100, 100);
		this.setLocation(420, 70);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		
		muteButton = new JToggleButton();
		muteButton.setText(config[FLAG_MUTE].equals("Y") ? "Mute" : "Normal");
		muteButton.setSelected(config[FLAG_MUTE].equals("Y"));
		muteButton.addItemListener(this);
		this.add(muteButton);
		
		this.add(createLabels("<html><p align='center'>VOLUME</p></html>" , 70));
		volume = new JSlider(JSlider.HORIZONTAL, 1, 6, 1);
		volume.setMinorTickSpacing(1);
		volume.setMajorTickSpacing(1);
		volume.setPaintTicks(true);
		volume.setPaintLabels(true);
		volume.setValue(Integer.parseInt(config[VOLUME]));
		
		volume.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {

				// Get level selected by the user on the slider, play sound and save it to dabase
				
				JSlider theSlider = (JSlider) e.getSource();
				
				if(!theSlider.getValueIsAdjusting()) {
					
					int v = theSlider.getValue();
					
					gameConfiguration.setConfigurationVolume(v); 
					database.updatePlayer(player, gameConfiguration);
					playSound(theSlider);
				}
			}
		});
		
		this.add(volume);
		
		
		this.add(createLabels("<html><p align='center'>BRIGHTNESS</p></html>" , 55));
		brightness = new JSlider(JSlider.HORIZONTAL , 1 , 6 , 1);
		brightness.setMinorTickSpacing(1);
		brightness.setMajorTickSpacing(1);
		brightness.setPaintTicks(true);
		brightness.setPaintLabels(true);
		brightness.setValue(Integer.parseInt(config[BRIGHTNESS]));
		
		img = new ImageIcon(new ImageIcon(NAME).getImage().getScaledInstance(200, 100, Image.SCALE_DEFAULT));
		imgPanel = new AlphaPanel(img, 1);
		
		/*
		 *  Get information from database in order to set initial value for the alpha
		 *  It is important to keep the game information save into the database 
		 */
		
		imgPanel.setAlpha((float) Integer.parseInt(config[BRIGHTNESS]) / brightness.getMaximum());
		
		brightness.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				
				// Get level selected by the user on the slider and update image
				
				JSlider theSlider = (JSlider) e.getSource();
				
				if(!theSlider.getValueIsAdjusting()) {
					
					int v = theSlider.getValue();
					
					gameConfiguration.setConfigurationBrightness(v); 
					database.updatePlayer(player, gameConfiguration);
					imgPanel.setAlpha((float) v / theSlider.getMaximum());
				}
				
			}
		});
		
		this.add(brightness);
		this.add(imgPanel);		
		
		this.addWindowFocusListener(new WindowFocusListener() {
			
			@Override
			public void windowLostFocus(WindowEvent arg0) {
				
				//Check if window lost focus, in this case dispose it
				terminateWindow();
			}
			
			@Override
			public void windowGainedFocus(WindowEvent arg0) {

				// Do nothing 
				
			}
		});
		
		this.pack();
		
	}
	
	private JLabel createLabels(String content , int margin) {
		
		JLabel labels = new JLabel(content);
		labels.setBorder(BorderFactory.createEmptyBorder(0, margin, 0, 0));
		labels.setForeground(Color.BLACK);
		labels.setFont(font);
		return labels;
	}
	
	private void terminateWindow() {
		
		this.dispose();
	}
	
	
	private void playSound(JSlider j) {
		
		int level = j.getValue();
		int volume = 0;
		
		//Level of the volume
		switch(level) {
		
			case 0:
			
				volume = - 30;
				break;
			
			case 1:
				
				volume = - 25;
				break;
				
			case 2:
				
				volume = - 20;
				break;
				
			case 3:
				
				volume = - 15;
				break;
				
			case 4:
				
				volume = - 10;
				break;
				
			case 5:
				
				volume = - 5;
				break;
				
			case 6:
				
				volume = 0;
				break;
		}
		
		try {
			
			/*
			 * Get audio of the wing flap as a test
			 * This method will be called every time the user changes the values at the slider
			 * The FloatControl is being manipulated to change the volume 
			 */
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(WING_SOUND_PATH));
			Clip clip =  AudioSystem.getClip();
			clip.open(audioInputStream);
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue((float) volume);
			clip.start();
			
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			
			e.printStackTrace();
		}
		
	}

	@Override
	public void keyPressed(KeyEvent key) {

		// Whenever the user presses escape key close the window
		
		switch(key.getKeyCode()) {
		
		
		case (KeyEvent.VK_ESCAPE):
			
			terminateWindow();
			
			break;
	}		
		
	}

	@Override
	public void keyReleased(KeyEvent key) {
		
		// Do nothing 
		
	}

	@Override
	public void keyTyped(KeyEvent key) {

		// Do nothing 
		
	}

	@Override
	public void itemStateChanged(ItemEvent item) {
		
		// Change the toggle button text and change it at the database 
		
		int state = item.getStateChange();
		
		if(state == ItemEvent.SELECTED) {
			
			gameConfiguration.setConfigurationFlagMute('Y');
			database.updatePlayer(player, gameConfiguration);
			muteButton.setText("Mute");
		}else {
			
			gameConfiguration.setConfigurationFlagMute('N');
			database.updatePlayer(player, gameConfiguration);
			muteButton.setText("Normal");
		}
	}
	
	class AlphaPanel extends JPanel {
		
		/**
		 * This class is used to change
		 * the alpha for the image as a preview for the user
		 * in order to adjust the brightness for the game
		 */
		private static final long serialVersionUID = 1L;
		private BufferedImage bi;
	    private float[] scales = {1f, 1f, 1f, 1f};
	    private float[] offsets = new float[4];
	    private RescaleOp rop;
	    
	    public AlphaPanel(ImageIcon icon, double scale) {

	        int width = (int) (scale * icon.getIconWidth());
	        int height = (int) (scale * icon.getIconHeight());
	        this.setPreferredSize(new Dimension(width, height));
	        this.bi = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB);
	        this.bi.createGraphics().drawImage( icon.getImage(), 0, 0, width, height, null);
	        rop = new RescaleOp(scales, offsets, null);
	    }
	    
	    @Override
	    public void paintComponent(Graphics g) {
	    	
	        super.paintComponent(g);
	        Graphics2D g2d = (Graphics2D) g;
	        g2d.drawImage(bi, rop, 0, 0);
	    }

	    public void setAlpha(float alpha) {
	    	
	    	//Change just the alpha of RGBA
	        this.scales[1] = alpha;
	        this.rop = new RescaleOp(scales, offsets, null);
	        this.repaint();
	    }
		
	}
	
}
