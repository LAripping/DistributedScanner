package dsSA.softwareAgent;

import java.util.concurrent.BlockingQueue;
import dsSA.softwareAgent.helpers.NmapJob;
import dsSA.softwareAgent.services.NmapJob_result;

/**
 *
 * @author root
 */
public class Sender implements Runnable {

	private final BlockingQueue<NmapJob> resultQueue;
	private boolean am_exists;
	private String am_url;

	/**
	 *
	 * @param rq The shared queue object where completed jobs -along with their results- are being put
	 * @param am_exists Whether an Aggregatormanager exists to send results to
	 * @param am_url The URL of the AggregatorManager to send the results to
	 */
	public Sender(BlockingQueue<NmapJob> rq, boolean am_exists, String am_url) {
		this.resultQueue = rq;
		this.am_exists = am_exists;
		this.am_url = am_url;
	}


	public void run() {

		if(Main.v){
			System.out.println("Started sender thread, ID:" + Thread.currentThread().getId());
		}
		while (true) {
			NmapJob job = null;
			
			try {
				job = resultQueue.take();		//Block here waiting for results from queue
			} catch (InterruptedException e) {
				System.err.println("Interrupted wait to take result from queue, in sender thread");
				return;
			}
										// Print results in screen 	
			if(!am_exists){
				System.out.println("Result from job #" + job.getId() + " retrieved: \n" + job.getResults());
			}
			
			NmapJob_result job_res = new NmapJob_result( am_url,job.getResults(),job.getId() );
			job_res.SendResult();
		}
	}
}
