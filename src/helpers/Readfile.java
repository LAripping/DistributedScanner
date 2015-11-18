package helpers;

import java.io.IOException;
import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import static java.nio.file.Files.newBufferedReader;

/**
 *
 * @author Apostolis
 */
public class Readfile {
	private Path prop_path;
	private Path jobs_path;
	private BufferedReader jobs_reader;
	private ArrayList<String> properties;
	private Charset cs;
	private int total_job_lines;
	
	
	
	/**
	 *
	 * @param prop_path The path to the property file as specified in command line arguments
	 */
	public Readfile(Path prop_path ) {
		this.prop_path = prop_path;
		this.jobs_path = null;
		this.cs = Charset.forName("UTF-8");
	}
	

	public void closeJobFile(){
		try {
			this.jobs_reader.close();
		} catch (IOException e) {
			System.err.println("Failed to close Job File - " + e.getMessage());
			e.printStackTrace();
		}
	}
	

	/**
	 *
	 * @param jobs_from_file Boolean predicate specifying the existance of an AM module to send Job requests
	 * @throws IOException
	 */
	public void readProperties( boolean jobs_from_file ) throws IOException {
		System.out.println("Reading properties from file \"" + prop_path.toString() + "\"... ");
	
		this.properties = (ArrayList<String>) Files.readAllLines(prop_path, cs);	
														// Store properties in the dedicated field
		for(String property : properties){
			if( property.matches("Jobs File : (.*)") ){					// Find Job File's path in properties 
				this.jobs_path = Paths.get( property.substring( 12  ));
				properties.remove( property );					// Job File existance is transparent to Main!
				break;
			}
		}
		
		if(jobs_from_file){
			if(jobs_path==null){									
				System.err.println("Job file not specified in property file!");
				System.exit(1);
			} else{
				System.out.println("Reading jobs from file \"" + jobs_path.toString() + "\"... ");
			}
	
			this.jobs_reader = newBufferedReader(jobs_path,cs);			// Calculate and store total lines in dedicated field
			while(this.jobs_reader.readLine()!=null ) this.total_job_lines++;

			this.closeJobFile();								// Close and reopen Job File to reset filepointer
			this.jobs_reader = newBufferedReader(jobs_path,cs);
		}
	}	

	
	/**
	 *
	 * @param lines_per_read The number of lines to read from the Jobs File in a single operation
	 * @throws IOException
	 * @return Array of lines read as strings
	 */
	public String[] readJobLines(int lines_per_read) throws IOException{
		int i;
		String[] job_lines = new String[lines_per_read];
		for(i=0; i< lines_per_read;i++){
			job_lines[i] = this.jobs_reader.readLine();
		}
		return job_lines;
	}

	
	
	public int getTotalJobLines() {
		return total_job_lines;
	}

	public ArrayList<String> getProperties() {
		return properties;
	}
	
	

}
