package Database;

import java.sql.Connection;
import java.sql.SQLException;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

public class ConnectMSSQLServer {
	
	private SQLServerDataSource ds;
	Connection conn;

	public ConnectMSSQLServer() {
		
		this.ds = new SQLServerDataSource();
		this.conn = null;
	}
	
	public Connection openConnection() {
		
		
			try {
				
				ds.setIntegratedSecurity(true);
				ds.setServerName("localhost");
				 /*
				    According to my sqlserver configuration,
				    this is the localhost port (59519), 
				    default port is normally (1433)
				 */
				ds.setPortNumber(59519);
				
				ds.setDatabaseName("FLAPPYBIRD");
				conn = ds.getConnection();
				
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
			
		return conn;
	}
	
}
