package dsSA.softwareAgent;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import dsSA.softwareAgent.helpers.*;
import dsSA.softwareAgent.services.Register_request;


/**
 *
 * @author tsaou
 */
public class Main {

	/**		
	 *
	 * @param args Command line arguments. Only the path to the property file is
	 * needed. If not provided default properties are used. See README for details 
	 *
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		int i;
		String all_info_hash = null;

		Properties defaults = new Properties();					// Set default behavior
		defaults.setProperty("Verbose","true");
		defaults.setProperty("AMexists","true");
		defaults.setProperty("JobsFile", "jobs.txt");
		defaults.setProperty("LinesPerRead", "3");
		defaults.setProperty("PoolSize", "3");
		defaults.setProperty("NmapJobRequestInterval", "10");
		defaults.setProperty("RegisterRequestInterval", "5");
		defaults.setProperty("AMurl", "http://localhost:9998");
		
		
		Properties config = new Properties(defaults);				// Configure app with default behavior
		if(args.length!=0){								
			try {											
				config.load(new FileInputStream(args[0]));		// Configure app with user-parameters (if any) 
				System.err.println("Property file loaded succesfully");
			} catch (IOException e) {
				System.err.println("Failed to load properties from given file - " + e.getMessage());
				e.printStackTrace();
			}
		}											// Parsing properties
												
		boolean v = Boolean.parseBoolean( config.getProperty("Verbose") );
		boolean am_exists = Boolean.parseBoolean( config.getProperty("AMexists") );
		int pool_size = Integer.parseInt( config.getProperty("PoolSize") );
		String jobs_file= 
			(am_exists ? null : config.getProperty("JobsFile") );
		String am_url= 
			(am_exists ? config.getProperty("AMurl") : null );
		int lines_per_read = 
			(am_exists ? -1 : Integer.parseInt( config.getProperty("LinesPerRead") ) );
		int job_request_interval = 
			(am_exists ? Integer.parseInt( config.getProperty("NmapJobRequestInterval") ) : -1);
		int register_request_interval = 
			(am_exists ? Integer.parseInt( config.getProperty("RegisterRequestInterval") ) : -1);
		if(v){
			System.out.println("Configuration used:");
			config.list(System.out);
			System.out.println("-- done listing properties -- \n");
		}
		
		
		StringBuilder sb=null;
		Readfile rf = (am_exists ? null : new Readfile(jobs_file) );
		if(am_exists){									// Gather registration info
			NIFtester nifTester = new NIFtester();
			NetworkInterface nif = nifTester.getInternetNIF();
			InetAddress ip = nifTester.getIp();
			
			if (nif==null || ip==null){
				System.out.println("Using loopback address...");
				nif=null;
				ip=InetAddress.getLocalHost();
			}
			else{
				ip = nifTester.getIp();
				sb = new StringBuilder(18);
				for (byte b : nif.getHardwareAddress()) {
					if (sb.length() > 0) {
						sb.append(':');
					}
					sb.append(String.format("%02x", b));
				}
			}
			Process nmap = Runtime.getRuntime().exec("nmap -V localhost");
			InputStream stdin = nmap.getInputStream();
			InputStreamtoString is2s = new InputStreamtoString();
			String[] command_results = is2s.getStringFromInput(stdin).split(" ", 4);
			
											// Registration info:
			String device_name = ip.getHostName();				// i.
			String interface_ip = ip.getHostAddress();			// ii.
			String interface_mac = (nif==null ? "--NO-NIFF--" : sb.toString() );				// iii.
			String os_version = System.getProperty("os.version");		// iv.
			String nmap_version = command_results[2];			// v.

			String all_info = device_name + '|' + interface_ip + '|'
				+ interface_mac + '|' + os_version + '|' + nmap_version;
			MessageDigest md = null;
			try {
				md = MessageDigest.getInstance("SHA-256");
			} catch (NoSuchAlgorithmException e) {
				System.err.println("Failed to digest SA info for Register request - " + e.getMessage());
				e.printStackTrace();
			}
			md.update(all_info.getBytes("UTF-8"));
			byte[] digest = md.digest();
			all_info_hash = String.format("%064x", new java.math.BigInteger(1, digest));
			
			String register_request = all_info + '|' + all_info_hash;
			if(v){
				System.out.println("Registration request to be sent:\n" + register_request);
			}	
			
			do{									// Request registration
				Register_request reg_req= new Register_request(am_url,register_request);
				if (reg_req.SendRegister()) {				// Except 200 - OK status code
					if(v){
						break;
					}
		 			
				}
				try {										// Wait for some time, then retry registering
					Thread.sleep( register_request_interval*1000 );	
				} catch (InterruptedException ex) {
					System.err.println("Interrupted main thread while sleeping between register requests - " + ex.getMessage());
					ex.printStackTrace();
				}
			}while( true );
		}
		
		BlockingQueue<NmapJob> jobQueue = new LinkedBlockingQueue<NmapJob>();
		BlockingQueue<NmapJob> resultQueue = new LinkedBlockingQueue<NmapJob>();
		ArrayList<Thread> threadHotel = new ArrayList<Thread>();
		ArrayList<NmapJob> jobhotel = new ArrayList<NmapJob>();		// List used to keep the id of the thread assigned, to find when stop command arrives
		ShutdownHook handler = new ShutdownHook(jobQueue, resultQueue, threadHotel);
		handler.attach();									// Prepare for shutdown procedures

		Thread tst = new Thread(new Sender(resultQueue));
		tst.start();										// Start sender thread
		threadHotel.add(tst);								
													
		Thread[] tot = new Thread[pool_size];
		for (i = 0; i < pool_size; i++) {							// Allocate One-Time-Thread-Pool
			tot[i] = new Thread(new OnetimeThread(jobQueue, resultQueue));
			tot[i].start();
			threadHotel.add(tot[i]);
		}


		int rem_lines = (am_exists ? -1 : rf.getTotalJobLines());
		while(true){									// Keep getting job-blocks untill the "break"
			String[] job_lines = null;						
			
			
			if(am_exists){								// Get a block of nmap jobs 
				Client c=Client.create();
				WebResource resource=c.resource(am_url + "/nmapjobs?hash="+all_info_hash);
				ClientResponse response = resource.accept("text/plain").get(ClientResponse.class);
												// Include SA's hash in request
				if (response.getStatus() != 200) {
					if(v){
						throw new RuntimeException("Failed : HTTP error code : "+ response.getStatus());
					}	
							
				}
				
				String output = response.getEntity(String.class);		// Check manager's response
				if(output.equals("[]")){
					System.out.println("No nmapjobs returned");								
					job_lines = new String[0];	
				}
				else {								// Parse nmapjob list
					output=output.replace("[","");
					output=output.replace("]","");
					System.out.println(output);		////////!!!!!!
					job_lines=output.split(", ");	
				}
			} else{
				if(rem_lines <= 0){						// Read nmapjobs from file
					if( !am_exists) rf.closeJobFile();
					break;
				}
				try{
					job_lines = rf.readJobLines(lines_per_read);
				} catch (IOException e) {
					rf.closeJobFile();
					System.err.println("Failed to read block of jobs - " + e.getMessage());
					e.printStackTrace();
				} 		
				rem_lines = rem_lines - lines_per_read;
			}
			
			for (i = 0; i < job_lines.length; i++) {					// Process each job in the block
				if(job_lines[i]==null) break;
				
				NmapJob job = new NmapJob(job_lines[i]); 
				jobhotel.add(job);
				
				if( job.checkExitCommand() ){
					System.err.println("Received exit command... Terminating");
					System.exit(0);
				}

				if (!job.hasXMLparam()) {
					System.err.println("Oops, job #" + job.getThread_id() + " does not contain the -oX parameter!");
					continue;
				}
				if (job.isPeriodic()) {
					if( job.checkStopCommand() ){
						
						for(NmapJob njob : jobhotel) {
							if(job.getId()==njob.getId()){
								job.setThread_id(njob.getThread_id());
								break;
							}
						}
						System.err.println("Received stop command for periodic thread #" + job.getThread_id() );
						try {
							for (Thread thre : threadHotel) {
								if(thre.getId()==job.getThread_id()){
									thre.interrupt();
									thre.join();
									break;
								}
							}
						} catch (InterruptedException ex) {
							System.err.println("Periodic thread commanded to stop was interrupted while blocked!");
							ex.printStackTrace();
						}
					}
					else {	
						Thread pj = new Thread(new PeriodicThread(job, resultQueue));
						pj.start();
						threadHotel.add(pj);
						job.setThread_id(pj.getId());
					}
				} else {
					try {
						jobQueue.put(job);
					} catch (InterruptedException e) {
						System.err.println("Interrupted wait to put job in queue");
						e.printStackTrace();
					}
				}
			}
			
			if(am_exists){									// Wait for some time before asking for more jobs
				try {									
					Thread.sleep( job_request_interval*1000 );	
				} catch (InterruptedException ex) {
					System.err.println("Interrupted main thread while sleeping between job requests - " + ex.getMessage());
					ex.printStackTrace();
				}
			}
			
			
		}
	}
}