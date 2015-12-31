package dsSA.softwareAgent.services;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 *
 * @author root
 */
public class NmapJob_result {
	String result;
	String am_url;
	int id;
	
	/**
	 *
	 * @param result The actual result from the execution of the NmapJob
	 * @param id The ID of the NmapJob executed
	 * @param am_url The URL of the AggregatorManager to send the results to
	 */
	public NmapJob_result(String am_url, String result,int id) {
		this.am_url = am_url;
		this.result=result;
		this.id=id;
	}

	public void SendResult() {
		Client c=Client.create();
		WebResource resource = c.resource(am_url+"/nmapjobs/"+id);
		resource.post(ClientResponse.class,result);
	}
	
}