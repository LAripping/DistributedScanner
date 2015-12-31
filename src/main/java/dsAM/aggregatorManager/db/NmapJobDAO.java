/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dsAM.aggregatorManager.db;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

/**
 *
 * @author tsaou
 */
public class NmapJobDAO {

	public NmapJobDAO() {
	}

	public void addNmapJob(String params, Boolean periodic, int period, String sa_hash) {
		Calendar calendar = Calendar.getInstance();
		Timestamp currentTimestamp = new java.sql.Timestamp(calendar.getTime().getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		MyConnection c = new MyConnection();
		con = c.getInstance();

		String sql = "INSERT INTO nmap_jobs("
			+ "params,periodic, period,"
			+ "insertion_time , SoftwareAgents_hash)"
			+ "VALUES (?,?,?,?,?)";
		try {
			con.setAutoCommit(false);
			stmt = con.prepareStatement(sql);
			stmt.setString(1, params);
			stmt.setBoolean(2, periodic);
			stmt.setInt(3, period);
			stmt.setTimestamp(4, currentTimestamp);
			stmt.setString(5, sa_hash);
			stmt.executeUpdate();
			
			con.commit();
			System.out.println("Nmapjob added");
			
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				System.err.println("Transaction rollback failed");
				e1.printStackTrace();
			}
			System.err.println("SQl Nmap storage  failed - " + e.getMessage());
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

	public void stopCommand(int id) {
		String params = "Stop";
		Connection con = null;
		PreparedStatement stmt = null;
		MyConnection c = new MyConnection();
		con = c.getInstance();

		String sql = "UPDATE nmap_jobs SET params=?,assigned=? WHERE id=?";
		try {
			con.setAutoCommit(false);
			stmt = con.prepareStatement(sql);
			stmt.setString(1, params);
			stmt.setBoolean(2, false);
			stmt.setInt(3, id);
			stmt.executeUpdate();
			con.commit();
			System.err.println("Nmapjob for removing, added");
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				System.err.println("Transaction rollback failed");
				e1.printStackTrace();
			}
			System.err.println("SQl Nmap storage for removing  failed - " + e.getMessage());
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
	
	public void exitCommand(String sa_hash){
		String params = "exit(0)";

		Connection con = null;
		PreparedStatement stmt = null;
		MyConnection c = new MyConnection();
		con = c.getInstance();

		String sql = "INSERT INTO nmap_jobs(id,params,periodic, period,SoftwareAgents_hash,assigned) VALUES (?,?,?,?,?,?)";
		try {
			con.setAutoCommit(false);
			stmt = con.prepareStatement(sql);
			stmt.setInt(1, -1);
			stmt.setString(2, params);
			stmt.setBoolean(3, true);
			stmt.setInt(4, -1);
			stmt.setString(5, sa_hash);
			stmt.setBoolean(6, false);
			stmt.executeUpdate();
			con.commit();
			System.out.println("Software Agent terminated");
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				System.err.println("Transaction rollback failed");
				e1.printStackTrace();
			}
			System.err.println("SQl Nmap storage for Software Agent terminate, failed - " + e.getMessage());
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
		
		

	public ArrayList<String> getSAsNmpaJobs(String sa_hash, Boolean assigned_value) {
		ArrayList<String> ret = new ArrayList<String>();

		Connection con = null;
		PreparedStatement stmt = null;
		MyConnection c = new MyConnection();
		con = c.getInstance();
		String	sql = "SELECT DISTINCT id,params,periodic,period FROM nmap_jobs WHERE SoftwareAgents_hash =?  AND assigned=?";
		
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sql);
			stmt.setString(1, sa_hash);
			stmt.setBoolean(2, assigned_value);
			rs = stmt.executeQuery();
				while (rs.next()) {
					Integer id = rs.getInt(1);
					String params = rs.getString(2);
					Boolean periodic = rs.getBoolean(3);
					Integer period = rs.getInt(4);
	
					String job = id.toString() + ',' + params + ',' + periodic.toString() + ',' + period.toString();
					ret.add(job);
			}
		} catch (SQLException e) {
			System.err.println("SQl select statement to get nmapjobs failed - " + e.getMessage());
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
		
		return ret;
	}

	
	
	public void setNmapJobAssigned(ArrayList<String> jobs, String sa_hash) {
		MyConnection c = new MyConnection();			// Updating sent nmapjobs' info
		Connection con = c.getInstance();
		PreparedStatement stmt = null;

		String sql = "UPDATE nmap_jobs SET assigned=? WHERE SoftwareAgents_hash=?";
		try {
			con.setAutoCommit(false);
			stmt = con.prepareStatement(sql);
			stmt.setBoolean(1, true);
			stmt.setString(2, sa_hash);
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
	
	
	public void setResults(int job_id, String result){
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
	
	
	public ArrayList<String> getResults(String sa_hash) {
		ArrayList<String> ret = new ArrayList<String>();
		Connection con = null;
		PreparedStatement stmt = null;
		MyConnection c = new MyConnection();
		con = c.getInstance();
		String sql = null;
		ResultSet rs = null;
		if(sa_hash.equals("0"))
		{
			sql = "SELECT Distinct(results) FROM results";
		}
		else
		{
			sql = "SELECT Distinct(results) FROM results,nmap_jobs_has_results,nmap_jobs,SoftwareAgents"
					+ " WHERE results.hash=nmap_jobs_has_results.results_hash"
					+ " AND nmap_jobs_has_results.nmap_jobs_id=nmap_jobs.id AND nmap_jobs.SoftwareAgents_hash=?";
		}
		
		try {
			stmt = con.prepareStatement(sql);
			if(!sa_hash.equals("0"))
			{
				stmt.setString(1, sa_hash);

			}
			rs = stmt.executeQuery();
			while (rs.next()) {
				String results = rs.getString(1);
				ret.add(results);
			}
		} catch (SQLException e) {
			System.err.println("SQl select statement to get results failed - " + e.getMessage());
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
		
		return ret;
	}
	
	public ArrayList<String> getFilteredResults(String sa_hash , String from , String to) 
	{
		ArrayList<String> ret = new ArrayList<String>();
		Connection con = null;
		PreparedStatement stmt = null;
		MyConnection c = new MyConnection();
		con = c.getInstance();
		String sql = null;
		ResultSet rs = null;
		if(sa_hash.equals("0"))
		{
			sql = "SELECT Distinct(results) "
				+ "FROM results, nmap_jobs_has_results,nmap_jobs "
				+ "WHERE results.hash=nmap_jobs_has_results.results_hash AND nmap_jobs_has_results.nmap_jobs_id=nmap_jobs.id "
				+ "AND nmap_jobs.insertion_time>? "
				+ "AND nmap_jobs.insertion_time<?";
		}
		else
		{
			sql = "SELECT Distinct(results) "
				+ "FROM results,nmap_jobs_has_results,nmap_jobs,SoftwareAgents "
				+ "WHERE results.hash=nmap_jobs_has_results.results_hash AND nmap_jobs_has_results.nmap_jobs_id=nmap_jobs.id "
				+ "AND nmap_jobs.SoftwareAgents_hash=? "
				+ "AND nmap_jobs.insertion_time>? "
				+ "AND nmap_jobs.insertion_time<?";
		}
		
		try {
			stmt = con.prepareStatement(sql);
			if(!sa_hash.equals("0"))
			{
				stmt.setString(1, sa_hash);	
				stmt.setString(2, from);
				stmt.setString(3, to);
			}
			else
			{
				stmt.setString(1, from);
				stmt.setString(2, to);
			}
			rs = stmt.executeQuery();
			while (rs.next()) {
				String results = rs.getString(1);
				ret.add(results);
			}
		} catch (SQLException e) {
			System.err.println("SQl select statement to get results failed - " + e.getMessage());
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
		
		return ret;
	}
	
	public void setoffline(String hash) {     //Set offline the sa
		Connection con = null;
		PreparedStatement stmt = null;
		MyConnection c = new MyConnection();
		con = c.getInstance();

		String sql = "UPDATE SoftwareAgents SET online=? WHERE hash=?";
		try {
			con.setAutoCommit(false);
			stmt = con.prepareStatement(sql);
			stmt.setBoolean(1, false);
			stmt.setString(2, hash);
			stmt.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				System.err.println("Transaction rollback failed");
				e1.printStackTrace();
			}
			System.err.println("SQl SA update  failed - " + e.getMessage());
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
