import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHelper {
	private Connection dbCnx;
	private Statement stmt;
	private ResultSet resultSet = null;
	
	public void connect() {
		try {
			//dbCnx = DriverManager.getConnection("jdbc:mysql://google/cyberminer?cloudSqlInstance=cyberminer-shae:us-central1:cyberminer&socketFactory=com.google.cloud.sql.mysql.SocketFactory&user=root&password=cyberminer&useSSL=false");
			dbCnx = DriverManager.getConnection("jdbc:mysql://35.188.65.89/cyberminer","root","cyberminer");
			
			stmt = dbCnx.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void disconnect() {
		try {
			if(resultSet != null) {
				resultSet.close();
				resultSet = null;
			}
			
			if(stmt != null) {
				stmt.close();
				stmt = null;
			}
			
			if(dbCnx != null) {
				dbCnx.close();
				dbCnx = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ResultSet executeSelectQuery(String query) {
		try {
			resultSet = stmt.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return resultSet;
	}
	
	public ResultSet executeInsertQueryAndReturnKey(String query) {
		try {
			stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
			
			resultSet = stmt.getGeneratedKeys();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return resultSet;
	}
	
	public void executeUpdateQuery(String query) {
		try {
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}