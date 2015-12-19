package dsAM.aggregatorManager.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class MyConnection {
	private String JDBC_DRIVER = "com.mysql.jdbc.Driver";  

	private static String DBuser;
	private static String DBpass;
	private static String DB_URL;

	
	public static void setConnectionParameters(String db_user, String db_pass) {		
		DBuser = db_user;
		DBpass = db_pass;
		DB_URL =  "jdbc:mysql://localhost/DSschema?user=" + DBuser + "&password=" + DBpass;
	}
	
	public Connection getInstance(){
		Connection con=null;
		try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			con=DriverManager.getConnection(DB_URL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
		
	}

}