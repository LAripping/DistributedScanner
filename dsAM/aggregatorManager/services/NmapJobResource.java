package dsAM.aggregatorManager.services;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import dsAM.aggregatorManager.*;
import dsAM.aggregatorManager.db.MyConnection;
import dsAM.aggregatorManager.db.NmapJobDAO;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

/**
 * REST Web Service
 *
 * @author root
 */
@Path("/nmapjobs")
public class NmapJobResource {


	public NmapJobResource() {
	}

	/**
	 * 
	 * @param sa_hash The hash of the SA trying to get NmapJobs 
	 * @return A string-encoded list of string-encoded NmapJobs assigned to this SA
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getjob(@QueryParam("hash") String sa_hash){
		if(Main.v){
			System.out.println("Received nmapjob request from agent " +sa_hash);
		}
		ArrayList<String> job_lines=new ArrayList<String>();

		NmapJobDAO dao = new NmapJobDAO();
		job_lines = dao.getSAsNmapJobs(sa_hash, false);
		dao.setNmapJobAssigned(job_lines, sa_hash);
		

		return job_lines.toString();
	}
	
	/**
	 *
	 * @param job_id The ID of the NmapJob for which results have arrived 
	 * @param result The actual result from the execution of this NmapJob
	 */
	@POST
	@Path("/{id}")
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
			con.setAutoCommit(false);
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
			con.setAutoCommit(false);
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
