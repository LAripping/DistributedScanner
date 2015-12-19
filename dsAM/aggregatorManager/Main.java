
package dsAM.aggregatorManager;

import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;

import dsAM.aggregatorManager.db.MyConnection;
import dsAM.aggregatorManager.gui.LogInForm;

import org.glassfish.grizzly.http.server.HttpServer;

import javax.ws.rs.core.UriBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Properties;

public class Main {

	public static Boolean v;
	public static int nmapjob_request_interval;

	private static int getPort(int defaultPort) {
		//grab port from environment, otherwise fall back to default port 9998
		String httpPort = System.getProperty("jersey.test.port");
		if (null != httpPort) {
			try {
				return Integer.parseInt(httpPort);
			} catch (NumberFormatException e) {
			} 
		} 
		return defaultPort;
	}    

	private static URI getBaseURI() {
		return UriBuilder.fromUri("http://localhost/").port(getPort(9998)).build();
	}

	public static final URI BASE_URI = getBaseURI();

	public static HttpServer startServer() throws IOException {
		ResourceConfig resourceConfig = new PackagesResourceConfig("dsAM.aggregatorManager");

		System.out.println("Starting grizzly2...");
		return GrizzlyServerFactory.createHttpServer(BASE_URI, resourceConfig);
	}

	public static void main(String[] args) throws IOException {
		
		Properties defaults = new Properties();					// Set default behavior
		defaults.setProperty("Verbose", "true");
		defaults.setProperty("NmapJobRequestInterval", "10");
		defaults.setProperty("DBuser","root");
		defaults.setProperty("DBpass","");

		Properties config = new Properties(defaults);				// Configure app with default behavior
		if(args.length!=0){								
			try {											
				config.load(new FileInputStream(args[0]));		// Configure app with user-parameters (if any) 
				System.out.println("Property file loaded succesfully");
			} catch (IOException e) {
				System.err.println("Failed to load properties from given file - " + e.getMessage());
				e.printStackTrace();
			}
		}			 						// Parsing properties

		v = Boolean.parseBoolean( config.getProperty("Verbose") );
		nmapjob_request_interval = Integer.parseInt( config.getProperty("NmapJobRequestInterval") );
		String db_user = config.getProperty("DBuser");
		String db_pass = config.getProperty("DBpass");
		MyConnection.setConnectionParameters(db_user, db_pass);
		
		if(v){
			System.out.println("Configuration used:");
			config.list(System.out);
			System.out.println("-- done listing properties -- \n");
		}
		synchronized(Thread.currentThread()){
			try {
				new LogInForm(Thread.currentThread()).setVisible(true);
				Thread.currentThread().wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		HttpServer httpServer = startServer();
		System.out.println("Aggregator Manager server started. Hit enter to stop it...");
		
		System.in.read();
		httpServer.stop();

	}    
}