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

import java.util.ArrayList;



public class AMPanel extends JFrame implements WindowListener
{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel Father ;
    private JPanel statusPanel ;
    private JPanel footer;
    private JScrollPane scrollpane ;
    private String[] rr = null;
	private JOptionPane optionPane;
	private JDialog dialog ;
	private String HASH;
    static boolean open_window=false;
    private static showNmapJobs sr;
    static private ArrayList<JPanel> jobspanel;
	static private ArrayList<String> idArray;
	static private boolean flag = false;
	
    public static void setflag(boolean flag) {
		AMPanel.flag = flag;
	}


	public static boolean isflag() {
		return flag;
	}

	public AMPanel()
    {
        initComponents();
    }        
    
    
    public static void setOpen_window(boolean open_window) {
		AMPanel.open_window = open_window;
	}
    
    
    public static void setsr(boolean action){
    	AMPanel.sr.setVisible(action);
    }
    
    public static void jobspanelremove(int index){
    	jobspanel.remove(index);
	}
    
    public static void idArrayremove(int index){
    	idArray.remove(index);
    }
    
    public static JFrame getSR()
	{
		return sr;
	}
    
    public static ArrayList<JPanel> getjobsPanel()
	{
		return jobspanel;
	}
	
	public static ArrayList<String> getIdArray()
	{
		return idArray;
	}

