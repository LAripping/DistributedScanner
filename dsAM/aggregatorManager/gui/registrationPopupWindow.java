package dsAM.aggregatorManager.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import dsAM.aggregatorManager.db.SoftwareAgentDAO;

/**
 *
 * @author root
 */
public class registrationPopupWindow extends JFrame {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	JLabel message;
	JTextArea SAinfo;
	JButton acceptbtn;
	JButton declinebtn;
	JPanel mypanel;
	String allinfo;
	String hash;
	Thread register;
	boolean accepted;

	/**
	 *
	 * @return
	 */
	public boolean isAccepted() {
		return accepted;
	}

	/**
	 *
	 * @param register_request
	 * @param th
	 */
	public registrationPopupWindow(String register_request, Thread th) {
		this.register = th;
		this.allinfo = register_request;
		//initComponents(register_request);
	}

	/**
	 *
	 */
	public void show_gui() {
		initComponents(this.allinfo);

	}

	private void initComponents(String allInfo) {
		String[] rr = allInfo.split("\\|");
		acceptbtn = new JButton("accept");
		declinebtn = new JButton("decline");
		message = new JLabel("ATTENTION!  A software agent requires registration");
		SAinfo = new JTextArea("ATTENTION!  A software agent requires registration");
		mypanel = new JPanel();

		setTitle("Registration");
		//setDefaultCloseOperation(EXIT_ON_CLOSE);

		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		int screenHeight = screenSize.height;
		int screenWidth = screenSize.width;
		setSize(screenWidth / 2, screenHeight / 2);
		setLocation(screenWidth / 4, screenHeight / 4);
		setResizable(false);

		SAinfo.setEditable(false);
		SAinfo.setRows(6);
		// FlowLayout flow = new FlowLayout(FlowLayout.LEFT);
		// mypanel.setLayout(flow);

		//message.setSize(1000, 300);
		/*String[] info = allInfo.split("|");
        String HostTerminalName = info[0];
        String IP = info[1];
        String MAC = info[2];
        String OS = info[3];
        String NmapVersion = info[4];
		 */
		//mypanel.setSize(screenWidth / 5, screenHeight / 5);
		//message.setSize(1000 , 500);
		//message.setBounds(100 , 10 , 300 , 10);
		//message.setLocation(200,0 );
		mypanel.add(message);
		mypanel.add(SAinfo);
		mypanel.add(acceptbtn);
		mypanel.add(declinebtn);
		setContentPane(mypanel);

		for (int i = 0; i < rr.length; i++) {
			SAinfo.setText(rr[i]);
		}
		this.hash = rr[5];
		acceptbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				acceptbtnActionPerformed(event);
			}
		});

		declinebtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				declinebtnActionPerformed(event);
			}
		});

	}

	private void acceptbtnActionPerformed(java.awt.event.ActionEvent event) {
		SoftwareAgentDAO dao = new SoftwareAgentDAO();
		dao.acceptSAregistration(this.hash);
		
		this.accepted = true;
		synchronized (this.register) {
			this.register.notify();
		}
		dispose();
	}

	private void declinebtnActionPerformed(java.awt.event.ActionEvent event) {
		this.accepted = false;
		synchronized (this.register) {
			this.register.notify();
		}
		dispose();
	}

}

//JUNK
//setDefaultLookAndFeelDecorated(true);
        //setLayout(null);
