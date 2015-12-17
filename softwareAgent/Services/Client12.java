package softwareAgent.Services;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class Client12 {
	public static void main(String[] args) {
		
		Client c=Client.create();
		WebResource resource = c.resource("http://localhost:9998/softwareagent");
		ClientResponse response = resource.put(ClientResponse.class,"hash_key_12");
		if (response.getStatus() != 200) {
 		   throw new RuntimeException("Failed : HTTP error code : "
 			+ response.getStatus());
 		}
		System.out.println(response.getStatus());
	}
	
}