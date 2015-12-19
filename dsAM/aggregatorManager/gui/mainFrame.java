package dsAM.aggregatorManager.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;

import static javax.swing.JFrame.EXIT_ON_CLOSE;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

import dsAM.aggregatorManager.db.MyConnection;
import dsAM.aggregatorManager.db.NmapJobDAO;
import java.util.ArrayList;


/**
 *
 * @author petrakos
 */
public class mainFrame extends JFrame {

	JPanel Father;
	JPanel statusPanel;
	JPanel footer;
	String[] rr = null;
	AssignedNmap sr;
	static boolean open_window = false;

	public mainFrame() {
		initComponents();
	}

	JTextArea text;

	private void initComponents() {
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		int screenHeight = screenSize.height;
		int screenWidth = screenSize.width;
		setSize(screenWidth / 2, screenHeight / 2);
		setLocation(screenWidth / 4, screenHeight / 4);
		setResizable(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Aggregator Manager");
		FlowLayout flow = new FlowLayout(FlowLayout.LEFT);
		setLayout(flow);

	}

	public void CreateFatherPanel() {
		Father = new JPanel();
		//Father.setLayout(new BoxLayout(Father, BoxLayout.PAGE_AXIS));
		Father.add(this.CreateStatusPanel());
		Father.add(Box.createRigidArea(new Dimension(50, 50)));
		Father.add(this.CreateFooter());
		this.setContentPane(Father);
	}

	public JPanel CreateStatusPanel() {
		statusPanel = new JPanel();
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.PAGE_AXIS));
		return statusPanel;
	}

	public JPanel CreateFooter() {
		footer = new JPanel();
		footer.setLayout(new BoxLayout(footer, BoxLayout.PAGE_AXIS));
		JButton showallbtn = new JButton("show all");
		footer.add(showallbtn);
		return footer;
	}

	public void addPanel(String s) {
		rr = null;
		rr = s.split("\\|");
		JPanel saPanel = new JPanel();
		FlowLayout flow = new FlowLayout(FlowLayout.LEFT);
		saPanel.setLayout(flow);
		// JLabel label = new JLabel(rr[5]);
		String[] columnNames = {"Software Agent Hash",
			"Host Terminal Name",
			"Interface IP",
			"Interface Mac address",
			"Host Terminal OS version",
			"Nmap version"};

		Object[][] data
			= {
				{rr[5], rr[0], rr[1], rr[2], rr[3], rr[4]}
			};
		JPanel tablePanel = new JPanel(new BorderLayout());
		//JScrollPane scrollPane = new JScrollPane();
		JTable table = new JTable(data, columnNames);
		tablePanel.add(table, BorderLayout.CENTER);
		tablePanel.add(table.getTableHeader(), BorderLayout.NORTH);
		//scrollPane.setViewportView(table);
		table.setEnabled(false);
		//add(new JScrollPane(table));

		JButton assignbtn = new JButton("assign job");
		JButton assignedbtn = new JButton("show assigned");
		JButton terminatebtn = new JButton("terminate SA");

		assignbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				assignbtnActionPerformed(event);
			}
		});

		assignedbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				resultsbtnActionPerformed(event);
			}
		});

		terminatebtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				terminatebtnActionPerformed(event);
			}
		});

		//saPanel.add(label);
		saPanel.add(tablePanel);
		saPanel.add(assignbtn);
		saPanel.add(assignedbtn);
		saPanel.add(terminatebtn);
		statusPanel.add(saPanel);
		statusPanel.add(Box.createRigidArea(new Dimension(50, 50)));

	}

	private void assignbtnActionPerformed(ActionEvent event) {
		new AddNmap(rr[5]).setVisible(true);

	}

	private void resultsbtnActionPerformed(ActionEvent event) {
		NmapJobDAO dao = new NmapJobDAO();
		ArrayList<String> assigned = dao.getSAsNmpaJobs(rr[5], true);
		
		for (String job : assigned) {
			if (!open_window) {
				sr = new AssignedNmap(rr[5]);
				sr.CreateStatusPanel();
				sr.setVisible(true);
			}
			sr.addPanel(job);
		}	
	}
	
				
				

	private void terminatebtnActionPerformed(ActionEvent event) {
		NmapJobDAO dao = new NmapJobDAO();
		dao.exitCommand(rr[5]);
		dispose();
	}
	
	
	/*
//Create the menu bar.
        menuBar = new JMenuBar();
        text = new JTextArea();
            //Build the first menu.
        menu = new JMenu("A Menu");
        menu.setMnemonic(KeyEvent.VK_A);
        menu.getAccessibleContext().setAccessibleDescription(
              "The only menu in this program that has menu items");
        menuBar.add(menu);
       
        menuItem = new JMenuItem("options");
        menuItem.setMnemonic(KeyEvent.VK_A);
        setJMenuBar(menuBar);
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 834, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 446, Short.MAX_VALUE)
        );
    }
     
          
     
      public void CreateStatusPanel()
    {
        Father = new JPanel();
        statusPanel = new JPanel();
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.PAGE_AXIS));
        //this.setContentPane(statusPanel);
        this.setContentPane(Father);
        statusPanel.add(Father);
        footer = new JPanel();
        JButton showallbtn = new JButton("show all");
        footer.add(showallbtn);
        Father.add(footer);
        
   
    }
	 */

}
