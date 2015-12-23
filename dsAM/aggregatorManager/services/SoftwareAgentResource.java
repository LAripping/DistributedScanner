package dsAM.aggregatorManager.services;

import dsAM.aggregatorManager.Main;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import dsAM.aggregatorManager.gui.mainFrame;
import dsAM.aggregatorManager.gui.registrationPopupWindow;

/**
 * REST Web Service
 *
 * @author root
 */
@Path("/softwareagent")
public class SoftwareAgentResource {
	static boolean open_window=false;
	mainFrame mf;
	
	
	/**
	 * PUT method for registering a SoftwareAgent 
	 * @param register_request The SA's registration request containing all of the required info
	 * @return The response that will be sent to the inquiring SA
	 */
	@PUT
	@Consumes(MediaType.TEXT_PLAIN)
	public Response registerAgent(String register_request) {
		String[] rr=register_request.split("\\|");			// register_request String to be passed to gui
		if(Main.v){
			System.out.println("Received Register Request From SA " +rr[5]);
		}
		registrationPopupWindow req= new registrationPopupWindow(register_request,Thread.currentThread());
		synchronized(Thread.currentThread()){
			try {
				req.show_gui();
				req.setVisible(true);
				Thread.currentThread().wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		if(req.isAccepted()) {
			if(Main.v){
				System.out.println("SA "+ rr[5] + " was succesfully registered");
			}
			if(!open_window) {
				mf = new mainFrame();
				mf.setVisible(true);
				mf.CreateFatherPanel();
			}
				mf.addPanel(register_request);
			return Response.ok(MediaType.TEXT_PLAIN).build();
		}
		else{									// If registration is rejected 
			return Response.status(406).build();		// return a "406-Not Acceptable" status
		}
	}
}