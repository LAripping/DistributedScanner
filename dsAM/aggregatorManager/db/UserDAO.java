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

	public UserDAO(JTextField usernameField,JPasswordField passwordField) {
		this.passwordField=passwordField;
		this.usernameField=usernameField;
	}
	
	public boolean CheckAdmin() {
		String admin_username=null;
    	String admin_password=null;
		Connection con=null;			
		PreparedStatement stmt= null;
		MyConnection c= new MyConnection();
		con=c.getInstance();
		ResultSet rs = null;
		
		String username=usernameField.getText();
        char[] c1 = passwordField.getPassword();
        String password=String.valueOf(c1);
        Arrays.fill(c1 , '0');
        String sql = "SELECT username , password  FROM users";
        try {
			stmt=con.prepareStatement( sql );
			rs = stmt.executeQuery();
			while(rs.next()) {
				admin_username=rs.getString(1);
				admin_password=rs.getString(2);
				
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
        if((admin_password.equals(password)) && (admin_username.equals(username))){
        	c= new MyConnection();
    		con=c.getInstance();
    		sql="UPDATE users SET active=?";
    		try {
    			con.setAutoCommit(false);
    			stmt=con.prepareStatement( sql );
    			stmt.setBoolean(1, true);
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
            return true;
        }
        else
        	return false;
	}

}
