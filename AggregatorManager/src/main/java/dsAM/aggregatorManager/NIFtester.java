package dsAM.aggregatorManager;


import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.channels.SocketChannel;
import java.util.Collections;
import java.util.Enumeration;

/**
 *
 * @author tsaou
 */
public class NIFtester {

	private InetAddress ip;


	public NIFtester() {
		ip = null;
	}
	
	/**
	 *
	 * @return Getter for the working InternetAddress found 
	 */
	public InetAddress getIp() {
		return ip;
	}
	
	/**
	 *
	 * @return An internet-connected Network Interface. The corresponding Internet Address used is also set
	 * @throws SocketException
	 */
	public NetworkInterface getInternetNIF() throws SocketException {
			// iterate over the network interfaces found
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			
			for (NetworkInterface interface_ : Collections.list(interfaces)) {
				// we shouldn't care about loopback addresses
				if (interface_.isLoopback()) {
					continue;
				}
				
				// if you don't expect the interface to be up you can skip this
				// though it would question the usability of the rest of the code
				if (!interface_.isUp()) {
					continue;
				}
				
				// iterate over the addresses associated with the interface
				Enumeration<InetAddress> addresses = interface_.getInetAddresses();
				for (InetAddress address : Collections.list(addresses)) {
					try {
						// look only for ipv4 addresses
						if (address instanceof Inet6Address) {
							continue;
						}
						
						try {
							// use a timeout big enough for your needs
							if (!address.isReachable(3000)) {
								continue;
							}
						} catch (IOException ex) {
							ex.printStackTrace();
						}
						
						//
						SocketChannel socket = null;
						try {
							socket = SocketChannel.open();
							
							//use a big enough timeout
							socket.socket().setSoTimeout(3000);
							
							// bind the socket to your local interface
							socket.bind(new InetSocketAddress(address, 8080));
							
							// try to connect to *somewhere*
							socket.connect(new InetSocketAddress("www.di.uoa.gr", 80));
						} catch (IOException ex) {
							ex.printStackTrace();
						}
						
						// stops at the first *working* solution
						ip = address;
						socket.close();
						return interface_;
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}

			if(Main.v){
				System.out.println("Error! No internet-connected network interface found!");
			}
			return null;
	}

}