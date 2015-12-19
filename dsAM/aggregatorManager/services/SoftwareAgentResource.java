package dsAM.aggregatorManager.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import dsAM.aggregatorManager.gui.mainFrame;
import dsAM.aggregatorManager.gui.registrationPopupWindow;

import java.sql.*;
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
	 * PUT method for creating an instance of SoftwareAgentInsatnce
	 * @param all_info_hash representation for the resource
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	@PUT
	@Consumes(MediaType.TEXT_PLAIN)
	public Response registerAgent(String register_request) {
		String[] rr=register_request.split("\\|");	// register_request String to be passed to gui
		System.out.println("Received Register Request From Agent " +rr[5]);
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
			System.out.println("Received Register Accepted!");
			if(!open_window) {
				mf = new mainFrame();
				mf.setVisible(true);
				mf.CreateFatherPanel();
			}
				mf.addPanel(register_request);
			return Response.ok(MediaType.TEXT_PLAIN).build();
		}
		else{							// If registration is rejected 
			return Response.status(406).build();		// return a "406-Not Acceptable" status
		}
	}
}