package dsAM.aggregatorManager.services;

import java.util.ArrayList;

import dsAM.aggregatorManager.Main;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import dsAM.aggregatorManager.db.NmapJobDAO;
import dsAM.aggregatorManager.gui.AMPanel;
import dsAM.aggregatorManager.gui.LogInForm;
import dsAM.aggregatorManager.gui.registrationRequest;


/**
 * REST Web Service
 *
 * @author root
 */

@Path("/softwareagent")
public class SoftwareAgentResource {
	static boolean open_window=false;
	static private AMPanel mf;
	static private ArrayList<JPanel> panelsArray;
	static private ArrayList<String> hashArray = null;
	static private ArrayList<Thread> threadHotel;
	
	public static boolean getOpen_window() {
		return open_window;
	}
	
	
	public static ArrayList<JPanel> getPanelsArray() {
		return panelsArray;
	}

	public static void setPanelsArray(ArrayList<JPanel> panelsArray) {
		SoftwareAgentResource.panelsArray = panelsArray;
	}

	public static ArrayList<String> getHashArray() {
		return hashArray;
	}

	public static void hashremove(int index){
		hashArray.remove(index);
	}
	
	public static void panelssremove(int index){
		panelsArray.remove(index);
	}
	
	public static void threadremove(int index){
		threadHotel.remove(index);
	}

	public static void setHashArray(ArrayList<String> hashArray) {
		SoftwareAgentResource.hashArray = hashArray;
	}

	public static void setOpen_window(boolean open_window) {
		SoftwareAgentResource.open_window = open_window;
	}

	public static JFrame getAM()
	{
		return mf;
	}
	
	public static ArrayList<JPanel> getPanelArray()
	{
		return panelsArray;
	}
	
	public static ArrayList<String> gethashArray()
	{
		return hashArray;
	}
	
	public static int gethashArrayIndex(String hash) {
		int index=hashArray.indexOf(hash);
		return index;
	}
	public static void interruptthread(int index){
		threadHotel.get(index).interrupt();
	}
	
	
	@PUT
	@Consumes(MediaType.TEXT_PLAIN)
	public Response registerAgent(String register_request) {
		String[] rr=register_request.split("\\|");			// register_request String to be passed to gui
		if(Main.v){
			System.out.println("Received Register Request From SA " +rr[5]);    //rr[5]=hash
		}
		registrationRequest req= new registrationRequest(register_request,Thread.currentThread());
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
				threadHotel = new ArrayList<Thread>();
				panelsArray = new ArrayList<JPanel>();
				hashArray = new ArrayList<String>();
				mf = new AMPanel();
				LogInForm.getPR().setVisible(false);
				mf.setVisible(true);
				mf.CreateFatherPanel();
				open_window = true;
			}
				panelsArray.add(mf.addPanel(register_request));
				hashArray.add(rr[5]);
				Thread timerthr = new Thread(new Timerthread(rr[5]));
				timerthr.start();
				threadHotel.add(timerthr);
			return Response.ok(MediaType.TEXT_PLAIN).build();
		}
		else{									// If registration is rejected 
			return Response.status(406).build();		// return a "406-Not Acceptable" status
		}
	}
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getSAs(){
		if (hashArray==null){
			return "There are no SAs registered";
		}
		else {
			if(hashArray.isEmpty()){
				return "There are no SAs registered";
			}
			else{
				return gethashArray().toString();
			}
		}
	}
	
	@DELETE
	@Path("/{hash}")
	@Consumes(MediaType.TEXT_PLAIN)
	public void Terminate(@PathParam("hash")String hash){
		NmapJobDAO dao = new NmapJobDAO();
		dao.exitCommand(hash);
	}
}
