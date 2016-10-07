package dsAM.aggregatorManager.services;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import dsAM.aggregatorManager.*;
import dsAM.aggregatorManager.db.NmapJobDAO;

import java.sql.*;

/**
 * REST Web Service
 *
 * @author root
 */
@Path("/nmapjobs")
public class NmapJobResource {

	/**
	 * Creates a new instance of NmapJobtoSend
	 */
	public NmapJobResource() {

	}

	/**
	 * 
	 * @return json'ed nmap job
	 * @throws SQLException 
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getjob(@QueryParam("hash") String sa_hash){
		if(Main.v){
			System.out.println("Received nmapjob request from agent " +sa_hash);
			//interrupt the Timerthread
			int index= SoftwareAgentResource.gethashArrayIndex(sa_hash);
			SoftwareAgentResource.interruptthread(index);
		}
		ArrayList<String> job_lines=new ArrayList<String>();

		NmapJobDAO dao = new NmapJobDAO();
		job_lines = dao.getSAsNmpaJobs(sa_hash, false);
		dao.setNmapJobAssigned(job_lines, sa_hash);
		
		if(job_lines.isEmpty()){
			return "[]";
		} 
		
		return job_lines.toString();
	}
	
	
	@POST
	@Path("/{id}")
	@Consumes(MediaType.TEXT_PLAIN)
	public void postedResults(@PathParam("id")int job_id, String result) {
	
		NmapJobDAO dao = new NmapJobDAO();
		dao.setResults(job_id, result);
	}
	
	@GET
	@Path("/{hash}")
	@Consumes(MediaType.TEXT_PLAIN)
	public String getMobileJobs(@PathParam("hash")String hash) {
	
		ArrayList<String> job_lines=new ArrayList<String>();
		NmapJobDAO dao = new NmapJobDAO();
		job_lines = dao.getSAsNmpaJobs(hash, true);
		return job_lines.toString();
	}
	
	@PUT
	@Consumes(MediaType.TEXT_PLAIN)
	public Response putJob(@QueryParam("hash") String sa_hash, String job){
		if(Main.v){
			System.out.println("New nmapjob \""
				+job+ "\"assigned by an android client, for agent " +sa_hash);
		}
		String[] job_info = job.split(",");
		int period;
		if(job_info[2].equals("null")){
			period=0;
		}
		else{
			period = Integer.parseInt(job_info[2]);
		}
		NmapJobDAO dao = new NmapJobDAO();
		dao.addNmapJob(job_info[0], 
			Boolean.parseBoolean(job_info[1]),
			period,
			sa_hash);

		return Response.ok().build();
	}
	
	@DELETE
	@Path("/{id}")
	@Consumes(MediaType.TEXT_PLAIN)
	public void deleteJob(@PathParam("id")int job_id){
		NmapJobDAO dao = new NmapJobDAO();
		dao.stopCommand(job_id);
		System.out.println("Job with id: "+ job_id+" is going to delete");
	}
	
	@POST
	@Path("/rerun")
	@Consumes(MediaType.TEXT_PLAIN)
	public void rerunJob(String job){
		String[] jobArray= job.split(",");
		int job_id=Integer.parseInt(jobArray[0]);
		NmapJobDAO dao = new NmapJobDAO();
		dao.setNmapJobNotAssigned(job_id);
		System.out.println("Re-run job with id "+ job_id);
	}
	
}
