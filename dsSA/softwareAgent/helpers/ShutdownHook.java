package dsSA.softwareAgent.helpers;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

/**
 * 
 * @author petros
 */
public class ShutdownHook {

	private final BlockingQueue<NmapJob> jobQueue;
	private final BlockingQueue<NmapJob> resultQueue;
	private final ArrayList<Thread> threadHotel;

	/**
	 * 
	 * @param jq
	 *            The shared queue object where undone jobs are being put
	 * @param rq
	 *            The shared queue object where completed jobs -along with their
	 *            results- are being put
	 * @param th
	 *            The structure containing all t objects to join them when
	 *            termination signal is caught.
	 */
	public ShutdownHook(BlockingQueue<NmapJob> jq, BlockingQueue<NmapJob> rq,
			ArrayList<Thread> th) {
		this.jobQueue = jq;
		this.resultQueue = rq;
		this.threadHotel = th;
	}

	/**
	 *
	 */
	public void attach() {
		System.out.println("Shutdown hook attached!");

		Runtime.getRuntime().addShutdownHook(new Thread() {

			@Override
			public void run() {
				if (jobQueue.isEmpty() && resultQueue.isEmpty()) {
					System.out
							.println("Structures were already empty - All jobs carried out!");
				}
				jobQueue.clear();
				resultQueue.clear();

				for (Thread t : threadHotel) {
					if (t.isAlive()) {
						try {
							t.interrupt();
							t.join();
						} catch (InterruptedException ex) {
							System.err
									.println("Thread was interrupted while blocked!");
							ex.printStackTrace();
						}
					}
				}
				System.out.println("All threads have been terminated");
			}
		});

	}

}
