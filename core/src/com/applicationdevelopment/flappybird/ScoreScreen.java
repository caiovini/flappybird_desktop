package com.applicationdevelopment.flappybird;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import Database.GameDAO;
import Layouts.ModifiedFlowLayout;
import Layouts.RoundedBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;



public class ScoreScreen extends JFrame {
	
	private JButton buttonEndgame = new JButton("END GAME"); 
	private boolean flagEndGame = false;
	private String playerName;
	
	
	private final int NICK = 1;
	
	private static final long serialVersionUID = 1L;

		public ScoreScreen(String playerName) {
			
			
			this.playerName = playerName;
			this.setLayout(new BorderLayout());
			this.setTitle("Score end game");
			this.setSize(500, 450);
			this.setResizable(false);
			this.setUndecorated(true);
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			
			JLabel background = new JLabel(new ImageIcon("images/score_screen/background.jpg"));
			ModifiedFlowLayout md = new ModifiedFlowLayout(FlowLayout.CENTER , 50 , 10);
			
			background.setLayout(md);
			background.add(createTextFields("TOP 10 HIGHEST SCORES" , 20 , Color.WHITE));
			String dots = repeat(".", 121);
			
			GameDAO database = new GameDAO();
			String[] player = database.getPlayer(this.playerName).split(";");
			
			String[][] scores = database.getScores();
			
			/*
			 * scores[0] Player nick
			 * scores[1] Player score 
			 */
			
			//add labels
			for(int i = 0 ; i < 10 ; i++) {
				
				if(scores[i][0] != null) {
				
					if(player[NICK].equals(scores[i][0])) {
				
						background.add(createTextFields(scores[i][0] + dots + scores[i][1] , 12 , Color.YELLOW));
				
					}else {
					
						background.add(createTextFields(scores[i][0] + dots + scores[i][1] , 12 , Color.WHITE));
						}
					}
				}
			
			
			RoundedBorder roundedBorder = new RoundedBorder(10);
			buttonEndgame.setBorder(roundedBorder);
			buttonEndgame.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					
					endGame();
				}
			});
			
			background.add(buttonEndgame);
			this.add(background);	
			
			this.addWindowFocusListener(new WindowFocusListener() {
				
				@Override
				public void windowLostFocus(WindowEvent e) {
					// TODO Auto-generated method stub
					disposeScreen();
					
				}
				
				@Override
				public void windowGainedFocus(WindowEvent e) {
					// TODO Auto-generated method stub
					
				}
			});		
			
			this.setVisible(true);
		}
		
		private JTextField createTextFields(String content , int fontSize , Color color) {
			
			JTextField textFields = new JTextField(content);
			textFields.setForeground(color);
			textFields.setFont(new Font("TimesRoman", Font.BOLD, fontSize));
			textFields.setOpaque(false);
			textFields.setBorder(javax.swing.BorderFactory.createEmptyBorder());
			textFields.setHorizontalAlignment(SwingConstants.RIGHT);
			textFields.setEditable(false);
			return textFields;
		}
		  
		private void endGame() {
			
			flagEndGame = true;
		}
		
		private void disposeScreen() {
			
			this.dispose();
		}
		
		public void focus() {
			
			this.requestFocus();
			this.toFront();
			this.setLocationRelativeTo(null);
		}
		
	    public static String repeat(String str, int times) {
	    	
	        return new String(new char[times]).replace("\0", str);
	    }
	    
	    public boolean isEndGame() {
	    	
	    	return flagEndGame;
	    }
	    
    }
