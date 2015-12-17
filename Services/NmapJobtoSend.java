package aggregatorManager.Services;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.grizzly.http.server.Request;

import aggregatorManager.Main;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

/**
 * REST Web Service
 *
 * @author root
 */
@Path("/nmapjobtosend")
public class NmapJobtoSend {

	/**
	 * Creates a new instance of NmapJobtoSend
	 */
	public NmapJobtoSend() {

	}

	/**
	 * 
	 * @return json'ed nmap job
	 * @throws SQLException 
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getjob(@PathParam("hash") String sa_hash){
		if(Main.v){
			System.out.println("Received nmapjob request from agent " +sa_hash);
		}
		List<String> job_lines=new ArrayList<String>();

		Connection con=null;				// Query database for SA-specific nmap jobs
		PreparedStatement stmt= null;
		MyConnection c= new MyConnection();
		con=c.getInstance();
		String sql="SELECT id,params,periodic,period FROM nmap_jobs WHERE SoftwareAgents_hash=?";
		ResultSet rs = null;
		Boolean found = false;
		try {
			stmt=con.prepareStatement( sql );
			stmt.setString(1, sa_hash);
			rs = stmt.executeQuery();
			while(rs.next()) {
				job_lines.add(rs.toString());
				found = true;
			}
		} catch (SQLException e) {
			System.err.println("SQl select statement to get nmapjobs failed - " + e.getMessage());
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
		
		
	
		if(found){
			c= new MyConnection();			// Updating sent nmapjobs' info
			con=c.getInstance();
	
			Calendar calendar= Calendar.getInstance();
			Timestamp ts= new Timestamp(calendar.getTime().getTime());
			sql="UPDATE nmap_jobs SET assigned=?, insertion_time=? FROM nmap_jobs WHERE SoftwareAgents_hash=?";
			try {
				stmt=con.prepareStatement( sql );
				stmt.setBoolean(1, true);
				stmt.setTimestamp(2, ts);
				stmt.setString(3, sa_hash);
				stmt.executeUpdate();
				con.commit();
			} catch (SQLException e) {
				System.err.println("SQl update statement for nmapjobs failed - " + e.getMessage());
				e.printStackTrace();
				try {
					con.rollback();
				} catch (SQLException e1) {
					System.err.println("Transaction rollback failed");
					e1.printStackTrace();
				}
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

		return job_lines.toString();
	}
	
	
	
	@POST
	@Path("/nmapjobtosend/{id}")
	@Consumes(MediaType.TEXT_PLAIN)
	public void postedResults(@PathParam("id")int job_id, String result) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			System.err.println("Failed to digest SA info for Register request - " + e.getMessage());
			e.printStackTrace();
		}
		try {
			md.update(result.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e2) {
			System.err.println("Encoding not recognized - " + e2.getMessage());
			e2.printStackTrace();
		}					// Hash result to store in db
		byte[] digest = md.digest();
		String result_hash = String.format("%064x", new java.math.BigInteger(1, digest));
		
		Connection con=null;
		PreparedStatement stmt= null;
		MyConnection c= new MyConnection();
		con=c.getInstance();			// Insert results in database  
		
		String sql="INSERT INTO results  VALUES(?,?)";
		try {
			stmt=con.prepareStatement( sql );
			stmt.setString(1, result_hash);
			stmt.setString(2, result);
			stmt.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				System.err.println("Transaction rollback failed");
				e1.printStackTrace();
			}
			System.err.println("SQl insert statement for results failed - " + e.getMessage());
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
		
		
		con=null;
		stmt= null;
		c= new MyConnection();
		con=c.getInstance();			// Connect nmap jobs with their reults  in database  
		
		sql="INSERT INTO nmap_jobs_has_results  VALUES(?,?)";
		try {
			stmt=con.prepareStatement( sql );
			stmt.setInt(1, job_id);
			stmt.setString(2, result_hash);
			stmt.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				System.err.println("Transaction rollback failed");
				e1.printStackTrace();
			}
			System.err.println("SQl insert statement for nmmap_job_has_results failed - " + e.getMessage());
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