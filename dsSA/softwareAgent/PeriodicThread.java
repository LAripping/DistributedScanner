package dsSA.softwareAgent;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.BlockingQueue;

import dsSA.softwareAgent.helpers.InputStreamtoString;
import dsSA.softwareAgent.helpers.NmapJob;


public class PeriodicThread implements Runnable {

	private NmapJob job;
	private final BlockingQueue<NmapJob> resultQueue;

	/**
	 *
	 * @param job The Nmap command object the thread will periodically execute
	 * @param resultQueue The shared queue object where completed jobs -along with their results- are being put
	 */
	public PeriodicThread(NmapJob job, BlockingQueue<NmapJob> resultQueue) {
		this.job = job;
		this.resultQueue = resultQueue;
	}


	public void run() {
		int count = 0;
		System.out.println("Periodic Thread with ID: " + Thread.currentThread().getId() + " took job # " + job.getId() );
		
		while (true) {									// Running job periodically
			Process proc;
			try {
				proc = Runtime.getRuntime().exec("nmap " + job.getParams());
				count++;
				System.out.println("Periodic Thread with ID: " + Thread.currentThread().getId() + " just ran job #" + job.getId() + ". This is loop #" + count);

				InputStream stdin = proc.getInputStream();
				InputStreamtoString is2s = new InputStreamtoString();
				job.setResults( is2s.getStringFromInput(stdin) );  
				
				try {
					resultQueue.put( job );
				} catch (InterruptedException e) {
					System.err.println("Interrupted wait to put result in queue, in periodic thread with ID: " + Thread.currentThread().getId() );
					return;
				}
			} catch (IOException e) {
				System.err.println("Failed to read results from nmap job in periodic thread with ID: " + Thread.currentThread().getId() );
				e.printStackTrace();
			}

			try {										// Sleep untill next run
				Thread.sleep(job.getPeriod() * 1000);
			} catch (InterruptedException e) {
				System.err.println("Interrupted sleep in periodic thread with ID: " + Thread.currentThread().getId());
				return;
			}
		}
	}
}