package aggregatorManager;

import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;

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
        ResourceConfig resourceConfig = new PackagesResourceConfig("aggregatorManager");

        System.out.println("Starting grizzly2...");
        return GrizzlyServerFactory.createHttpServer(BASE_URI, resourceConfig);
    }
    
    public static void main(String[] args) throws IOException {
        // Grizzly 2 initialization
        HttpServer httpServer = startServer();
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...",
                BASE_URI));
        
        
        Properties defaults = new Properties();					// Set default behavior
	defaults.setProperty("Verbose","true");
	defaults.setProperty("NmapJob Request Interval", "10");
	
	Properties config = new Properties(defaults);				// Configure app with default behavior
	if(args.length!=0){								
		try {											
			config.load(new FileInputStream(args[0]));		// Configure app with user-parameters (if any) 
			System.out.println("Property file loaded succesfully");
		} catch (IOException e) {
			System.err.println("Failed to load properties from given file - " + e.getMessage());
			e.printStackTrace();
		}
	}									// Parsing properties
											
	v = Boolean.parseBoolean( config.getProperty("Verbose") );
	nmapjob_request_interval = Integer.parseInt( config.getProperty("NmapJob Request Interval") );
        
        System.in.read();
        httpServer.stop();
        
    }    
}
