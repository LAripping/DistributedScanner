package dsAM.aggregatorManager.gui;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import static javax.swing.JFrame.EXIT_ON_CLOSE;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;

import dsAM.aggregatorManager.db.NmapJobDAO;

/**
 *
 * @author petrakos
 */
public class AddNmap extends JFrame{
    JPanel mypanel ;
    JLabel SaBox ;
    JComboBox Periodic ;
    JTextField parameters;
    JTextField period;
    JButton confirmbtn ;
    //String HashSa=null;
    
	/**
	 *
	 * @param HashSa
	 */
	public AddNmap(String HashSa){
    	//this.HashSa = HashSa;
        initComponents(HashSa);
    }

    private void initComponents(String HashSa)
    {
         
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        setSize(screenWidth / 2, screenHeight / 2);
        setLocation(screenWidth / 4, screenHeight / 4);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Assign Nmap Job");
        
        
        mypanel = new JPanel();
        parameters = new JTextField();
        period = new JTextField();
        confirmbtn = new JButton("Confirm");
        FlowLayout flow = new FlowLayout(FlowLayout.LEFT);
        mypanel.setLayout(flow);
        
        
       // String[] SAS = { "tolis" , "leo"};
        String[] option = {"true" , "false"};
        SaBox = new JLabel(HashSa);
        
        Periodic = new JComboBox(option);//editable
        
        period.setBorder(new CompoundBorder(BorderFactory.createTitledBorder("Period"), period.getBorder()));
        period.setColumns(10);
        parameters.setBorder(new CompoundBorder(BorderFactory.createTitledBorder("Parameters"), parameters.getBorder()));
        parameters.setColumns(15);
        SaBox.setBorder(new CompoundBorder(BorderFactory.createTitledBorder("SA"), SaBox.getBorder()));
        
        Periodic.setBorder(new CompoundBorder(BorderFactory.createTitledBorder("Periodic"), Periodic.getBorder()));
        
        
        mypanel.add(SaBox);
        mypanel.add(parameters);
        mypanel.add(Periodic);
        mypanel.add(period);
        mypanel.add(confirmbtn);
        this.setContentPane(mypanel);
        
        
        confirmbtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event) {
               confirmbtnActionPerformed(event);
            }
        });
        
    }
    
    private void confirmbtnActionPerformed(ActionEvent event)
    {
        //FIX check wrong input//
        String hash=SaBox.getText();
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
        
       // String id = storeNmap(selectedSA , params , periodic , value , currentTimestamp);
        
	   NmapJobDAO dao = new NmapJobDAO();
	   dao.addNmapJob(params, periodic, Period, hash);
	   dispose();
	   
	
        

/*        
        String nmap = id + ',' + params + "," 
                + (periodic ? 1 : 0) + ',' + (periodic ? value : 0);
        
 */       
    }
    
    
}
