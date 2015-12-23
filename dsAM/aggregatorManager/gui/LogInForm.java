package dsAM.aggregatorManager.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import dsAM.aggregatorManager.db.UserDAO;

/**
 *
 * @author root
 */
public class LogInForm extends JFrame
{
    JPanel mypanel ;
    JLabel usernameLabel;
    JLabel passwordLabel;
    JPasswordField passwordField;
    JTextField usernameField;
    JButton loginbtn;
    Thread server;              
             
    /**
     * Creates new form LogInForm
	 * @param th
     */
    public LogInForm(Thread th) {
        initComponents();
        this.server=th;
    }

    
    
    private void initComponents()
    {
        
        mypanel = new JPanel();
        loginbtn = new JButton("log in");
        usernameLabel = new JLabel("username");
        passwordLabel = new JLabel("password");
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        
        
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        setSize(screenWidth / 2, screenHeight / 2);
        setLocation(screenWidth / 4, screenHeight / 4);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("login");
        
        //FlowLayout flow = new FlowLayout(FlowLayout);
        //mypanel.setLayout(flow);
        
        usernameField.setColumns(15);
        passwordField.setColumns(15);
        
        mypanel.add(usernameLabel);
        mypanel.add(passwordLabel);
        mypanel.add(usernameField);
        mypanel.add(passwordField);
        mypanel.add(loginbtn);
        
        setContentPane(mypanel);
        
        loginbtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event) {
                loginbtnActionPerformed(event);
            }
        });
        
    }
    
    
    private void loginbtnActionPerformed(java.awt.event.ActionEvent event) 
    {  
    	UserDAO user = new UserDAO(usernameField,passwordField);
    	if(user.CheckAdmin()){
    		dispose();
    		synchronized(this.server){
    			server.notifyAll();
    		}
            new PendingWindow().setVisible(true);
    	}
    	else{
    		System.out.println("Wrong Username or Password!!!");
    	}
    }
}                   