package Database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import Interface.PlayerDAO;
import Interface.ScoreDAO;
import Models.GameConfiguration;
import Models.Player;

public class GameDAO implements PlayerDAO , ScoreDAO {
	
	public static final String NAME = "name";
	public static final String NICK = "nick";
	public static final String AGE = "age";
	public static final String GENDER = "gender";
	public static final String LAST_SCORE = "last_score";
	public static final String MAX_SCORE = "max_score";
	public static final String MIN_SCORE = "min_score";
	public static final String AVG_SCORE = "avg_score";
	public static final String LAST_TIME = "last_time";
	public static final String MAX_TIME = "max_time";
	public static final String MIN_TIME = "min_time";
	public static final String AVG_TIME = "avg_time";
	
	@Override
	public boolean checkUser(String name) {
		
		boolean result = false;
		ConnectMSSQLServer sql = new ConnectMSSQLServer();
		Connection con = sql.openConnection(); //Remember to close connection
		
		try {
			
			PreparedStatement stmt = con.prepareStatement("SELECT COUNT(*) FROM TBL_PLAYER WHERE PLAYER_NAME = ?");
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();
			
			//Check if result of the statement is equal to one, in this case user was found
			while(rs.next()) {
				
				result = rs.getInt(1) == 1;
			}
			
			
			
		} catch (SQLException e) {

			e.printStackTrace();
			
		}finally {
			
			try {
				
				con.close();
				
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}
		
		
		return result;
	}
	
	@Override
	public boolean checkNick(String nick) {
		
		boolean result = false;
		ConnectMSSQLServer sql = new ConnectMSSQLServer();
		Connection con = sql.openConnection(); //Remember to close connection
		
		try {
			
			PreparedStatement stmt = con.prepareStatement("SELECT COUNT(*) FROM TBL_PLAYER WHERE PLAYER_NICK = ?");
			stmt.setString(1, nick);
			ResultSet rs = stmt.executeQuery();
			
			//Check if result of the statement is equal to one, in this case user was found
			while(rs.next()) {
				
				result = rs.getInt(1) == 1;
			}
			
			
			
		} catch (SQLException e) {

			e.printStackTrace();
			
		}finally {
			
			try {
				
				con.close();
				
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}
		
		
		return result;
	}
	
	
	@Override
	public boolean insertNewScoreTime(int playerScore , String playerName , float time) {
		
		boolean result = false;
		ConnectMSSQLServer sql = new ConnectMSSQLServer();
		Connection con = sql.openConnection(); //Remember to close connection
		
		try {
			
			CallableStatement stmt = con.prepareCall("{ call SP_INSERT_SCORE_TIME(? , ? , ?) }");
			stmt.setString(1, playerName);
			stmt.setInt(2, playerScore);
			stmt.setFloat(3, time);
			stmt.execute();
			result = true;
			
		} catch (SQLException e) {

			e.printStackTrace();
			
		}finally {
			
			try {
				
				con.close();
				
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}
		
		
		return result;
	}

	@Override
	public boolean insertNewPlayer(Player player, GameConfiguration gameConfiguration) {

		ConnectMSSQLServer sql = new ConnectMSSQLServer();
		Connection con = sql.openConnection(); //Remember to close connection
		
		boolean result = false;
		
		try {
			
			CallableStatement stmt = con.prepareCall("{ call SP_USER(? , ? , ? , ? , ? , ? , ? , ?) }");
			stmt.setString(1, "Y"); //'Y' for new user
			stmt.setString(2, player.getPlayerName());
			stmt.setString(3, player.getPlayerNick());
			stmt.setInt(4, player.getPlayerAge());
			stmt.setString(5, String.valueOf(player.getPlayerGender()));
			stmt.setString(6, String.valueOf(gameConfiguration.getConfigurationFlagMute()));
			stmt.setInt(7, gameConfiguration.getConfigurationVolume());
			stmt.setInt(8, gameConfiguration.getConfigurationBrightness());
			stmt.execute();
			result = true;
			
		}catch(SQLException e) {
			
			e.printStackTrace();
		}finally {
			
			try {
				con.close();
				
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
		
		return result;
		
	}

	@Override
	public boolean updatePlayer(Player player, GameConfiguration gameConfiguration) {
		
		ConnectMSSQLServer sql = new ConnectMSSQLServer();
		Connection con = sql.openConnection(); //Remember to close connection
		
		boolean result = false;
		
		try {
			
			CallableStatement stmt = con.prepareCall("{ call SP_USER(? , ? , ? , ? , ? , ? , ? , ?) }");
			stmt.setString(1, "N"); //'N' for updating configuration
			stmt.setString(2, player.getPlayerName());
			stmt.setString(3, player.getPlayerNick());
			stmt.setInt(4, player.getPlayerAge());
			stmt.setString(5, String.valueOf(player.getPlayerGender()));
			stmt.setString(6, String.valueOf(gameConfiguration.getConfigurationFlagMute()));
			stmt.setInt(7, gameConfiguration.getConfigurationVolume());
			stmt.setInt(8, gameConfiguration.getConfigurationBrightness());
			stmt.execute();
			result = true;
			
		}catch(SQLException e) {
			
			e.printStackTrace();
		}finally {
			
			try {
				con.close();
				
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
		
		return result;
		
	}

	@Override
	public String getPlayer(String name) {
		
		ConnectMSSQLServer sql = new ConnectMSSQLServer();
		Connection con = sql.openConnection(); //Remember to close connection
		
		StringBuilder response = new StringBuilder();
		
		try {
			
			CallableStatement stmt = con.prepareCall("{ call SP_GET_USERINI(?) }");
			stmt.setString(1 , name);
			stmt.execute();
			ResultSet result = stmt.getResultSet();
			
			while(result.next()) {
				
				response.append(result.getString(1));
				response.append(";");
				response.append(result.getString(2));
				response.append(";");
				response.append(result.getString(3));
				response.append(";");
				response.append(result.getString(4));
				response.append(";");
				response.append(result.getString(5));
			}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			
		}finally {
			
			try {
				
				con.close();
				
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
		
		return response.toString();
	}

	@Override
	public String[][] getScores() {
		
		ConnectMSSQLServer sql = new ConnectMSSQLServer();
		Connection con = sql.openConnection(); //Remember to close connection
		
		String[][] matrix = new String[10][2];
		
		try {
			
			CallableStatement stmt = con.prepareCall("{ call SP_GET_TOP10 }");
			stmt.execute();
			ResultSet result = stmt.getResultSet();
			
			int i = 0;
			while(result.next()) {
				
				matrix[i][0] = (result.getString(1));
				matrix[i][1] = (result.getString(2));
				i++;
			}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			
		}finally {
			
			try {
				
				con.close();
				
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
		
		return matrix;
	}
	
	public HashMap<String, String> getUserInfo(String namePlayer) {
		
		ConnectMSSQLServer sql = new ConnectMSSQLServer();
		Connection con = sql.openConnection(); //Remember to close connection
		HashMap<String, String> userInfo = new HashMap<String, String>();
		
		try {
								
			//Using view (VW_USER_INFO) from database
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM VW_USER_INFO WHERE PLAYER_NAME = ?");
			stmt.setString(1, namePlayer);
			ResultSet rs = stmt.executeQuery();
			
			//Check if result of the statement is equal to one, in this case user was found
			while(rs.next()) {
				
				userInfo.put(NAME, rs.getString(1));
				userInfo.put(NICK, rs.getString(2));
				userInfo.put(AGE, rs.getString(3));
				userInfo.put(GENDER, rs.getString(4));
				userInfo.put(LAST_SCORE, rs.getString(5));
				userInfo.put(MAX_SCORE, rs.getString(6));
				userInfo.put(MIN_SCORE, rs.getString(7));
				userInfo.put(AVG_SCORE, rs.getString(8));
				userInfo.put(LAST_TIME, rs.getString(9));
				userInfo.put(MAX_TIME, rs.getString(10));
				userInfo.put(MIN_TIME, rs.getString(11));
				userInfo.put(AVG_TIME, rs.getString(12));
			}
			
			
			
		} catch (SQLException e) {

			e.printStackTrace();
			
		}finally {
			
			try {
				
				con.close();
				
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}
		
		return userInfo;
	}
}
