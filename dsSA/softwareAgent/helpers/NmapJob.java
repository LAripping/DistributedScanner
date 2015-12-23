package dsSA.softwareAgent.helpers;

/**
 *
 * @author root
 */
public class NmapJob {

	private int id;
	private String params;
	private boolean periodic;
	private int period;
	private String results;
	private long thread_id;

	/**
	 *
	 * @return Getter for the ID of this NmapJob
	 */
	public int getId() {
		return id;
	}

	/**
	 *
	 * @param id Setter for the ID of this NmapJob
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 *
	 * @return Getter for the parameters of this NmapJob
	 */
	public String getParams() {
		return params;
	}

	/**
	 *
	 * @param params Setter for the parameters of this NmapJob
	 */
	public void setParams(String params) {
		this.params = params;
	}

	/**
	 *
	 * @return Whether this NmapJob is periodic or not
	 */
	public boolean isPeriodic() {
		return periodic;
	}

	/**
	 *
	 * @param periodic Set whether this NmapJob will be periodic or not
	 */
	public void setPeriodic(boolean periodic) {
		this.periodic = periodic;
	}

	/**
	 *
	 * @return Getter for the period of this NmapJob
	 */
	public int getPeriod() {
		return period;
	}

	/**
	 *
	 * @param period Setter for the period of this NmapJob
	 */
	public void setPeriod(int period) {
		this.period = period;
	}

	/**
	 *
	 * @return Getter for the results of this NmapJob
	 */
	public String getResults() {
		return results;
	}

	/**
	 *
	 * @param results Setter for the results of this NmapJob
	 */
	public void setResults(String results) {
		this.results = results;
	}
	
	/**
	 *
	 * @return The ID of the thread running this NmapJob
	 */
	public long getThread_id() {
		return thread_id;
	}

	/**
	 *
	 * @param thread_id Remember the ID of the thread that will run this NmapJob
	 */
	public void setThread_id(long thread_id) {
		this.thread_id = thread_id;
	}

	/**
	 * 
	 * @param line Parse the line and form a NmapJob object holding all the relevant info
	 */
	public NmapJob(String line) {
		String[] temp = line.split(","); // Parse nmap job

		this.id = Integer.parseInt(temp[0]);
		this.params = temp[1];
		this.periodic = Boolean.parseBoolean(temp[2]);
		this.period = Integer.parseInt(temp[3]);
		//String job_token = temp[0].concat("," + params);
	}
	
	/**
	 *
	 * @param copy The NmapJob to copy
	 */
	public NmapJob(NmapJob copy){
		this.id = copy.id;
		this.params = copy.params;
		this.periodic = copy.periodic;
		this.period = copy.period;
	}

	/**
	 * 
	 * @return Whether this NmapJob contains a -oX parameter 
	 */
	public boolean hasXMLparam() {
		String[] temp2 = params.split(" ");				 // Looking for the -oX option
		for (String param : temp2) {
			if (param.equals("-oX") || param.equals("Stop")) { // Except if it's a "stop" command from AM
				return true;
			}
		}
		return false;
	}

	/**
	 *
	 * @return Whether it's a termination command for this SA
	 */
	public boolean checkExitCommand() {
		return (/*this.id == -1 && */this.params.equals("exit(0)") && this.periodic && this.period == -1);
	}

	/**
	 *
	 * @return Whether it's a stop command for this periodic NmapJob
	 */
	public boolean checkStopCommand() {
		return (this.params.equals("Stop") && this.periodic);
	}
}