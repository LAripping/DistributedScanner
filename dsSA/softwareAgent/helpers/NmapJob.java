package dsSA.softwareAgent.helpers;

public class NmapJob {

	private int id;
	private String params;
	private boolean periodic;
	private int period;
	private String results;
	private long thread_id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public boolean isPeriodic() {
		return periodic;
	}

	public void setPeriodic(boolean periodic) {
		this.periodic = periodic;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public String getResults() {
		return results;
	}

	public void setResults(String results) {
		this.results = results;
	}
	
	public long getThread_id() {
		return thread_id;
	}

	public void setThread_id(long thread_id) {
		this.thread_id = thread_id;
	}

	/**
	 * 
	 * @param line
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
	 * @return
	 */
	public boolean hasXMLparam() {
		String[] temp2 = params.split(" "); // Looking for the -oX option
		for (String param : temp2) {
			if (param.equals("-oX") || param.equals("Stop")) { // Except if it's a "stop" command from AM
				return true;
			}
		}
		return false;
	}

	public boolean checkExitCommand() {
		return (/*this.id == -1 && */this.params.equals("exit(0)") && this.periodic && this.period == -1);
	}

	public boolean checkStopCommand() {
		return (this.params.equals("Stop") && this.periodic);
	}
}