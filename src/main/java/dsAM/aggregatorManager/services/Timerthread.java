package dsAM.aggregatorManager.services;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import dsAM.aggregatorManager.Main;
import dsAM.aggregatorManager.db.NmapJobDAO;
import dsAM.aggregatorManager.gui.AMPanel;
import dsAM.aggregatorManager.gui.PendingRequests;

public class Timerthread implements Runnable {
	private String hash;
	static private ArrayList<JPanel> panelArray;
	private JFrame AMobj;
	
	public Timerthread(String hash){
		this.hash=hash;
	}
	
	public void run() {				
		while(true){
			try {									
				Thread.sleep( Main.nmapjob_request_interval * 3000 );
				System.out.println("Software Agent with hash "+hash+" is offline");
				break;
			} catch (InterruptedException ex) {
				if(AMPanel.isflag())
					break;
				else 
					continue;
			}
		}
		
		NmapJobDAO dao = new NmapJobDAO();
		dao.setoffline(hash);
		int index= SoftwareAgentResource.gethashArrayIndex(hash);
		if(index!=-1) {
			panelArray = SoftwareAgentResource.getPanelArray();
			panelArray.get(index).setVisible(false);
			SoftwareAgentResource.hashremove(index);
			SoftwareAgentResource.panelssremove(index);
			SoftwareAgentResource.threadremove(index);
			if(panelArray.isEmpty()){
				AMobj = SoftwareAgentResource.getAM();
				AMobj.setVisible(false);
				SoftwareAgentResource.setOpen_window(false);
				PendingRequests pr = new PendingRequests();
				pr.setVisible(true);
				
			}
		}
		AMPanel.setflag(false);
		try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
