
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
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;

import dsAM.aggregatorManager.db.NmapJobDAO;

/**
 *
 * @author petrakos
 */
public class timeFilter extends JFrame implements WindowListener
{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panel ;
    private JLabel infoLabel;
    private JLabel start;
    private JLabel stop;
    private JComboBox<?> startDay;
    private JComboBox<?> stopDay;
    private JComboBox<?> startMonth;
    private JComboBox<?> stopMonth;
    private JComboBox<?> startHour;
    private JComboBox<?> stopHour;
    private JComboBox<?> startMinute;
    private JComboBox<?> startSecond;
    private JComboBox<?> stopMinute;
    private JComboBox<?> stopSecond;
    private JButton searchbtn;
    private JButton searchallbtn;
    private JOptionPane optionPane;
    private JDialog dialog ;
    private String hash;
    
    public timeFilter(String HASH)
    {
		this.hash = HASH;
        initComponents();
    }
    
    
    private void initComponents()
    {
        //setting JFrame properties//
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        this.setSize(screenWidth / 2, (int) (screenHeight / 1.5));
        setLocation(screenWidth / 4, screenHeight / 4);
        setResizable(false);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setTitle("Time filter");
        addWindowListener(this);
        
        panel = new JPanel();
        panel.setLayout(null);
        this.setContentPane(panel);
        
        
        String HTML = "<html><p> Please specify a time cap for your search!<p></html>";
        infoLabel = new JLabel(HTML);
        infoLabel.setBounds(100 , 20 , this.getWidth() - 130 , 30);
        start = new JLabel("Starting Point");
        start.setBounds(100 , 70 , 120 , 40);
        stop = new JLabel("Stoping Point");
        stop.setBounds(100 , 200 , 120 , 40);   
             
        //configure buttons//
        String [] days = {"01" , "02" , "03" , "04" , "05" , "06" ,"07", "08" ,
                          "09" , "10", "11" , "12" , "13" , "14" , "15" , 
                          "16" , "17" , "18" , "18" , "20" , "21" , "22" ,
                          "23" , "24" , "25" , "26" , "27" , "28" , "29" , "30" , "31"
                         };
        
     
        String [] months = {"01" , "02" , "03" , "04" , "05" , "06" ,"07", "08" ,
                            "09" , "10", "11" , "12"
                           };
        
        String [] hours = { "00" , "01" , "02" , "03" , "04" , "05" , "06" ,"07", "08" ,
                            "09" , "10", "11" , "12" , "13" , "14" , "15" , 
                            "16" , "17" , "18" , "18" , "20" , "21" , "22" , "23"
                          };
        String [] minutes = {"00" , "01" , "02" , "03" , "04" , "05" , "06" ,"07", "08" ,
                            "09" , "10", "11" , "12" , "13" , "14" , "15" , 
                            "16" , "17" , "18" , "18" , "20" , "21" , "22" , "23",
                            "24" , "25" , "26" , "27" , "28" , "29" , "30" , "31",
                            "32" , "33" , "34" , "35" , "36" , "37" , "38" , "39",
                            "40" , "41" , "42" , "43" , "44" , "45" , "46" , "47",
                            "48" , "49" , "50" , "51" , "52" , "53" , "54" , "55",
                            "56" , "57" , "58" , "59"};
        
        
        String [] seconds = {"00" , "01" , "02" , "03" , "04" , "05" , "06" ,"07", "08" ,
                            "09" , "10", "11" , "12" , "13" , "14" , "15" , 
                            "16" , "17" , "18" , "18" , "20" , "21" , "22" , "23",
                            "24" , "25" , "26" , "27" , "28" , "29" , "30" , "31",
                            "32" , "33" , "34" , "35" , "36" , "37" , "38" , "39",
                            "40" , "41" , "42" , "43" , "44" , "45" , "46" , "47",
                            "48" , "49" , "50" , "51" , "52" , "53" , "54" , "55",
                            "56" , "57" , "58" , "59"};

        startDay = new JComboBox<Object>(days);
        startDay.setBorder(new CompoundBorder(BorderFactory.createTitledBorder("Day"), startDay.getBorder()));
        startDay.setBounds(60 , 110 , 100 , 50 );
        
        stopDay = new JComboBox<Object>(days);
        stopDay.setBorder(new CompoundBorder(BorderFactory.createTitledBorder("Day"), stopDay.getBorder()));
        stopDay.setBounds(60 , 250 , 100 , 50 );
        
        startMonth = new JComboBox<Object>(months);
        startMonth.setBorder(new CompoundBorder(BorderFactory.createTitledBorder("Month"), startMonth.getBorder()));   
        startMonth.setBounds(170 , 110 , 100 , 50 );
        
        stopMonth = new JComboBox<Object>(months);
        stopMonth.setBorder(new CompoundBorder(BorderFactory.createTitledBorder("Month"), stopMonth.getBorder()));
        stopMonth.setBounds(170 , 250 , 100 , 50 );
        
        startHour = new JComboBox<Object>(hours);
        startHour.setBorder(new CompoundBorder(BorderFactory.createTitledBorder("Hour"), startHour.getBorder()));
        startHour.setBounds(290 , 110 , 100 , 50 );
        
        stopHour = new JComboBox<Object>(hours);
        stopHour.setBorder(new CompoundBorder(BorderFactory.createTitledBorder("Hour"), stopHour.getBorder()));
        stopHour.setBounds(290 , 250 , 100 , 50 );
        
        startMinute = new JComboBox<Object>(minutes);
        startMinute.setBorder(new CompoundBorder(BorderFactory.createTitledBorder("Minute"), startMinute.getBorder()));
        startMinute.setBounds(410 , 110 , 100 , 50 );
        
        stopMinute = new JComboBox<Object>(minutes);
        stopMinute.setBorder(new CompoundBorder(BorderFactory.createTitledBorder("Minute"), stopMinute.getBorder()));
        stopMinute.setBounds(410 , 250 , 100 , 50 );
        
        startSecond = new JComboBox<Object>(seconds);
        startSecond.setBorder(new CompoundBorder(BorderFactory.createTitledBorder("Second"), startSecond.getBorder()));
        startSecond.setBounds(530 , 110 , 100 , 50 );
        
        stopSecond = new JComboBox<Object>(seconds);
        stopSecond.setBorder(new CompoundBorder(BorderFactory.createTitledBorder("Second"), stopSecond.getBorder()));
        stopSecond.setBounds(530 , 250 , 100 , 50 );
        
        
        searchbtn = new JButton("Filtered Search");
        searchbtn.setBounds(120 , 340 , 210 , 50);
        searchbtn.setFont(new Font("Verdana", Font.BOLD , 15)); 
        ImageIcon icon1 = new ImageIcon("./src/main/java/dsAM/aggregatorManager/gui/Images/timedSearch.jpeg");
        Image img = icon1.getImage().getScaledInstance(37, 35, java.awt.Image.SCALE_SMOOTH);
        icon1 = new ImageIcon(img);
        searchbtn.setIcon(icon1);
        searchbtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchbtn.setForeground(Color.BLACK);
        searchbtn.setToolTipText("Search using the specified filters");
        
        searchallbtn = new JButton("Search All");
        searchallbtn.setBounds(360 , 340 , 210 , 50);
        searchallbtn.setFont(new Font("Verdana", Font.BOLD , 15)); 
        ImageIcon icon2 = new ImageIcon("./src/main/java/dsAM/aggregatorManager/gui/Images/searchall.jpeg");
        Image img2 = icon2.getImage().getScaledInstance(35, 35, java.awt.Image.SCALE_SMOOTH);
        icon2 = new ImageIcon(img2);
        searchallbtn.setIcon(icon2);
        searchallbtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchallbtn.setForeground(Color.BLACK);
        searchallbtn.setToolTipText("Search ignoring the filters");
        
        
        panel.add(start);
        panel.add(stop);
        panel.add(infoLabel);
        panel.add(stopDay);
        panel.add(startDay);
        panel.add(startMonth);
        panel.add(stopMonth);
        panel.add(startHour);
        panel.add(stopHour);
        panel.add(startMinute);
        panel.add(stopMinute);
        panel.add(startSecond);
        panel.add(stopSecond);
        panel.add(searchbtn);
        panel.add(searchallbtn);
        
        
        searchbtn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                searchbtn.setBackground(Color.green);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                searchbtn.setBackground(UIManager.getColor("control"));
            }
        });

        searchallbtn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                searchallbtn.setBackground(Color.green);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                searchallbtn.setBackground(UIManager.getColor("control"));
            }
        });
        
        
        searchallbtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event) {
                searchallbtnActionPerformed(event);
            }
        });

        searchbtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event) {
                searchbtnActionPerformed(event);
            }
        });    
           
    }
    
    
    private void searchallbtnActionPerformed(ActionEvent event) 
    {
        NmapJobDAO dao = new NmapJobDAO();
		ArrayList<String> assigned = dao.getResults(this.hash);
		showResults sr = new showResults();
		sr.setVisible(true);
		for (String result : assigned)
		{
			sr.addPanel(result);
		}
		
    }

    //filtered search
    private void searchbtnActionPerformed(ActionEvent event) 
    {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		
		String month1 =  startMonth.getSelectedItem().toString();
		String day1 =  startDay.getSelectedItem().toString();
		String hour1 =  startHour.getSelectedItem().toString();
		String minute1 =  startMinute.getSelectedItem().toString();
		String second1 =  startSecond.getSelectedItem().toString();
		String starts =  year + "-" + month1 + "-" + day1 + " " + hour1 + ":" + minute1 + ":" + second1;
		
		String month2 =  stopMonth.getSelectedItem().toString();
		String day2 =  stopDay.getSelectedItem().toString();
		String hour2 =  stopHour.getSelectedItem().toString();
		String minute2 =  stopMinute.getSelectedItem().toString();
		String second2 =  stopSecond.getSelectedItem().toString();
		String ends =  year + "-" + month2 + "-" + day2 + " " + hour2 + ":" + minute2 + ":" + second2;
		
        NmapJobDAO dao = new NmapJobDAO();
		ArrayList<String> assigned = dao.getFilteredResults(hash , starts , ends);
		showResults sr = new showResults();
		sr.setVisible(true);
		for (String result : assigned)
		{
			sr.addPanel(result);
		}
    }
    
    
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
