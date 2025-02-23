package zs.library.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbManager {

	private static final String URL = "jdbc:postgresql://localhost:5432/LibraryManagement";
	private static final String USER_NAME = "postgres";
	private static final String PASSWORD = "Arun@1103";

	private static DbManager instance = null;
	
//	static {
//	    try {
//	        Class.forName("org.postgresql.Driver");
//	    } catch (ClassNotFoundException e) {
//	        e.printStackTrace();
//	    }
//	}

	
//	private static Connection connection = null;
	
	
	private DbManager() {
		// TODO Auto-generated constructor stub
	}
	
	public static DbManager getInstance() {
		if(instance == null) {
			instance = new DbManager();
		}
		
		return instance;
	}
	
	public Connection createConnection() throws SQLException, ClassNotFoundException {

//		if (connection == null) {
		Connection	connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
//		}
		return connection;

	}

}
