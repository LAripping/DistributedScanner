package dsAM.aggregatorManager.db;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author root
 */
public class SoftwareAgentDAO {

	public SoftwareAgentDAO() {
	}

	/**
	 *
	 * @param sa_hash The hash of the SA that will be registered
	 */
	public void acceptSAregistration(String sa_hash) {
		Connection con = null;
		PreparedStatement stmt = null;
		MyConnection c = new MyConnection();
		con = c.getInstance();			// Insert SA in database  

		String sql = "INSERT INTO SoftwareAgents  VALUES(?,?)";
		try {
			con.setAutoCommit(false);
			stmt = con.prepareStatement(sql);
			stmt.setString(1, sa_hash);
			stmt.setBoolean(2, true);
			stmt.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				System.err.println("Transaction rollback failed");
				e1.printStackTrace();
			}
			System.err.println("SQl registration failed - " + e.getMessage());
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
}
