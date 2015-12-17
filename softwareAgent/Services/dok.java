package softwareAgent.Services;

import java.sql.*;

public class dok {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://localhost/DSschema?user=root&password=";
	
	public static void main(String[] args) throws SQLException	{
		Connection con=null;
		Statement stmt= null;
		String params = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con=DriverManager.getConnection(DB_URL);
			stmt=con.createStatement();
			String sql="INSERT INTO users " +"VALUES(3,'ljhns','theos',0)";
			stmt.executeUpdate(sql);
			sql="SELECT id,username FROM users";
			ResultSet rs= stmt.executeQuery(sql);
			while(rs.next()){
				int id=rs.getInt("id");
				params =rs.getString("username");
				System.out.println(id);
				System.out.println(params);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		            con.close();
		      }catch(SQLException se){
		      }// do nothing
		      try{
		         if(con!=null)
		            con.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }//end try
		   System.out.println("Goodbye!");

	}

}
