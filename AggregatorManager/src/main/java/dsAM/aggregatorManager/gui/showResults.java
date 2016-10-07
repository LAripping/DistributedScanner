package dsAM.aggregatorManager.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
		

public class showResults extends JFrame implements WindowListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel Father;
    private JScrollPane scrollpane ;
    private JPanel statusPanel;
    private JOptionPane optionPane;
	private JDialog dialog;
    
    public showResults()
    {
        initComponents();
    }        

    private void initComponents() 
    {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        setSize(screenWidth / 2, screenHeight / 2);
        setLocation(screenWidth / 4, screenHeight / 4);
        setResizable(true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setTitle(" Nmap Jobs Results");
        addWindowListener(this);        
         //a panel to host other panels inside//
        Father = new JPanel();
        Father.setLayout(new BoxLayout(Father, BoxLayout.PAGE_AXIS));
        this.setContentPane(Father);
       
        
        statusPanel = new JPanel();
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.PAGE_AXIS));
        scrollpane = new JScrollPane(statusPanel);
        scrollpane.getVerticalScrollBar().setUnitIncrement(12);
        scrollpane.getHorizontalScrollBar().setUnitIncrement(12);
        Father.add(scrollpane);
    }
    
    public void addPanel(String result)
    {
        //make a new panel//
        JPanel saPanel = new JPanel();
        JTextArea resultsBox = new JTextArea();
        resultsBox.setLineWrap(true);
        resultsBox.setEditable(false);
        resultsBox.setFont(new Font("Comicsans", Font.BOLD, 16));
        resultsBox.setWrapStyleWord(true);
        resultsBox.setText(result);
        
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        
        JScrollPane textscrollpane = new JScrollPane(resultsBox);
        textscrollpane.getVerticalScrollBar().setUnitIncrement(12);
        textscrollpane.getHorizontalScrollBar().setUnitIncrement(12);
        textscrollpane.setPreferredSize( new Dimension(screenWidth -500 , screenHeight / 5) );
        resultsBox.setCaretPosition(0);
        
        saPanel.add(textscrollpane);
        statusPanel.add(saPanel);
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
            	
            }
            else if ((value.toString()).equals(Integer.toString(JOptionPane.NO_OPTION) ) )
            {
                dialog.setVisible(false);
            }
        }
    }

}