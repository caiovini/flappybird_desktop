package Models;

public class Player {

	private String playerName;
	private String playerNick;
	private int playerAge;
	private char playerGender;
	
	public Player(String playerName , String playerNick , int playerAge , char playerGender) {
		
		this.playerName = playerName;
		this.playerNick = playerNick;
		this.playerAge = playerAge;
		this.playerGender = playerGender;
	}
	
	public Player(String playerName) {
		
		this.playerName = playerName;
	}
	
	
	public String getPlayerName() {
		
		return this.playerName;
	}
	
	public void setPlayerName(String playerName) {
		
		this.playerName = playerName;
	}
	
	public String getPlayerNick() {
		
		return this.playerNick;
	}
	
	public void setPlayerNick(String playerNick) {
		
		this.playerNick = playerNick;
	}
	
	public int getPlayerAge() {
		
		return this.playerAge;
	}
	
	public void setPlayerAge(int playerAge) {
		
		this.playerAge = playerAge;
	}
	
	public char getPlayerGender() {
		
		return this.playerGender;
	}
	
	public void setPlayerGender(char playerGender) {
		
		this.playerGender = playerGender;
	}
	
}
