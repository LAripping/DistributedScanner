package dsAM.aggregatorManager.services;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import dsAM.aggregatorManager.db.NmapJobDAO;;


@Path("/results")
public class ResultsResource {
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getResults(){
		ArrayList<String> list=new ArrayList<String>();
		NmapJobDAO dao = new NmapJobDAO();
		list = dao.getResults("0");
		if(list.isEmpty()){
			return "[]";
		} 
		StringBuilder sb = new StringBuilder();
		for(String single_res : list) sb.append(single_res + "</ResultEndsHere>");
		return sb.toString();
	}
	
	@GET
	@Path("/{hash}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getSaResults(@PathParam("hash")String hash){
		ArrayList<String> list=new ArrayList<String>();
		NmapJobDAO dao = new NmapJobDAO();
		list = dao.getResults(hash);
		if(list.isEmpty()){
			return "[]";
		} 
		StringBuilder sb = new StringBuilder();
		for(String single_res : list) sb.append(single_res + "</ResultEndsHere>");
		return sb.toString();
	}

}
