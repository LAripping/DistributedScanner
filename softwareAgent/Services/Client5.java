package softwareAgent.Services;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class Client5 {
	public static void main(String[] args){
		Client c=Client.create();
		WebResource resource = c.resource("http://localhost:9998/nmapjobtosend/results");
		resource.post(ClientResponse.class,"results string");
	}
}
