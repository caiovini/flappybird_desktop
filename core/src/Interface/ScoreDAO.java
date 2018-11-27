package Interface;


public interface ScoreDAO {

	public boolean insertNewScoreTime(int playerScore , String playerName , float time);
	public String[][] getScores();
	
}
