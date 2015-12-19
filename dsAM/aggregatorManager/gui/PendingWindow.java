package dsAM.aggregatorManager.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author petrakos
 */
public class PendingWindow extends JFrame{
    
    JLabel message;
    JPanel mypanel;
    public PendingWindow()
    {
        initComponents();
    }

    private void initComponents() {
        mypanel = new JPanel();
        message = new JLabel("No software agents registered currently!\nPending registration request");
        
           
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        setSize(screenWidth / 2, screenHeight / 2);
        setLocation(screenWidth / 4, screenHeight / 4);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        FlowLayout flow = new FlowLayout();
        mypanel.setLayout(flow);
        setContentPane(mypanel);
        add(message);
        
    }
    
}