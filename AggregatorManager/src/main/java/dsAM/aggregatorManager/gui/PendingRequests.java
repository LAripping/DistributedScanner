package dsAM.aggregatorManager.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;



public class PendingRequests extends JFrame
{
   
   
	private static final long serialVersionUID = 1L;
	private JLabel message;
    private JPanel mypanel;
    private JOptionPane optionPane;
    private JDialog dialog ;


    public PendingRequests()
    {
        initComponents();
    }

    private void initComponents() 
    {
        mypanel = new JPanel();
        this.addWindowListener(new WindowAdapter() 
        {
            @Override
            public void windowClosing(WindowEvent windowEvent)
            {
               windowClosingActionPerformed(windowEvent);
            } 
        });
        
        String HTML = "<html><p>No software agents currently registered!<br>Waiting for registration requests....<p></html>";
        message = new JLabel(HTML);  
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        setSize(screenWidth / 2, screenHeight / 2);
        
        setLocation(screenWidth / 4, screenHeight / 4);
        setResizable(false);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        mypanel.setLayout(null);
        mypanel.setBackground(Color.white);
        setContentPane(mypanel);
        
        message.setForeground(Color.RED);
        message.setFont(new Font("Verdana", Font.BOLD , 15));
        message.setBounds(120, 50, 400, 90);
        
        
        ImageIcon icon = new ImageIcon("./src/main/java/dsAM/aggregatorManager/gui/Images/waiting.gif");
        Image img = icon.getImage().getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH);
        icon = new ImageIcon(img);
        JLabel thumb = new JLabel();
        thumb.setIcon(icon);
        thumb.setBounds(80, 30, 600, 400);
        mypanel.add(thumb);
        mypanel.add(message);   
    }
    
    private void windowClosingActionPerformed(WindowEvent windowEvent) 
    {
        String message = "Are you sure to exit?\nThis action will terminate the AM!";
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
                System.exit(0);
            }
            else if ((value.toString()).equals(Integer.toString(JOptionPane.NO_OPTION) ) )
            {
                dialog.setVisible(false);
            }
        }
    }
    
}
