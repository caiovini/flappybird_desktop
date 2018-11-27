package screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import Database.GameDAO;
import helper.Histogram;

public class MyInfoScreen extends JFrame implements KeyListener{
	
	private Font font = new Font("TimesRoman", Font.BOLD, 15);
	
	/**
	 * This is a simple screen to show user' information
	 * Name, gender, age and Nickname for the game
	 */
	private static final long serialVersionUID = 1L;
	private String playerName;
	
	public MyInfoScreen(String playerName) {
		
		this.playerName = playerName;
		
		this.addKeyListener(this);
		this.setLayout(new GridLayout(3 , 1 , 0 , 0));
		this.setTitle(" My info ");
		this.getContentPane().setBackground(Color.WHITE);
		this.setFocusable(true);
		this.setResizable(false);
		this.setSize(500, 500);
		this.setLocationRelativeTo(null); // Center the frame
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		GameDAO database = new GameDAO();
		HashMap<String, String> userInfo = database.getUserInfo(this.playerName); 
	
		JPanel infoPanel = new JPanel(new GridLayout(1 , 4 , 0 , 0));
		infoPanel.add(createLabels("<html><u>" + "NAME: " + userInfo.get(GameDAO.NAME) + "</u></html>"));
		infoPanel.add(createLabels("<html><u>" + "NICK: " + userInfo.get(GameDAO.NICK) + "</u></html>"));
		infoPanel.add(createLabels("<html><u>" + "AGE: "  + userInfo.get(GameDAO.AGE) + "</u></html>"));
		infoPanel.add(createLabels("<html><u>" + "GENDER: " + userInfo.get(GameDAO.GENDER) + "</u></html>"));
		
		this.add(infoPanel);
		
		float countScore[] = { Float.parseFloat(userInfo.get(GameDAO.LAST_SCORE)),
							   Float.parseFloat(userInfo.get(GameDAO.MAX_SCORE)), 
							   Float.parseFloat(userInfo.get(GameDAO.MIN_SCORE)),
							   Float.parseFloat(userInfo.get(GameDAO.AVG_SCORE))};
		
		float countTime[] = { Float.parseFloat(userInfo.get(GameDAO.LAST_TIME)),
				   			  Float.parseFloat(userInfo.get(GameDAO.MAX_TIME)), 
				   			  Float.parseFloat(userInfo.get(GameDAO.MIN_TIME)),
				   			  Float.parseFloat(userInfo.get(GameDAO.AVG_TIME))};
		
		
		this.add(new Histogram(countScore , new String[] {"LAST SCORE" , "MAX SCORE" , "MIN SCORE" , "AVG SCORE"} , 
		                                                 false , "SCORE"));
		
		this.add(new Histogram(countTime , new String[] {"LAST TIME" , "MAX TIME" , "MIN TIME" , "AVG TIME"} ,
				                                         true , "TIME"));
		
		this.addWindowFocusListener(new WindowFocusListener() {
			
			@Override
			public void windowLostFocus(WindowEvent e) {
				// TODO Auto-generated method stub
				terminateWindow();
			}
			
			@Override
			public void windowGainedFocus(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	
	private JLabel createLabels(String content) {
		
		JLabel labels = new JLabel(content);
		labels.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		labels.setForeground(Color.DARK_GRAY);
		labels.setFont(font);
		return labels;
	}
	
	//Close window
	private void terminateWindow() {
		
		this.dispose();
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
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
