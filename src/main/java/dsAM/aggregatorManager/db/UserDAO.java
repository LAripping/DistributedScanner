package dsAM.aggregatorManager.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class UserDAO {
	JPasswordField passwordField;
    JTextField usernameField;
    int status;
	boolean active;

	public UserDAO(JTextField usernameField,JPasswordField passwordField) {
		this.passwordField=passwordField;
		this.usernameField=usernameField;
	}
	
	
	public UserDAO() {
		
	}


	public int getStatus()
	{
		return this.status;
	}
	
	
	public void CheckAdmin() 
	{
		//set up the query
		boolean active = false;
		Connection con=null;			
		PreparedStatement stmt= null;
		MyConnection c= new MyConnection();
		con=c.getInstance();
		ResultSet rs = null;
		this.status = -1;
		
		String username=usernameField.getText();
        char[] c1 = passwordField.getPassword();
        String password=String.valueOf(c1);
        Arrays.fill(c1 , '0');
        
        String sql = "SELECT active FROM users WHERE username=? AND password=?";
        
        try {
			stmt=con.prepareStatement( sql );
			stmt.setString(1, username);
			stmt.setString(2, password);
			rs = stmt.executeQuery();
			if(rs.next()) 					//if there is such a record in the database then...
			{
				active = rs.getBoolean(1);
				if(active == true)						//check to see if the user has already enter using his acount
				{
					this.status = 1;							//second time login
				}
				else
				{
					this.status = 0;							//successful login
				}
			}
			else
			{
				this.status = 2;								//user data dont exist
			}
				
		} catch (SQLException e) {
			System.err.println("SQl select statement to admin's data failed - " + e.getMessage());
			e.printStackTrace();
		} finally{
			try {
				stmt.close();
				con.close();
			} catch (SQLException e) {
				System.err.println("Failed to close SQL connection - " + e.getMessage());
				e.printStackTrace();
			}
		}
		
        if(status == 0)
        {
        	c= new MyConnection();
    		con=c.getInstance();
    		sql="UPDATE users SET active=? WHERE username=? AND password=?";
    		try 
    		{
    			con.setAutoCommit(false);
    			stmt=con.prepareStatement( sql );
    			stmt.setBoolean(1, true);
    			stmt.setString(2,username);
    			stmt.setString(3,password);
    			stmt.executeUpdate();
    			con.commit();
    		}
    		catch (SQLException e)
    		{
    			try {
    				con.rollback();
    			} catch (SQLException e1) {
    				System.err.println("Transaction rollback failed");
    				e1.printStackTrace();
    			}
    			System.err.println("SQl update statement for users failed - " + e.getMessage());
    			e.printStackTrace();
    		}
    		finally
    		{
    			try {
    				stmt.close();
    				con.close();
    			} catch (SQLException e) {
    				System.err.println("Failed to close SQL connection - " + e.getMessage());
    				e.printStackTrace();
    			}
    		}
        }
	}
	
	public void CheckAdmin(String username,String password) {
		Connection con=null;			
		PreparedStatement stmt= null;
		MyConnection c= new MyConnection();
		con=c.getInstance();
		ResultSet rs = null;
		this.status = -1;
		
		String sql = "SELECT active FROM users WHERE username=? AND password=?";
        
        try {
			stmt=con.prepareStatement( sql );
			stmt.setString(1, username);
			stmt.setString(2, password);
			rs = stmt.executeQuery();
			if(rs.next()) 					//if there is such a record in the database then...
			{
				active = rs.getBoolean(1);
				if(active == true)						//check to see if the user has already enter using his acount
				{
					this.status = 1;							//second time login
				}
				else
				{
					this.status = 0;							//successful login
				}
			}
			else
			{
				this.status = 2;								//user data dont exist
			}
				
		} catch (SQLException e) {
			System.err.println("SQl select statement to admin's data failed - " + e.getMessage());
			e.printStackTrace();
		} finally{
			try {
				stmt.close();
				con.close();
			} catch (SQLException e) {
				System.err.println("Failed to close SQL connection - " + e.getMessage());
				e.printStackTrace();
			}
		}
		
        if(status == 0)
        {
        	c= new MyConnection();
    		con=c.getInstance();
    		sql="UPDATE users SET active=? WHERE username=? AND password=?";
    		try 
    		{
    			con.setAutoCommit(false);
    			stmt=con.prepareStatement( sql );
    			stmt.setBoolean(1, true);
    			stmt.setString(2,username);
    			stmt.setString(3,password);
    			stmt.executeUpdate();
    			con.commit();
    		}
    		catch (SQLException e)
    		{
    			try {
    				con.rollback();
    			} catch (SQLException e1) {
    				System.err.println("Transaction rollback failed");
    				e1.printStackTrace();
    			}
    			System.err.println("SQl update statement for users failed - " + e.getMessage());
    			e.printStackTrace();
    		}
    		finally
    		{
    			try {
    				stmt.close();
    				con.close();
    			} catch (SQLException e) {
    				System.err.println("Failed to close SQL connection - " + e.getMessage());
    				e.printStackTrace();
    			}
    		}
        }
	}
	
	public void addUser(String username,String password) {
		Connection con = null;
		PreparedStatement stmt = null;
		MyConnection c = new MyConnection();
		con = c.getInstance();
		String sql = "INSERT INTO users(username,password,active)VALUES (?,?,?)";
		try {
			con.setAutoCommit(false);
			stmt = con.prepareStatement(sql);
			stmt.setString(1,username);
			stmt.setString(2, password);
			stmt.setBoolean(3, true);
			stmt.executeUpdate();
			con.commit();
			System.out.println("User has been added");
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				System.err.println("Transaction rollback failed");
				e1.printStackTrace();
			}
			System.err.println("SQl User storage failed - " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				con.close();
			} catch (SQLException e) {
				System.err.println("Failed to close SQL connection - " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	public void userLogout(String username,String password){
		Connection con = null;
		PreparedStatement stmt = null;
		MyConnection c = new MyConnection();
		con = c.getInstance();
		String sql="UPDATE users SET active=? WHERE username=? AND password=?";
		try {
			con.setAutoCommit(false);
			stmt=con.prepareStatement( sql );
			stmt.setBoolean(1, false);
			stmt.setString(2, username);
			stmt.setString(3, password);
			stmt.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				System.err.println("Transaction rollback failed");
				e1.printStackTrace();
			}
			System.err.println("SQl update statement for users failed - " + e.getMessage());
			e.printStackTrace();
		} finally{
			try {
				stmt.close();
				con.close();
			} catch (SQLException e) {
				System.err.println("Failed to close SQL connection - " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

}
