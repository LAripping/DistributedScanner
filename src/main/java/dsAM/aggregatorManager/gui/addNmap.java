package dsAM.aggregatorManager.gui;

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
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;

import dsAM.aggregatorManager.db.NmapJobDAO;



public class addNmap extends JFrame
        implements WindowListener
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel mypanel ;
    private JComboBox<?> Periodic ;
    private JTextField parameters;
    private JTextField period;
    private JButton confirmbtn ;
    private JButton resetbtn;
    private JOptionPane optionPane;
    private JDialog dialog ;
    
    public addNmap(String HashSa){
        initComponents( HashSa );
    }

    private void initComponents(String HashSa )
    {
        //set up Jframe properties//
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        setSize(screenWidth / 2, screenHeight / 2);
        setLocation(screenWidth / 4, screenHeight / 4);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);        
        setResizable(false);
        setTitle("Assign Nmap Job");
        addWindowListener(this);
        
        
        //initiate components//
        mypanel = new JPanel();
        mypanel.setLayout(null);
        parameters = new JTextField();
        period = new JTextField();
        confirmbtn = new JButton("Confirm");
        resetbtn = new JButton("Reset");
       
        String[] option = {"false" , "true"};
        
        Periodic = new JComboBox<Object>(option);
        Periodic.setBorder(new CompoundBorder(BorderFactory.createTitledBorder("Periodic"), Periodic.getBorder()));
        period.setBorder(new CompoundBorder(BorderFactory.createTitledBorder("Period"), period.getBorder()));
        period.setColumns(10);
        period.setEnabled(false);
        parameters.setBorder(new CompoundBorder(BorderFactory.createTitledBorder("Parameters"), parameters.getBorder()));
        parameters.setColumns(15);
        
       
        parameters.setBounds(50 , 100 , 250 , 50);
        Periodic.setBounds(50 , 180 , 100 ,50 );
        period.setBounds(50 , 260 , 250 , 50);
        parameters.setToolTipText("Enter the nmap parameters here");
        period.setToolTipText("Enter the period of the job here");
        
        confirmbtn.setBounds(450 , 120 , 150 , 50);
        confirmbtn.setFont(new Font("Verdana", Font.BOLD , 15)); 
        ImageIcon confirmIcon = new ImageIcon("./src/main/java/dsAM/aggregatorManager/gui/Images/acceptbtn.jpg");
        Image img = confirmIcon.getImage().getScaledInstance(42, 35, java.awt.Image.SCALE_SMOOTH);
        confirmIcon = new ImageIcon(img);
        confirmbtn.setIcon(confirmIcon);
        confirmbtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        confirmbtn.setForeground(Color.BLACK);
        confirmbtn.setActionCommand(HashSa);
        
        resetbtn.setBounds(450 , 200 , 150 , 50);
        resetbtn.setFont(new Font("Verdana", Font.BOLD , 15)); 
        ImageIcon resetIcon = new ImageIcon("./src/main/java/dsAM/aggregatorManager/gui/Images/cancel.png");
        Image img2 = resetIcon.getImage().getScaledInstance(35, 35, java.awt.Image.SCALE_SMOOTH);
        resetIcon = new ImageIcon(img2);
        resetbtn.setIcon(resetIcon);
        resetbtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        resetbtn.setForeground(Color.BLACK);
       
        //add components to the panel//
        mypanel.add(parameters);
        mypanel.add(Periodic);
        mypanel.add(period);
        mypanel.add(confirmbtn);
        mypanel.add(resetbtn);
        this.setContentPane(mypanel);
        
        //add action listeners
        confirmbtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event) {
               confirmbtnActionPerformed(event);
            }
        });
        
        resetbtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event) {
               resetbtnActionPerformed(event);
            }
        });
        
        
        Periodic.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event) {
               periodicbtnActionPerformed(event);
            }
        });
        
    }
    
    
    private void periodicbtnActionPerformed(ActionEvent event)
    {
        String choice =  Periodic.getSelectedItem().toString();
        if(choice.equals("true"))
        {
            period.setEnabled(true);
        }
        else
        {
            period.setEnabled(false);
        }
    }
    
    
    private void resetbtnActionPerformed(ActionEvent event)
    {
        period.setText("");
        parameters.setText("");
        Periodic.setSelectedItem("false");
                
    }
    
    private void confirmbtnActionPerformed(ActionEvent event)
    {       
		String hash=event.getActionCommand();
        int Period;
        String params = parameters.getText();
        String value1 =  Periodic.getSelectedItem().toString();
        String value2 =  period.getText();
        boolean periodic;
        
        if(value1.equals("true"))
        {
            periodic = true;
            Period = Integer.parseInt(value2);
        }
        else
        {
            periodic = false;
            Period =0;
        }
       
	   NmapJobDAO dao = new NmapJobDAO();
	   dao.addNmapJob(params, periodic, Period, hash);
	   this.dispose();
    }

    @Override
    public void windowClosing(WindowEvent e)
    {
        String message = "Are you sure to exit?";
        optionPane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE, JOptionPane.YES_NO_OPTION );
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
