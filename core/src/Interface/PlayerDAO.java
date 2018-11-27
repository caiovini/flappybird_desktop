package Interface;

import java.util.HashMap;
import Models.GameConfiguration;
import Models.Player;

public interface PlayerDAO {

	public boolean insertNewPlayer(Player player, GameConfiguration gameConfiguration);
	public boolean updatePlayer(Player player, GameConfiguration gameConfiguration);
	public String getPlayer(String name);
	public boolean checkUser(String name);
	public boolean checkNick(String nick);
	public HashMap<String, String> getUserInfo(String name);
}
