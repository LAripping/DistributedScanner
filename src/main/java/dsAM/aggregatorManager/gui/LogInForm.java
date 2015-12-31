
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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import dsAM.aggregatorManager.db.UserDAO;


public class LogInForm extends JFrame
        implements WindowListener
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel mypanel ;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JTextField usernameField;
    private JButton loginbtn;
    private JButton cancelbtn;
    private JOptionPane optionPane;
    private JDialog dialog ;
	private Thread server;
	private static PendingRequests pr;

	
	public static PendingRequests getPR()
	{
		return pr;
	}

    /**
     * Creates new form LogInForm
     */
    public LogInForm(Thread th)
    {
        createAndShowGUI();
        this.server=th;
    }
    

private void createAndShowGUI()
{
    //setting JFrame properties//
    Toolkit tk = Toolkit.getDefaultToolkit();
    Dimension screenSize = tk.getScreenSize();
    int screenHeight = screenSize.height;
    int screenWidth = screenSize.width;
    this.setSize(screenWidth / 2, screenHeight / 2);
    setLocation(screenWidth / 4, screenHeight / 4);
    setResizable(false);
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    setTitle("Log In");
    addComponentsToPane();
    addWindowListener(this);
}


    private void addComponentsToPane()
    {
        //initiate components//
        mypanel = new JPanel();
        loginbtn = new JButton("Log in");
        usernameLabel = new JLabel("Username");
        passwordLabel = new JLabel("Password");
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        cancelbtn = new JButton("Cancel");

        //set stage panel//
        mypanel.setLayout(null);
        mypanel.setBackground(Color.CYAN);
        setContentPane(mypanel);

        //set up all other components//
        usernameLabel.setBounds(300, 50 , 80 , 10);
        usernameField.setBackground(Color.LIGHT_GRAY);
        usernameField.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        usernameField.setBounds(240 , 80 , 200 , 30);
        passwordLabel.setBounds(300 , 130 , 80 , 15);
        passwordField.setBounds(240 , 160 , 200 , 30);
        passwordField.setBackground(Color.LIGHT_GRAY);
        usernameField.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        usernameField.setToolTipText("Enter username");
        passwordField.setToolTipText("Enter password");
        
        //style the buttons
        loginbtn.setBounds(200 , 230 , 135 , 50);
        loginbtn.setFont(new Font("Verdana", Font.BOLD , 15));
        ImageIcon icon = new ImageIcon("./src/main/java/dsAM/aggregatorManager/gui/Images/lock.png");
        Image img = icon.getImage().getScaledInstance(35 , 35 , java.awt.Image.SCALE_SMOOTH);
        icon = new ImageIcon(img);
        loginbtn.setIcon(icon);
        loginbtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginbtn.setForeground(Color.BLACK);        
        
        cancelbtn.setBounds(360 , 230 , 135 , 50);
        cancelbtn.setFont(new Font("Verdana", Font.BOLD , 15)); 
        ImageIcon cancelIcon = new ImageIcon("./src/main/java/dsAM/aggregatorManager/gui/Images/cancel.png");
        Image img2 = cancelIcon.getImage().getScaledInstance(35, 35, java.awt.Image.SCALE_SMOOTH);
        cancelIcon = new ImageIcon(img2);
        cancelbtn.setIcon(cancelIcon);
        cancelbtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelbtn.setForeground(Color.BLACK);
        
        
        loginbtn.addMouseListener(new java.awt.event.MouseAdapter() 
        {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt)
			{
				loginbtn.setBackground(Color.green);
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) 
			{
				loginbtn.setBackground(UIManager.getColor("control"));
			}
		});
        
        cancelbtn.addMouseListener(new java.awt.event.MouseAdapter()
        {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) 
			{
				cancelbtn.setBackground(Color.red);
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt)
			{
				cancelbtn.setBackground(UIManager.getColor("control"));
			}
		});

		        //add action listeners//
        loginbtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event) {
                loginbtnActionPerformed(event);
            }
        });

        cancelbtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event) {
                cancelbtnActionPerformed(event);
            }
        });
        
        
        //add components to panel//
        mypanel.add(usernameLabel);
        mypanel.add(usernameField);
        mypanel.add(passwordLabel);
        mypanel.add(passwordField);
        mypanel.add(loginbtn);
        mypanel.add(cancelbtn);

    }


    private void loginbtnActionPerformed(java.awt.event.ActionEvent event)
    {
		UserDAO user = new UserDAO(usernameField,passwordField);
    	if(user.CheckAdmin())
    	{
    		this.dispose();
    		synchronized(this.server){
    			server.notifyAll();
    		}
            LogInForm.pr = new PendingRequests();
            pr.setVisible(true);
    	}
    	else
    	{
    		System.out.println("Wrong Username or Password!!!");
    	}
    }

    private void cancelbtnActionPerformed(ActionEvent event)
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
                System.exit(0);
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
