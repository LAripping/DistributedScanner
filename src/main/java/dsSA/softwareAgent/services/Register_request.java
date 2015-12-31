package dsSA.softwareAgent.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import dsSA.softwareAgent.Main;
import dsSA.softwareAgent.helpers.InputStreamtoString;
import dsSA.softwareAgent.helpers.NIFtester;

public class Register_request {

	String am_url;
	String all_info_hash;
	String all_info;

	/**
	 *
	 * @param am_url The URL of the AggregatorManager to send registration requests to
	 */
	public Register_request(String am_url) {
		this.am_url = am_url;
	}

	/**
	 *
	 * @return Getter for the SA's hash 
	 * @throws SocketException 
	 */
	public String getHash() throws SocketException {
		StringBuilder sb=null;
		
		
		NIFtester nifTester = new NIFtester();
		NetworkInterface nif=null;
		nif = nifTester.getInternetNIF();
		InetAddress ip = nifTester.getIp();

		if (nif == null || ip == null) {
			if(Main.v){
				System.out.println("Using loopback address...");
			}
			nif = null;
			try {
				ip = InetAddress.getLocalHost();
			} catch (UnknownHostException ex) {
				ex.printStackTrace();
			}
		} else {
			ip = nifTester.getIp();
			sb = new StringBuilder(18);
			for (byte b : nif.getHardwareAddress()) {
				if (sb.length() > 0) {
					sb.append(':');
				}
				sb.append(String.format("%02x", b));
			}
		}
		Process nmap = null;
		try {
			nmap = Runtime.getRuntime().exec("nmap -V localhost");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		InputStream stdin = nmap.getInputStream();
		InputStreamtoString is2s = new InputStreamtoString();
		String[] command_results = is2s.getStringFromInput(stdin).split(" ", 4);

													// Registration info:
		String device_name = ip.getHostName();					// i.
		String interface_ip = ip.getHostAddress();					// ii.
		String interface_mac = (nif == null ? "--NO-NIFF--" : sb.toString());	// iii.
		String os_version = System.getProperty("os.version");			// iv.
		String nmap_version = command_results[2];				// v.

		all_info = device_name + '|' + interface_ip + '|'
			+ interface_mac + '|' + os_version + '|' + nmap_version;
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			System.err.println("Failed to digest SA info for Register request - " + e.getMessage());
			e.printStackTrace();
		}
		try {
			md.update(all_info.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
		}
		byte[] digest = md.digest();
		byte[] random_bytes = new byte[digest.length];			// Add a random number to ensure different SAs from same PC 
		new Random().nextBytes(random_bytes);
		for(int i=0; i<digest.length; i++){
			digest[i] ^= random_bytes[i];
		}
		java.math.BigInteger hash_int = new java.math.BigInteger(1, digest);
				
		all_info_hash = String.format("%064x", hash_int);
		System.out.println( all_info_hash + digest.length);

		return all_info_hash;
	}

	/**
	 *
	 * @return Whether registration was accepted or not
	 * @throws SocketException
	 */
	public boolean SendRegister() throws SocketException {

		String register_request = all_info + '|' + all_info_hash; 
		if (Main.v) {
			System.out.println("Registration request to be sent:\n" + register_request);
		}
		

		Client c = Client.create();
		WebResource resource = c.resource(am_url + "/softwareagent");
		ClientResponse response = resource.put(ClientResponse.class, register_request);
		if (response.getStatus() != 200) {				// Except 200 - OK status code
			if(Main.v){
				System.out.println("Registration request rejected (status code:" + response.getStatus() + " ), waiting to resend...");
			}
			return false;
		} else {
			if(Main.v){
				System.out.println("Registration request accepted!");
			}
			return true;
		}
	}

}