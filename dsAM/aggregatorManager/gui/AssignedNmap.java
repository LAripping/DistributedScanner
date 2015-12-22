package dsAM.aggregatorManager.gui;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;

import static javax.swing.JFrame.EXIT_ON_CLOSE;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

import dsAM.aggregatorManager.db.NmapJobDAO;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/**
 *
 * @author petrakos
 */
public class AssignedNmap extends JFrame {
	
	JPanel statusPanel;
	String Hash;
	
	public AssignedNmap(String HashSa) {
		this.Hash = HashSa;
		initComponents();
	}	
	
	private void initComponents() {
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		int screenHeight = screenSize.height;
		int screenWidth = screenSize.width;
		setSize(screenWidth / 2, screenHeight / 2);
		setLocation(screenWidth / 4, screenHeight / 4);
		setResizable(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Assigned Nmap Results");
		//FlowLayout flow = new FlowLayout(FlowLayout.LEFT);
		//setLayout(flow);
	}
	
	public void CreateStatusPanel() {
		statusPanel = new JPanel();
		//statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.PAGE_AXIS));
		this.setContentPane(statusPanel);
		
	}
	
	public void addPanel(String job) {
		JPanel saPanel = new JPanel();
		FlowLayout flow = new FlowLayout(FlowLayout.LEFT);
		saPanel.setLayout(flow);
		JLabel label = new JLabel("NmapJob");
		String[] columnNames = {"NmapID",
			"Nmap Parameters", "periodic", "period"};
		
		String[] job_lines = job.split(",");
		Object[][] data
			= {
				{job_lines[0], job_lines[1], job_lines[2], job_lines[3]}
			};
		JPanel tablePanel = new JPanel(new BorderLayout());
		JTable table = new JTable(data, columnNames);
		tablePanel.add(table, BorderLayout.CENTER);
		tablePanel.add(table.getTableHeader(), BorderLayout.NORTH);
		table.setEnabled(false);
		
		JButton showresultsbtn = new JButton("show results");
		JButton deletebtn = new JButton("delete Nmap");
		deletebtn.setActionCommand(job_lines[0]);
		
		saPanel.add(label);
		saPanel.add(tablePanel);
		saPanel.add(showresultsbtn);
		saPanel.add(deletebtn);
		statusPanel.add(saPanel);
		statusPanel.add(Box.createRigidArea(new Dimension(500, 50)));
		
		showresultsbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				showresultsbtnActionPerformed(event);
			}
		});
		
		deletebtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				deletebtnActionPerformed(event);
			}
		});
		
	}
	
	private void showresultsbtnActionPerformed(ActionEvent event) {
		//new NmapResults().setVisible(true);
		
		//ResultDAO dao = new ;
		// dao.showResults( From,Until, hash)
	}
	
	private void deletebtnActionPerformed(ActionEvent event) {
		int id = Integer.parseInt(event.getActionCommand());
		
		NmapJobDAO dao = new NmapJobDAO();
		dao.stopCommand(id);
		dispose();
	}
	
}
