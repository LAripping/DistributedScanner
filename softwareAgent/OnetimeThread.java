package softwareAgent;


import helpers.NmapJob;
import helpers.InputStreamtoString;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.BlockingQueue;

public class OnetimeThread implements Runnable {

	private final BlockingQueue<NmapJob> jobQueue;
	private final BlockingQueue<NmapJob> resultQueue;

	/**
	 *
	 * @param jq The shared queue object where undone jobs are being put
	 * @param rq The shared queue object where completed jobs -along with their results- are being put
	 * 
	 */
	public OnetimeThread(BlockingQueue<NmapJob> jq, BlockingQueue<NmapJob> rq) {
		this.jobQueue = jq;
		this.resultQueue = rq;
	}

	
	public void run() {
		System.out.println("Onetime Thread with ID: " + Thread.currentThread().getId() + " started");

		while (true) {
			NmapJob job = null;

			try {
				job = jobQueue.take();						// Block here witing for next job_token if queue empty
			} catch (InterruptedException e) {
				System.err.println("Interrupted wait to take job from queue, in Onetime thread with ID: " + Thread.currentThread().getId());
				return;
			}

			if (job == null) {
				System.err.println("Oops, null job retrieved from queue, in Onetime thread with ID: " + Thread.currentThread().getId());
				System.exit(1);
			} else {
				System.out.println("Onetime Thread with ID: " + Thread.currentThread().getId() + " took job #" + job.getId() + " from queue ");
				Process proc;
				try {										// Running job_token
					proc = Runtime.getRuntime().exec("nmap " + job.getParams());
					InputStream stdin = proc.getInputStream();
					InputStreamtoString is2s = new InputStreamtoString();
					job.setResults(  is2s.getStringFromInput(stdin) );
					
					try {
						resultQueue.put( job );			// Block here waiting for space to become available in result queue
					} catch (InterruptedException e) {
						System.err.println("Interrupted wait to put result in queue, in Onetime thread with ID: " + Thread.currentThread().getId());
						return;
					}
				} catch (IOException e) {
					System.err.println("Failed to read results from nmap job, in Onetime thread with ID: " + Thread.currentThread().getId());
					e.printStackTrace();
				}
			}
		}

	}
}