package dsAM.aggregatorManager.gui;

import dsAM.aggregatorManager.db.UserDAO;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;


public class androidRegistrationRequest extends JFrame  implements WindowListener
{
    private JLabel  message;
    private JButton acceptbtn;
    private JButton declinebtn;
    private JPanel  panel;
    private JOptionPane optionPane;
    private JDialog dialog ;
    private String username;
    private String password;
    private Thread register;
    private JLabel username_title;
    private JTextField username_container;
    private JLabel password_title;
    private JPasswordField password_container;
    private JCheckBox hideShow;
    private boolean accepted;                                                   //used to query the user choice from outside this class
    private static final long serialVersionUID = 1L;
    
    public androidRegistrationRequest(String username , String password , Thread th)
    {
        this.register = th;
        this.username = username;
        this.password = password;
    }


    public void show_gui() {
        initComponents();
    }

        
    public boolean isAccepted() {
    	return accepted;
    }
    

    private void initComponents() 
    {
        //create a custom border
        Border raisedbevel = BorderFactory.createRaisedBevelBorder();
        Border loweredbevel = BorderFactory.createLoweredBevelBorder();
        Border blackline = BorderFactory.createLineBorder(Color.black);
        CompoundBorder myborder;
        myborder = BorderFactory.createCompoundBorder(raisedbevel, loweredbevel);
        myborder = BorderFactory.createCompoundBorder(blackline , myborder);
        
        
        //create new components//
        panel = new JPanel();
        panel.setBackground(Color.yellow);
        panel.setLayout(null);
        this.setContentPane(panel);
        acceptbtn = new JButton("Accept");
        declinebtn = new JButton("Decline");
        message = new JLabel("ATTENTION!  An Android client requires registration!");
        username_title = new JLabel("Client Username");
        username_container = new JTextField(this.username);
        username_container.setEditable(false);
        username_container.setBorder(myborder);
        username_container.setBackground(Color.WHITE);
        password_title = new JLabel("Client Password");
        password_container = new JPasswordField(this.password);
        password_container.setEditable(false);
        password_container.setBorder(myborder);
        password_container.setBackground(Color.WHITE);
        
        //Use a check box to hide or show the password
        hideShow = new JCheckBox("Show Password");
        hideShow.setSelected(false);
        hideShow.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    password_container.setEchoChar((char) 0);
                } else {
                     password_container.setEchoChar('*');
                }
            }
        });
        
        //set Jframe properties//
        setTitle("Android Client Registration Request");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        setSize(screenWidth / 2, screenHeight / 2);
        setLocation(screenWidth / 4, screenHeight / 4);
        setResizable(false);
        addWindowListener(this);
        
        //Set components positions
        message.setBounds(60, 30 , this.getWidth() , 30);
        acceptbtn.setBounds( (this.getWidth() - 200) , 110 , 140 , 50);
        declinebtn.setBounds( (this.getWidth() - 200) , 180 , 140 , 50);
        username_title.setBounds(60 , 80 , 160 ,30);
        username_container.setBounds(60 , 120 , 160 ,35);
        password_title.setBounds(60 , 190 , 160 ,30);
        password_container.setBounds(60 , 220 , 160 ,35);
        hideShow.setBounds(60 ,270 , 150 , 40 );
        
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
        panel.add(password_title);
        panel.add(username_title);
        panel.add(password_container);
        panel.add(username_container);
        panel.add(hideShow);
        
        
        //add button action listeners
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

    /**
     * 
     * @param event 
     *  Registers new user and notifies the thread to allow for progress
     */
    private void acceptbtnActionPerformed(java.awt.event.ActionEvent event)
    {
        UserDAO dao = new UserDAO();
        dao.addUser(this.username , this.password);
	this.accepted = true;
        synchronized (this.register) {
            this.register.notify();
	}
	String message3 = "Android client successfully registered!";
        JOptionPane.showMessageDialog(this , message3);
        setVisible(false);
    }
    
    /**
     * 
     * @param event 
     * Rejects android client registration request
     * according to the will of the manager
     */
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
        String message1 = "Are you sure to exit?\nThis action will cancel the registration!";
        optionPane = new JOptionPane(message1, JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION );
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
    
    /**
     * 
     * @param e Event triggering this action
     * 
     * defines what happens when the manager 
     * click the decline button
     */
    private void UserChoiceEvent(PropertyChangeEvent e) 
    {
        Object source = e.getSource();
        if ((source == optionPane)) 
        {
            Object value = optionPane.getValue();
            if ((value.toString()).equals(Integer.toString(JOptionPane.YES_OPTION) ) )
            {                                                                   //manager decided to decline registration
                this.accepted = false;                                          //set accepted according to user choice
                synchronized (this.register) {                                  
                    this.register.notify();
                }
                this.setVisible(false);
            }
            else if ((value.toString()).equals(Integer.toString(JOptionPane.NO_OPTION) ) )
            {                                                                   //the user will reconsider and choose again
                dialog.setVisible(false);
            }
        }
    }
    
    /**
     * 
     * @param e Event triggering this action
     * 
     * defines what happens when the manager tries to close
     * the registration window
     */
    private void UserChoiceEvent2(PropertyChangeEvent e) 
    {
        Object source = e.getSource();
        if ((source == optionPane)) 
        {
            Object value = optionPane.getValue();
            if ((value.toString()).equals(Integer.toString(JOptionPane.YES_OPTION) ) )
            {                                                                   //manager decided to decline registration
            	this.setVisible(false);                                         //set accepted according to user choice            	
            }
            else if ((value.toString()).equals(Integer.toString(JOptionPane.NO_OPTION) ) )
            {                                                                   //the user will reconsider and choose again
                dialog.setVisible(false);
            }
        }
    }
    
}
