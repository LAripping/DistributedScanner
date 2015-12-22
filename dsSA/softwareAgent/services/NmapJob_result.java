package dsSA.softwareAgent.services;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class NmapJob_result {
	String result;
	int id;
	
	public NmapJob_result(String result,int id) {
		this.result=result;
		this.id=id;
	}
	public void SendResult() {
		Client c=Client.create();
		WebResource resource = c.resource("http://localhost:9998/nmapjobs/" +id);
		resource.post(ClientResponse.class,result);
	}
	
}
