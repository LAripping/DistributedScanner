package softwareAgent.Services;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class Client34 {
	public static void main(String[] args){
		Client c=Client.create();
		WebResource resource=c.resource("http://localhost:9998/nmapjobtosend/hash_key_34"); //+hash_key
		ClientResponse response = resource.accept("application/json").get(ClientResponse.class);

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatus());
		}
		
		String output = response.getEntity(String.class);
		output=output.replace("[","");
		output=output.replace("]","");
		System.out.println(output);
		String[] job_lines=output.split(", ");
		System.out.println(job_lines[0]);
		System.out.println(job_lines[1]);
		System.out.println(job_lines[2]);
	}
}

