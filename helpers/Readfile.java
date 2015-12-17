package helpers;

import java.io.IOException;
import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.Files.newBufferedReader;

/**
 * 
 * @author tsaou
 */
public class Readfile {

	private BufferedReader jobs_reader;
	private Charset cs;
	private int total_job_lines;

	/**
	 * 
	 * @param jobs_file
	 *            The filename to read nmap jobs from
	 * @throws java.io.IOException
	 *             If an error occurs in oppening jobs file
	 */
	public Readfile(String jobs_file) {
		this.cs = Charset.forName("UTF-8");
		Path jobs_path = Paths.get(jobs_file);
		try {
			this.jobs_reader = newBufferedReader(jobs_path, cs); // Calculate
																	// and store
																	// total
																	// lines in
																	// dedicated
																	// field

			while (this.jobs_reader.readLine() != null) {
				this.total_job_lines++;
			}
			this.closeJobFile(); // Close and reopen Job File to reset
									// filepointer
			this.jobs_reader = newBufferedReader(jobs_path, cs);
		} catch (IOException e) {
			System.err.println("Failed to open Job File - " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void closeJobFile() {
		try {
			this.jobs_reader.close();
		} catch (IOException e) {
			System.err.println("Failed to close Job File - " + e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param lines_per_read
	 *            The number of lines to read from the Jobs File in a single
	 *            operation
	 * @throws IOException
	 * @return Array of lines read as strings
	 */
	public String[] readJobLines(int lines_per_read) throws IOException {
		int i;
		String[] job_lines = new String[lines_per_read];
		for (i = 0; i < lines_per_read; i++) {
			job_lines[i] = this.jobs_reader.readLine();
		}
		return job_lines;
	}

	public int getTotalJobLines() {
		return total_job_lines;
	}

}