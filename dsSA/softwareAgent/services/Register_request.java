package dsSA.softwareAgent.services;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class Register_request {
	String am_url;
	String request;
	
	public Register_request(String am_url,String request) {
		this.am_url=am_url;
		this.request=request;
	}
	
	public boolean SendRegister(){
		Client c=Client.create();
		WebResource resource = c.resource(am_url + "/softwareagent");
		ClientResponse response = resource.put(ClientResponse.class,request);
		if (response.getStatus() != 200) {				// Except 200 - OK status code
			System.out.println("Registration request rejected (status code:" + response.getStatus() +" ), waiting to resend..." );
			return false;
 		}
		else{
			System.out.println("Registration request accepted!");
			return true;
		}
	}

}
