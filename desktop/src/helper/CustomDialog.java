package helper;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import Database.GameDAO;
import Models.GameConfiguration;
import Models.Player;
import screens.InitialScreen;

public class CustomDialog extends JDialog implements ActionListener, PropertyChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Object[] options = { "OK", "CANCEL" };
	private JTextField userName , userNick , userAge , userGender;
	private JOptionPane optionPane ;
	private JLabel alert; 


    /**
     * Creates the reusable dialog.
     */
    public CustomDialog(Frame aFrame, String aWord) {
        super(aFrame, true);

        
        JPanel newUserPanel = new JPanel( new GridLayout(9, 1 , 10 , 10));
        newUserPanel.add(new JLabel("USER NAME"));
		newUserPanel.add(userName = new HintTextField("USER NAME"));
		userName.setDocument(new JTextFieldLimit(50));
		
		newUserPanel.add(new JLabel("NICKNAME WITH MAXIMUM 3 LETTERS"));
		newUserPanel.add(userNick = new JTextField());
		userNick.setDocument(new JTextFieldLimit(3));
		
		newUserPanel.add(new JLabel("AGE"));
		newUserPanel.add(userAge = new HintTextField("AGE"));
		userAge.setDocument(new JTextFieldLimit(2));
		
		newUserPanel.add(new JLabel("USER GENDER"));
		newUserPanel.add(userGender = new HintTextField("GENDER"));
		userGender.setDocument(new JTextFieldLimit(1));
		
		alert = new JLabel("");
		alert.setForeground(Color.RED);
		newUserPanel.add(alert);

        //Create the JOptionPane.
        optionPane = new JOptionPane(newUserPanel , 
        		                     JOptionPane.DEFAULT_OPTION ,
        		                     JOptionPane.PLAIN_MESSAGE , 
        		                     null,
        		                     options ,
        		                     options[0]);

        //Make this dialog display it.
        this.setContentPane(optionPane);

        //Handle window closing correctly.
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        //Ensure the text field always gets the first focus.
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent ce) {
            	userName.requestFocusInWindow();
            }
        });

        //Register an event handler that puts the text into the option pane.
        userName.addActionListener(this);

        //Register an event handler that reacts to option pane state changes.
        optionPane.addPropertyChangeListener(this);
        pack();
    }

    /**
     * This method handles events for the text field.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        optionPane.setValue(userName);
    }

    /**
     * This method reacts to state changes in the option pane.
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();
        
        alert.setText("");
        
        String name = userName.getText().toString();
        String nick = userNick.getText().toString();
        String age = userAge.getText().toString();
        String gender = userGender.getText().toString();
        
        String component = "";
        
        try {

        	component = e.getNewValue().toString();
        	
        }catch(NullPointerException ex) {
        	
        	exit();
        }

        
        if (isVisible()
                && (e.getSource() == optionPane)
                && (JOptionPane.VALUE_PROPERTY.equals(prop)
                || JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {
            Object value = optionPane.getValue();

            if (value == JOptionPane.UNINITIALIZED_VALUE) {
                //ignore reset
                return;
            }

            //Reset the JOptionPane's value.
            //If you don't do this, then if the user
            //presses the same button next time, no
            //property change event will be fired.
            optionPane.setValue(
                    JOptionPane.UNINITIALIZED_VALUE);
            
            if (component.equals("CANCEL")) exit();
            
            if (component.equals("OK")) {
            	
            	GameDAO database = new GameDAO();
            	
            	if (name.isEmpty() || name.equals("USER NAME")) {
            		
            		alert.setText("User name can not be empty");
            		userName.requestFocus();
            		
            	}else if(nick.isEmpty() || nick.length() < 3) {
            		
            		alert.setText("Nickname must be longer than 3 letters");
            		userNick.requestFocus();
            		
            	}else if(database.checkUser(name)) {
            	
            		alert.setText("User name already exists");
            		userName.requestFocus();
            	
            	}else if(database.checkNick(nick)) {
            	
            		alert.setText("Nick already exists");
            		userNick.requestFocus();
            	
            	}else if(!isInteger(age)) {
            	
            		alert.setText("Age must be numeric");
            		userAge.requestFocus();
            		
            	}else if(gender.isEmpty()) {
            		
            		alert.setText("Gender can not be empty");
            		userGender.requestFocus();
            		
            	}else if(!gender.equals("M") && !gender.equals("F")) {
            		
            		alert.setText("Gender must be M of F");
            		userGender.requestFocus();
            		
            	}else {
            		
            		Player player = new Player(name , nick , Integer.parseInt(age) , gender.charAt(0));
            		
            		if(!database.insertNewPlayer(player, new GameConfiguration())) {
            			
            			alert.setText("Ops, error trying to insert new user");
            			
            		}else {
            		
            			this.setVisible(false);
            			InitialScreen frame = new InitialScreen(name);
            			frame.setVisible(true);
            		}
            	}
            }
        }
    }
    
    private static boolean isInteger(String s) {
        try { 
            Integer.parseInt(s);
            
        } catch(NumberFormatException e) {
        	
            return false; 
        } catch(NullPointerException e) {
        	
            return false;
        }
        // only got here if we didn't return false
        return true;
    }
    

    /**
     * This method clears the dialog and hides it.
     */
    public void exit() {
    	
        System.exit(0);
    }
}
