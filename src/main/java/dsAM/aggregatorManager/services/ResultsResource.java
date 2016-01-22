package dsAM.aggregatorManager.services;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
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
		
		return list.toString();
	}

}
