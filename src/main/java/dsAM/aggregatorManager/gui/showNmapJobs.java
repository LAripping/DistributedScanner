package dsAM.aggregatorManager.gui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

import dsAM.aggregatorManager.db.NmapJobDAO;


public class showNmapJobs extends JFrame
        implements WindowListener
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel Father ;
    private JPanel statusPanel ;
    private JPanel footer;
    private JScrollPane scrollpane ;
    private JOptionPane optionPane;
    private JDialog dialog ;
    private  String hash ;
    private ArrayList<JPanel> jobspanel;
   	private ArrayList<String> idArray;
   	private JFrame SRobj; 
   	
    public showNmapJobs(String HASH)
    {
    	this.hash = HASH;
         createAndShowGUI();
    }

	private void createAndShowGUI()
	{
		//initiate JFrame size//
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		int screenHeight = screenSize.height;
		int screenWidth = screenSize.width;
		setSize(screenWidth/2 , (int) (screenHeight/1.5));
		setLocation(screenWidth / 4, screenHeight / 5);
		setResizable(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setTitle("Active Nmap Jobs");   
		CreateFatherPanel();
		addWindowListener(this);
	}
    
    public void CreateFatherPanel()
    {
        //a panel to host other panels inside//
        Father = new JPanel();
        Father.setLayout(new BoxLayout(Father, BoxLayout.PAGE_AXIS));
        this.setContentPane(Father);
        
         //make 2 new panels//
        statusPanel = CreateStatusPanel();
        footer = CreateFooter();

        scrollpane = new JScrollPane(statusPanel);
        scrollpane.getVerticalScrollBar().setUnitIncrement(12);
        scrollpane.getHorizontalScrollBar().setUnitIncrement(12);
        
        //add components to their right place//
        Father.add(scrollpane);
        Father.add(footer);
        
    }
     
    public JPanel CreateStatusPanel()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        return panel;
    }
    
    
    public JPanel CreateFooter()
    {
        //initiate components//
        JPanel panel = new JPanel();
        JButton showallbtn = new JButton("Show all");
        
        //style components//
        showallbtn.setFont(new Font("Verdana", Font.BOLD , 20));
        showallbtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        showallbtn.setToolTipText("Show all Nmap jobs results");
        showallbtn.setPreferredSize(new Dimension(200 , 80));
        
        
        //button action listeners//
        showallbtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event) {
                showallbtnActionPerformed(event);
            }
        });        
        
        panel.setMinimumSize(new Dimension(Short.MAX_VALUE , 130));
        panel.setMaximumSize(new Dimension(Short.MAX_VALUE , 130));
        panel.add(showallbtn);
       
        return panel;
    }
    
    
    public JPanel addPanel(String job)
    {
        String[] job_lines = job.split(",");        
        //make a new panel//
        JPanel saPanel = new JPanel();
        saPanel.setLayout(null);
        final JButton deletebtn = new JButton("Delete job");
       
        //set up the table//
        String[] columnNames = {"Nmap id", "Parameters", "Periodic", "Period"};
        
        Object[][] data = 
        {
            { job_lines[0], job_lines[1], job_lines[2], job_lines[3]}
        };
        
        Border raisedbevel = BorderFactory.createRaisedBevelBorder();
        Border loweredbevel = BorderFactory.createLoweredBevelBorder();
        Border blackline = BorderFactory.createLineBorder(Color.black);
        CompoundBorder myborder;
        
        myborder = BorderFactory.createCompoundBorder(raisedbevel, loweredbevel);
        myborder = BorderFactory.createCompoundBorder(blackline , myborder);

        JPanel tablePanel = new JPanel(new BorderLayout());
        JTable table = new JTable(data, columnNames);
        
        tablePanel.add(table, BorderLayout.CENTER);
        tablePanel.add(table.getTableHeader(), BorderLayout.NORTH);
        tablePanel.setBorder(myborder);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(50);
        table.getColumnModel().getColumn(3).setPreferredWidth(50);
        table.setEnabled(false);
        
        tablePanel.setBounds(400 ,70 , 600, 45);
        deletebtn.setBounds(600 ,160 , 200 , 50 );
        deletebtn.setFont(new Font("Verdana", Font.BOLD , 15));
        deletebtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deletebtn.setToolTipText("Terminate Nmap Job");
        ImageIcon icon1 = new ImageIcon("./src/main/java/dsAM/aggregatorManager/gui/Images/deletebtn.png");
        Image img1 = icon1.getImage().getScaledInstance(43 , 35 , java.awt.Image.SCALE_SMOOTH);
        icon1 = new ImageIcon(img1);
        deletebtn.setIcon(icon1);
        deletebtn.setForeground(Color.BLACK);
        if(job_lines[2].equals("false"))
        {
            deletebtn.setEnabled(false);
        }
        deletebtn.setActionCommand(job_lines[0]);
        
        
        saPanel.add(tablePanel);
        saPanel.add(deletebtn);
        saPanel.setPreferredSize(new Dimension(1400 , 220));
        
        statusPanel.add(saPanel);
       
       //mouse hovering actions//
        deletebtn.addMouseListener(new java.awt.event.MouseAdapter() {
    @Override
    public void mouseEntered(java.awt.event.MouseEvent evt) {
        deletebtn.setBackground(Color.red);
    }

    @Override
    public void mouseExited(java.awt.event.MouseEvent evt) {
        deletebtn.setBackground(UIManager.getColor("control"));
    }
});
        
        //button action listeners//
        deletebtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event) {
                deletebtnActionPerformed(event);
            }
        });        
        
        return saPanel;
    }
     
    public void deletebtnActionPerformed(ActionEvent event)
    {
    	String nmapId = event.getActionCommand();
        int id = Integer.parseInt(event.getActionCommand());
		NmapJobDAO dao = new NmapJobDAO();
		dao.stopCommand(id);
		
		jobspanel = AMPanel.getjobsPanel();
		idArray = AMPanel.getIdArray();
		int index= idArray.indexOf(nmapId);
		jobspanel.get(index).setVisible(false);
		AMPanel.jobspanelremove(index);
		AMPanel.idArrayremove(index);
		if(jobspanel.isEmpty()){
			
			SRobj = AMPanel.getSR();
			SRobj.setVisible(false);
			AMPanel.setOpen_window(false);
		}
				
    }
    
    private void showallbtnActionPerformed(ActionEvent event) 
    {
    	new timeFilter(this.hash).setVisible(true);
    }
    
    @Override
    public void windowClosing(WindowEvent e)
    {
        String message = "Are you sure to exit?";
        optionPane = new JOptionPane(message, JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION );
        optionPane.addPropertyChangeListener( new PropertyChangeListener()
        {
            @Override
            public void propertyChange(PropertyChangeEvent e)
            {
                UserChoiceEvent(e);
            }
        });
        dialog = new JDialog(this, "Choose wisely!" , true);
        dialog.setContentPane(optionPane);
        dialog.setDefaultCloseOperation( JDialog.DO_NOTHING_ON_CLOSE );
        dialog.pack();
        dialog.setAutoRequestFocus(true);
        dialog.setAlwaysOnTop(true);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    
    private void UserChoiceEvent(PropertyChangeEvent e) 
    {
        Object source = e.getSource();
        if ((source == optionPane)) 
        {
            Object value = optionPane.getValue();
            if ((value.toString()).equals(Integer.toString(JOptionPane.YES_OPTION) ) )
            {
                this.setVisible(false);
                AMPanel.setOpen_window(false);
            }
            else if ((value.toString()).equals(Integer.toString(JOptionPane.NO_OPTION) ) )
            {
                dialog.setVisible(false);
            }
        }
    }
    
    
    
    @Override
    public void windowOpened(WindowEvent e) {}

    @Override
    public void windowClosed(WindowEvent e) {}

    @Override
    public void windowIconified(WindowEvent e) {}

    @Override
    public void windowDeiconified(WindowEvent e) {}

    @Override
    public void windowActivated(WindowEvent e) {}

    @Override
    public void windowDeactivated(WindowEvent e) {}
    
}
