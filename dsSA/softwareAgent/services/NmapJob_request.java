package dsSA.softwareAgent.services;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import dsSA.softwareAgent.Main;

/**
 *
 * @author root
 */
public class NmapJob_request {

	private String am_url;
	private String all_info_hash;
	private String[] job_lines = null;

	/**
	 *
	 * @param am_url The URL of the AggregatorManager to send requests to
	 * @param all_info_hash The hash of this SA to include in the requests
	 */
	public NmapJob_request(String am_url, String all_info_hash) {
		this.am_url = am_url;
		this.all_info_hash = all_info_hash;
	}

	/**
	 *
	 * @return An array of string-encoded NmapJobs received as a response
	 */
	public String[] SendRequest() {
		Client c = Client.create();
		WebResource resource = c.resource(am_url + "/nmapjobs?hash=" + all_info_hash);
		ClientResponse response = resource.accept("text/plain").get(ClientResponse.class);
		// Include SA's hash in request
		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}

		String output = response.getEntity(String.class);		// Check manager's response
		if (output.equals("[]")) {
			if (Main.v) {
				System.out.println("No nmapjobs returned");
			}
			job_lines = new String[0];
			return job_lines;
		} else {										// Parse nmapjob list
			output = output.replace("[", "");
			output = output.replace("]", "");
			job_lines = output.split(", ");
			return job_lines;
		}
	}

}
