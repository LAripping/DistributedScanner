package softwareAgent;

import java.util.concurrent.BlockingQueue;


public class Sender implements Runnable {

	private final BlockingQueue<NmapJob> resultQueue;

	/**
	 *
	 * @param rq The shared queue object where completed jobs -along with their results- are being put
	 */
	public Sender(BlockingQueue<NmapJob> rq) {
		this.resultQueue = rq;
	}


	@Override
	public void run() {

		System.out.println("Started sender thread, ID:" + Thread.currentThread().getId());
		while (true) {
			NmapJob job = null;
			
			try {
				job = resultQueue.take();		//Block here waiting for results from queue
			} catch (InterruptedException e) {
				System.err.println("Interrupted wait to take result from queue, in sender thread");
				return;
			}

										// Print results in screen 	
			System.out.println("Result from job #" + job.getId() + " retrieved: \n" + job.getResults());
		}
	}
}
