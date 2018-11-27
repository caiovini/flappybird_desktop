package com.applicationdevelopment.flappybird.desktop;

import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import Database.GameDAO;
import helper.*;
import screens.InitialScreen;

public class DesktopLauncher {
	

	
	public static void main (String[] arg) {
		
		JTextField userName;
		boolean userFound = false;
		
		InitialScreen frame;
		
		JFrame optionFrame = new JFrame();
		optionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		optionFrame.setLocationRelativeTo(null);
		
		Object[] options = { "OK", "NEW USER" };
		
		JPanel optionPanel = new JPanel( new GridLayout(1, 1));
		userName = new HintTextField("USER NAME");
		userName.setDocument(new JTextFieldLimit(50));
		optionPanel.add(userName);
		
		
		do { //Do while fields are not OK
		int result = JOptionPane.showOptionDialog(
				                 optionFrame, 
				                 optionPanel ,
				                 "ENTER USER NAME" ,
				                 JOptionPane.DEFAULT_OPTION , 
				                 JOptionPane.PLAIN_MESSAGE,
				                 null,
				                 options,
				                 options[0]);
		
		String name = userName.getText().toString();
		
		if( result == JOptionPane.YES_OPTION ) {
		
			//Check if username exists on database//
			
			if(name.isEmpty() || name.equals("USER NAME")) {
				
				JOptionPane.showMessageDialog(null, "User can not be empty");
				
			}else {
				
				GameDAO player = new GameDAO();
				
				if(!player.checkUser(name)) {
			
				
					JOptionPane.showMessageDialog(null, "User not found");
				
				}else {
			
					userFound = true;
					frame = new InitialScreen(name);
					frame.setVisible(true);
					
				}
			}
			
		}else if( result == JOptionPane.NO_OPTION) {
				
			userFound = true; 
			CustomDialog dialog = new CustomDialog(optionFrame , "NEW USER");
			dialog.setLocationRelativeTo(null);
			dialog.setVisible(true);
			
		}else if(result == JOptionPane.CLOSED_OPTION) {
			  
			  userFound = true; //Get out of the loop just in case
			  System.exit(0);
		  }
	    }
		
	//While user not found	
	while(!userFound);
   }	
 }	
	