    private void initComponents()
    {        
        //initiate Jframe properties//
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        //setSize(screenWidth/2 , (int) (screenHeight/1.5));
        setSize(screenWidth , screenHeight);
        setLocation(screenWidth / 4, screenHeight / 5);
        setResizable(true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setTitle("Aggregator Manager");      
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
        showallbtn.setToolTipText("Show all Nmap jobs");
        showallbtn.setPreferredSize(new Dimension(200 , 80));
        
        panel.setMinimumSize(new Dimension(Short.MAX_VALUE , 130));
        panel.setMaximumSize(new Dimension(Short.MAX_VALUE , 130));
        panel.add(showallbtn);
       
		showallbtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event) {
                showallbtnActionPerformed(event);
            }
        });
        
       
        return panel;
    }
   
    
    public JPanel addPanel(String allInfo)
    {
		rr = null;
		rr = allInfo.split("\\|");
		
        //make a new panel//
        JPanel saPanel = new JPanel();
        saPanel.setLayout(null);
        
        //set up table information and style//
        String[] columnNames = {"Software Agent Hash" , 
								"Host Terminal Name",
                                "Interface IP",
                                "Interface Mac address",
                                "Host Terminal OS version",
                                "Nmap version"
                               };
        
        Object[][] data = 
        {
				{rr[5], rr[0], rr[1], rr[2], rr[3], rr[4]}			//rr[5]=hash
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
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(60);
        table.getColumnModel().getColumn(3).setPreferredWidth(110);
        table.getColumnModel().getColumn(4).setPreferredWidth(150);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);
        
       
        table.setEnabled(false);
        
        //make buttons and style them//
        final JButton assignbtn = new JButton("assign job");
        final JButton assignedbtn = new JButton("active nmaps");
        final JButton terminatebtn = new JButton("terminate SA");
        
        
        tablePanel.setBounds(200 ,70 , 900, 45);
        assignbtn.setBounds(280 ,170 , 200 , 50 );
        assignbtn.setFont(new Font("Verdana", Font.BOLD , 15));
        assignbtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        assignbtn.setToolTipText("Assign new Nmap Job");
        ImageIcon icon1 = new ImageIcon("./src/main/java/dsAM/aggregatorManager/gui/Images/assign.jpg");
        Image img1 = icon1.getImage().getScaledInstance(43 , 35 , java.awt.Image.SCALE_SMOOTH);
        icon1 = new ImageIcon(img1);
        assignbtn.setIcon(icon1);
        assignbtn.setForeground(Color.BLACK);        
        
        
        assignedbtn.setBounds(580 ,170 , 200 , 50 );
        assignedbtn.setFont(new Font("Verdana", Font.BOLD , 15));
        assignedbtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        assignedbtn.setToolTipText("Show active Nmap Jobs");
        ImageIcon icon2 = new ImageIcon("./src/main/java/dsAM/aggregatorManager/gui/Images/showme.jpeg");
        Image img2 = icon2.getImage().getScaledInstance(35 , 35 , java.awt.Image.SCALE_SMOOTH);
        icon2 = new ImageIcon(img2);
        assignedbtn.setIcon(icon2);
        assignedbtn.setForeground(Color.BLACK);        
        
        terminatebtn.setBounds(880 ,170 , 200 , 50 );
        terminatebtn.setFont(new Font("Verdana", Font.BOLD , 15));
        terminatebtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        terminatebtn.setToolTipText("terminate the Software Agent");
        ImageIcon icon3 = new ImageIcon("./src/main/java/dsAM/aggregatorManager/gui/Images/terminate.png");
        Image img3 = icon3.getImage().getScaledInstance(35 , 35 , java.awt.Image.SCALE_SMOOTH);
        icon3 = new ImageIcon(img3);
        terminatebtn.setIcon(icon3);
        terminatebtn.setForeground(Color.BLACK);        
        
        assignbtn.setActionCommand(rr[5]);									//pass the hash
        assignedbtn.setActionCommand(rr[5]);
		terminatebtn.setActionCommand(rr[5]);
        
        
        //add components to staging area//
        saPanel.add(tablePanel);
        saPanel.add(assignbtn);
        saPanel.add(assignedbtn);
        saPanel.add(terminatebtn);
        saPanel.setPreferredSize(new Dimension(1300 , 220));
        
        statusPanel.add(saPanel);
        statusPanel.revalidate();
        
        
       
       //mouse hovering actions//
        assignbtn.addMouseListener(new java.awt.event.MouseAdapter() {
    @Override
    public void mouseEntered(java.awt.event.MouseEvent evt) {
        assignbtn.setBackground(Color.green);
    }

    @Override
    public void mouseExited(java.awt.event.MouseEvent evt) {
        assignbtn.setBackground(UIManager.getColor("control"));
    }
});
        
           assignedbtn.addMouseListener(new java.awt.event.MouseAdapter() {
    @Override
    public void mouseEntered(java.awt.event.MouseEvent evt) {
        assignedbtn.setBackground(Color.green);
    }

    @Override
    public void mouseExited(java.awt.event.MouseEvent evt) {
        assignedbtn.setBackground(UIManager.getColor("control"));
    }
});
           
              terminatebtn.addMouseListener(new java.awt.event.MouseAdapter() {
    @Override
    public void mouseEntered(java.awt.event.MouseEvent evt) {
        terminatebtn.setBackground(new Color(2 ,5,4));
    }

    @Override
    public void mouseExited(java.awt.event.MouseEvent evt) {
        terminatebtn.setBackground(UIManager.getColor("control"));
    }
});
        
        //button action listeners//
        
         assignbtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event) {
                assignbtnActionPerformed(event);
            }
        });
        
        
        assignedbtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event) {
                resultsbtnActionPerformed(event);
            }
        });
        
        terminatebtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event) {
                terminatebtnActionPerformed(event);
            }
        });
        
        
       return saPanel;
        
    }
    
    //assigned nmap button
	private void assignbtnActionPerformed(ActionEvent event) 
	{
		HASH = event.getActionCommand();
		addNmap addnew = new addNmap(HASH );		//hash and terminal name
		addnew.setLocationRelativeTo(this);
		addnew.setVisible(true); 
	}
     
	//show active nmaps button
    private void resultsbtnActionPerformed(ActionEvent event)
    {
		HASH = event.getActionCommand();	
		NmapJobDAO dao = new NmapJobDAO();
		ArrayList<String> assigned = dao.getSAsNmpaJobs(HASH , true);

		if(assigned==null){
			
			String message3 = "There are no active nmap_jobs for this SoftwareAgent";
	        JOptionPane.showInternalMessageDialog(null, message3,"Confirmation",JOptionPane.INFORMATION_MESSAGE);
		}
		else
		{
			for (String job : assigned) {
				if (!open_window) 
				{
					jobspanel = new ArrayList<JPanel>();
					idArray = new ArrayList<String>();
					sr = new showNmapJobs(HASH);		//pass the SA hash and terminal name//
					sr.setVisible(true);
					open_window = true ;
				}
				String[] id=job.split(",");
				if(!id[1].equals("Stop")) {
					jobspanel.add(sr.addPanel(job));
					idArray.add(id[0]);
				}
			}
		}
    }
    
    //terminate button
    private void terminatebtnActionPerformed(ActionEvent event)
    {
		HASH = event.getActionCommand();
		String message = "Are you sure you want to terminate the Software Agent?";
        optionPane = new JOptionPane(message, JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION );
        optionPane.addPropertyChangeListener( new PropertyChangeListener()
        {
            @Override
            public void propertyChange(PropertyChangeEvent e)
            {
                Object source = e.getSource();
				if ((source == optionPane)) 
				{
					Object value = optionPane.getValue();
					if ((value.toString()).equals(Integer.toString(JOptionPane.YES_OPTION) ) )
					{
						NmapJobDAO dao = new NmapJobDAO();
						dao.exitCommand(HASH);
						dialog.setVisible(false);
						flag=true;
						
					}
					else if ((value.toString()).equals(Integer.toString(JOptionPane.NO_OPTION) ) )
					{
						dialog.setVisible(false);
					}
				}
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
	
    private void showallbtnActionPerformed(ActionEvent event)
	{
    	String indicator = "0";
    	new timeFilter(indicator).setVisible(true);
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
    
    @Override
    public void windowClosing(WindowEvent e)
    {
        String message = "Are you sure to exit?\nThis action will terminate the AM!";
        optionPane = new JOptionPane(message, JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION );
        optionPane.addPropertyChangeListener( new PropertyChangeListener()
        {
            @Override
            public void propertyChange(PropertyChangeEvent e)
            {
                UserChoiceEvent2(e);
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


    //what happens when the user closes the window
    private void UserChoiceEvent2(PropertyChangeEvent e) 
    {
        Object source = e.getSource();
        if ((source == optionPane)) 
        {
            Object value = optionPane.getValue();
            if ((value.toString()).equals(Integer.toString(JOptionPane.YES_OPTION) ) )
            {
            	this.setVisible(false);
            	System.exit(0);
            	
            }
            else if ((value.toString()).equals(Integer.toString(JOptionPane.NO_OPTION) ) )
            {
                dialog.setVisible(false);
            }
        }
    }
	
}
