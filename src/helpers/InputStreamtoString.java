package helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author Apostolis
 */
public class InputStreamtoString {

	/**
	 *
	 * @param stream The input stream to convert.
	 * Currently used: stdin, to get the results of "nmap" commands in strings
	 * @return Returns the ouyput of nmap commands in String format
	 */
	public String getStringFromInput(InputStream stream) {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		String line;

		try {
			br = new BufferedReader(new InputStreamReader(stream));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			System.err.println("Failed to parse results to string");
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException ioe) {
					System.err.println("Failed to close broken BufferedReader");
					ioe.printStackTrace();
				}
			}
		}
		return sb.toString();
	}

}
