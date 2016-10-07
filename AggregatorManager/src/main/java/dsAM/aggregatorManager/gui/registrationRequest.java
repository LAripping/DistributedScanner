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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

import dsAM.aggregatorManager.db.SoftwareAgentDAO;


public class registrationRequest extends JFrame implements WindowListener
{    
    private JLabel  message;
    private JTable  infoTable;
    private JButton acceptbtn;
    private JButton declinebtn;
    private JPanel  panel;
    private JOptionPane optionPane;
    private JDialog dialog ;
    private String allinfo;
	private String hash;
	private Thread register;
	private boolean accepted;
    private static final long serialVersionUID = 1L;
    
    public registrationRequest(String register_request, Thread th) {
		this.register = th;
		this.allinfo = register_request;
	}

	
	public void show_gui() 
	{
		initComponents(this.allinfo);
	}

    public boolean isAccepted() {
    	return accepted;
    }
    

    private void initComponents(String allInfo) 
    {
		String[] rr = allInfo.split("\\|");
		this.hash = rr[5];
        //create new components//
        panel = new JPanel();
        panel.setLayout(null);
        this.setContentPane(panel);
        acceptbtn = new JButton("Accept");
        declinebtn = new JButton("Decline");
        message = new JLabel("ATTENTION!  A software agent requires registration.");
        
        
        //set Jframe properties//
        setTitle("Software Agent Registration Request");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        setSize(screenWidth / 2, screenHeight / 2);
        setLocation(screenWidth / 4, screenHeight / 4);
        setResizable(false);
        addWindowListener(this);
        
        
        //fix a border fot the table//
        Border raisedbevel = BorderFactory.createRaisedBevelBorder();
        Border loweredbevel = BorderFactory.createLoweredBevelBorder();
        Border blackline = BorderFactory.createLineBorder(Color.black);
        CompoundBorder myborder;
        myborder = BorderFactory.createCompoundBorder(raisedbevel, loweredbevel);
        myborder = BorderFactory.createCompoundBorder(blackline , myborder);
        
        //build table info//
        String[] columnNames = {"Attributes" , "Data"};
        Object[][] data = 
        {
            { "Software Agent Hash" , rr[5] } ,
            {"Host Terminal Name" , rr[0] },
            {"Interface IP" , rr[1] },
            {"Interface Mac address" ,rr[2] },
            {"Host Terminal OS version" , rr[3] },
            {"Nmap version" , rr[4] }
        };
        
        //customize table appearance//
        infoTable = new JTable(data, columnNames);
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(infoTable, BorderLayout.CENTER);
        tablePanel.add(infoTable.getTableHeader(), BorderLayout.NORTH);
        tablePanel.setBorder(myborder);
        infoTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        infoTable.getColumnModel().getColumn(0).setPreferredWidth(140);
        infoTable.getColumnModel().getColumn(1).setPreferredWidth(140);
        infoTable.setEnabled(false);
       
        
        message.setBounds(60, 30 , this.getWidth() , 30);
        tablePanel.setBounds(60 , 120 , 340 , 125);
        acceptbtn.setBounds( (this.getWidth() - 200) , 110 , 140 , 50);
        declinebtn.setBounds( (this.getWidth() - 200) , 180 , 140 , 50);
        
        
        //decorate buttons
        acceptbtn.setFont(new Font("Verdana", Font.BOLD , 15));
        ImageIcon icon = new ImageIcon("./src/main/java/dsAM/aggregatorManager/gui/Images/acceptbtn.jpg");
        Image img = icon.getImage().getScaledInstance(35 , 30 , java.awt.Image.SCALE_SMOOTH);
        icon = new ImageIcon(img);
        acceptbtn.setIcon(icon);
        acceptbtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        acceptbtn.setForeground(Color.BLACK);        
        
        declinebtn.setFont(new Font("Verdana", Font.BOLD , 15)); 
        ImageIcon cancelIcon = new ImageIcon("./src/main/java/dsAM/aggregatorManager/gui/Images/declinebtn.jpg");
        Image img2 = cancelIcon.getImage().getScaledInstance(35, 30, java.awt.Image.SCALE_SMOOTH);
        cancelIcon = new ImageIcon(img2);
        declinebtn.setIcon(cancelIcon);
        declinebtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        declinebtn.setForeground(Color.BLACK);

		//add components to the panel//
        panel.add(acceptbtn);    
        panel.add(declinebtn);    
        panel.add(message);    
        panel.add(tablePanel);    
       
        acceptbtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event) {
                acceptbtnActionPerformed(event);
            }
        });
        
        
        declinebtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event) {
                declinebtnActionPerformed(event);
            }
        });
        
    }


    private void acceptbtnActionPerformed(java.awt.event.ActionEvent event)
    {
        SoftwareAgentDAO dao = new SoftwareAgentDAO();
		dao.acceptSAregistration(this.hash);
		
		this.accepted = true;
		synchronized (this.register) {
			this.register.notify();
		}
		String message3 = "Software Agent successfully registered!";
        optionPane = new JOptionPane(message3, JOptionPane.INFORMATION_MESSAGE, JOptionPane.OK_OPTION );
        setVisible(false);
    }
    
    private void declinebtnActionPerformed(java.awt.event.ActionEvent event)
    {	
        String message2 = "Are you sure you want to decline registration !";
        optionPane = new JOptionPane(message2, JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION );
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
    
    
    private void UserChoiceEvent(PropertyChangeEvent e) 
    {
        Object source = e.getSource();
        if ((source == optionPane)) 
        {
            Object value = optionPane.getValue();
            if ((value.toString()).equals(Integer.toString(JOptionPane.YES_OPTION) ) )
            {
                this.accepted = false;
        		synchronized (this.register) {
        			this.register.notify();
        		}
                this.setVisible(false);
            }
            else if ((value.toString()).equals(Integer.toString(JOptionPane.NO_OPTION) ) )
            {
                dialog.setVisible(false);
            }
        }
    }
    
    
    private void UserChoiceEvent2(PropertyChangeEvent e) 
    {
        Object source = e.getSource();
        if ((source == optionPane)) 
        {
            Object value = optionPane.getValue();
            if ((value.toString()).equals(Integer.toString(JOptionPane.YES_OPTION) ) )
            {
            	this.setVisible(false);
            	//System.exit(0);
            	
            }
            else if ((value.toString()).equals(Integer.toString(JOptionPane.NO_OPTION) ) )
            {
                dialog.setVisible(false);
            }
        }
    }
}
