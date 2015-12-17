package aggregatorManager.Services;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.sql.*;
/**
 * REST Web Service
 *
 * @author root
 */
@Path("/softwareagent")
public class SoftwareAgentInstance {

	

	
	
	/**
	 * PUT method for creating an instance of SoftwareAgentInsatnce
	 * @param all_info_hash representation for the resource
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	@PUT
	@Consumes(MediaType.TEXT_PLAIN)
	public Response registerAgent(String register_request) {		
		String[] rr=register_request.split("/");	// register_request String to be passed to gui
		String hash = rr[5];
		System.out.println(register_request);
		//guielements.popup( hash )

		//Boolean accepted = guielements.buttonValue()
		Boolean accepted=true;

		if(accepted){
			Connection con=null;
			PreparedStatement stmt= null;
			MyConnection c= new MyConnection();
			con=c.getInstance();			// Insert SA in database  
			
			String sql="INSERT INTO SoftwareAgents  VALUES(?,?)";
			try {
				con.setAutoCommit(false);
				stmt=con.prepareStatement( sql );
				stmt.setString(1, hash);
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
				System.err.println("SQl update statement for nmapjobs failed - " + e.getMessage());
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

			return Response.ok(MediaType.TEXT_PLAIN).build();
		}
		else{							// If registration is rejected 
			return Response.status(406).build();		// return a "406-Not Acceptable" status
		}
	}
}