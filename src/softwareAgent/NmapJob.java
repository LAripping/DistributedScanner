package softwareAgent;

/**
 *
 * @author tsaou
 */
public class NmapJob {

	private int id;
	private String params;
	private boolean periodic;
	private int period;
	private String results;


	
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

	/**
	 *
	 * @param line
	 */
	public NmapJob(String line) {
		String[] temp = line.split(",");			// Parse nmap job

		this.id = Integer.parseInt(temp[0]);
		this.params = temp[1];
		this.periodic = Boolean.parseBoolean(temp[2]);
		this.period = Integer.parseInt(temp[3]);
		String job_token = temp[0].concat("," + params);
	}

	/**
	 *
	 * @return
	 */
	public boolean hasXMLparam() {
		String[] temp2 = params.split(" ");		// Looking for the -oX option	
		for (String param : temp2) {
			if (param.equals("-oX")) {
				return true;
			}
		}
		return false;
	}
	
	

}
