package softwareAgent;


import helpers.Readfile;
import helpers.ShutdownHook;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author tsaou
 */
public class Main {

	/**
	 *
	 * @param args Command line arguments. Only the path to the property file is
	 * needed. If not provided "properties.txt" in the current directory is used by default. 
	 * needed and the user is prompted if it is not provided
	 *
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		int i;
		Path prop = null;

		if(args.length==0){								// Open property file
			prop = Paths.get( "properties.txt" );
		} else{
			try {											
				prop = Paths.get(args[0]);
			} catch (InvalidPathException e) {
				System.err.println("Invalid path for property file - " + e.getMessage());
				e.printStackTrace();
			}
		}

		Readfile rf = new Readfile(prop);
		try {
			rf.readProperties( true );						// Call method with FALSE parameter if jobs are not to be retrieved from a file
		} catch (IOException e) {
			rf.closeJobFile();
			System.err.println("Failed to read property file - " + e.getMessage());
			e.printStackTrace();
		} 
		
		int lines_per_read=3, pool_size=3;
		ArrayList<String> properties = rf.getProperties();			// Extract and set parameters from properties
		for(String p : properties){
			if( p.matches("Lines per Read : (.*)") ){
				lines_per_read = Integer.parseInt( p.substring(17) );
				System.out.println("Property \"Lines per Read\" recognized. Set to " + lines_per_read);
			} else if( p.matches("Pool Size : (.*)") ){
				pool_size = Integer.parseInt( p.substring(12) );
				System.out.println("Property \"Pool Size\" recognized. Set to " + pool_size);
			} else{
				System.err.println("Unrecognized property \"" + p + "\"");
			}
		}
		

		BlockingQueue<NmapJob> jobQueue = new LinkedBlockingQueue<>();
		BlockingQueue<NmapJob> resultQueue = new LinkedBlockingQueue<>();

		ArrayList<Thread> threadHotel = new ArrayList<>() ;
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


		int rem_lines = rf.getTotalJobLines();
		while (rem_lines > 0) {
			String[] job_lines = null;						// Array containing a block of nmap jobs 

			try{
				job_lines = rf.readJobLines(lines_per_read);
			} catch (IOException e) {
				rf.closeJobFile();
				System.err.println("Failed to read block of jobs - " + e.getMessage());
				e.printStackTrace();
			} 		

			for (i = 0; i < job_lines.length; i++) {
				if (job_lines[i] != null) {

					NmapJob job = new NmapJob(job_lines[i]); 
					if (!job.hasXMLparam()) {
						System.err.println("Oops, job #" + job.getId() + "does not contain the -oX parameter!");
						continue;
					}

					if (job.isPeriodic()) {
						Thread pj = new Thread(new PeriodicThread(job, resultQueue));
						pj.start();

						threadHotel.add(pj);
					} else {
						try {
							jobQueue.put(job);
						} catch (InterruptedException e) {
							System.err.println("Interrupted wait to put job in queue");
							e.printStackTrace();
						}
					}
				}
			}
			rem_lines = rem_lines - lines_per_read;
		}
		rf.closeJobFile();
	}
}
