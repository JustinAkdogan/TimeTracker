package GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import Database.Database;
import Functions.CreateWorkspace;
import Functions.General;

public class MainFrame extends JFrame {
	
	//GUI Size
	final short width = 600;
	final short height = 800;
	final short chartPanelWidth = 490;
	boolean isConnectedToSQL = false;
	
	//Class Declarations
	Database database = new Database();
    General general = new General();
	JPanel jp = new JPanel();
	JScrollPane scrollPane = new JScrollPane();
	DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	JFreeChart chart = ChartFactory.createBarChart3D("Arbeitszeiten","","", dataset, PlotOrientation.VERTICAL, false, false, false);
	CategoryPlot catplot = chart.getCategoryPlot();
	BarRenderer barRenderer = (BarRenderer) catplot.getRenderer();
	ChartPanel chartPanel = new ChartPanel(chart);
	String[] columnNames = {"", "", "", "",""};
	CreateWorkspace createWorkspace = new CreateWorkspace();
	
	//Variables
	private java.awt.Point initialClick;
	String userid = System.getProperty("user.name");
	int day = 0;
	String memberName = "Keine Verbindung";
    Date date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    
    JTable table;

    float [] weekHours = {0,0,0,0,0,0};
     
    
    //GUI Elements
	JButton closeBtn, minimizeBtn, post, lastDay, nextDay, timeRegistrationTable,settings;
	JLabel title,border, postedtimes_title, statistic_title, hoursOfTheMonth, hoursOfTheWeek, hoursFromToday;
	
	
	public MainFrame() {
		
		isConnectedToSQL = database.checkConnection();
		
		table = new JTable(getTableData() , columnNames) {
		    @Override
		    public boolean isCellEditable(int row, int column) {    
		    	if (row >= 1) {
		    		String cellValue = (String) table.getValueAt(row-1, column); //#TODO FIX
		    		if (cellValue == "" || cellValue == null) {
		    			return false;
		    		}else {
		    			return true; 
		    		}
		    	}else {
		    		return false;
		    	}	    
		    };
		};
		
		//GUI Layout
		setSize(width, height);
		setLocationRelativeTo(null);
		setUndecorated(true);
		
		//Functions
		OnAfterSQLConnect();
		setupBarChart();
		refreshBarChart();
		disableSQLObjectsIfNoConnectionFound();
		
		
		//Element Options
				
		
		
		closeBtn = new JButton();
		closeBtn.setBounds(568, 0, 32, 32);
		closeBtn.setBorderPainted(false);
		closeBtn.setBorder(null);
		closeBtn.setContentAreaFilled(false);
		closeBtn.setIcon(new ImageIcon(getClass().getResource("/close_operation.png")));
		
		minimizeBtn = new JButton();
		minimizeBtn.setBounds(532, 0, 32, 32);
		minimizeBtn.setBorderPainted(false);
		minimizeBtn.setBorder(null);
		minimizeBtn.setContentAreaFilled(false);
		minimizeBtn.setIcon(new ImageIcon(getClass().getResource("/minimize_operation.png")));
		
	
		title = new JLabel("Zeiterfassung: " + memberName);
		title.setBounds((width-350)/2,0,350,32);
		title.setFont(new Font("Calibri",Font.PLAIN,22));
		title.setForeground(Color.WHITE);
		
		border = new JLabel("_____________________________________________________________________________________________________________________________");
		border.setBounds(0,12,width+100,32);
		border.setHorizontalAlignment(JLabel.CENTER);
		border.setFont(new Font("Calibri",Font.PLAIN,22));
		border.setForeground(Color.WHITE);
		
		statistic_title = new JLabel("Statistik");
		statistic_title.setBounds(general.centerObject(80),50, 80, 30);
		statistic_title.setForeground(Color.white);
		statistic_title.setFont(new Font("Calibri", Font.PLAIN, 22)); 
		
		hoursOfTheMonth = new JLabel("Monat: 160.00 std.");
		hoursOfTheMonth.setBounds(general.centerObject(120),70,120,50);
		hoursOfTheMonth.setForeground(Color.white);
		hoursOfTheMonth.setFont(new Font("Calibri", Font.PLAIN, 14)); 
		
		hoursOfTheWeek = new JLabel("Woche: " + general.getTotalHoursOfTheWeek(weekHours) + " std.");
		hoursOfTheWeek.setBounds(general.centerObject(120),90,120,50);
		hoursOfTheWeek.setForeground(Color.white);
		hoursOfTheWeek.setFont(new Font("Calibri", Font.PLAIN, 14)); 
		
		hoursFromToday = new JLabel("Heute: " + general.getTotalHoursOfToday(weekHours) + "  std.");
		hoursFromToday.setBounds(general.centerObject(120),110,120,50);
		hoursFromToday.setForeground(Color.white);
		hoursFromToday.setFont(new Font("Calibri", Font.PLAIN, 14)); 
		
		postedtimes_title = new JLabel("Gebuchte Zeiten: " + sdf.format(date));
		postedtimes_title.setBounds(general.centerObject(260),200,260,32);
		postedtimes_title.setFont(new Font("Calibri", Font.PLAIN, 22)); 
		postedtimes_title.setForeground(Color.white);
		
		lastDay = new JButton();
		lastDay.setBounds(0, 280, 32, 32);
		lastDay.setBackground(new java.awt.Color(224, 74, 74,0));
		lastDay.setBorderPainted(false);
		lastDay.setBorder(null);
		lastDay.setContentAreaFilled(false);
		lastDay.setIcon(new ImageIcon(getClass().getResource("/back.png")));
		
		nextDay = new JButton();
		nextDay.setBounds(568, 280, 32, 32);
		nextDay.setBackground(new java.awt.Color(224, 74, 74,0));
		nextDay.setBorderPainted(false);
		nextDay.setBorder(null);
		nextDay.setContentAreaFilled(false);
		nextDay.setIcon(new ImageIcon(getClass().getResource("/next.png")));
		
		timeRegistrationTable = new JButton();
		timeRegistrationTable.setBounds(5, 0, 32, 32);
		timeRegistrationTable.setBackground(new java.awt.Color(224, 74, 74,0));
		timeRegistrationTable.setBorderPainted(false);
		timeRegistrationTable.setBorder(null);
		timeRegistrationTable.setContentAreaFilled(false);
		timeRegistrationTable.setIcon(new ImageIcon(getClass().getResource("/list.png")));
		
		settings = new JButton();
		settings.setBounds(50, 0, 32, 32);
		settings.setBackground(new java.awt.Color(224, 74, 74,0));
		settings.setBorderPainted(false);
		settings.setBorder(null);
		settings.setContentAreaFilled(false);
		settings.setIcon(new ImageIcon(getClass().getResource("/settings.png")));
						
		table.setBounds((width-490)/2,250,490,320);
		
		chartPanel.setBounds(general.centerObject(490), 600, 490, 200);
		
		//Adding Elements
		add(statistic_title);
		add(closeBtn);
		add(minimizeBtn);
		add(hoursOfTheMonth);
		add(hoursOfTheWeek);
		add(hoursFromToday);
		add(title);
		add(border);
		add(postedtimes_title);
		add(timeRegistrationTable);
		add(settings);
		add(table);
		add(chartPanel);
		add(lastDay);
		add(nextDay);
		add(jp);
		validate();
		
		jp.setBackground(new java.awt.Color(224, 74, 74));	
		
		//GUI Trigger
		
		closeBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		
		minimizeBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setState(Frame.ICONIFIED);
			}
		});
		
		lastDay.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				day = day + 1;
				getTimeRegistrationsFromSelectedDay();
			}
		});
		
		nextDay.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				day = day - 1;
				getTimeRegistrationsFromSelectedDay();
			}
		});
		
		timeRegistrationTable.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (isConnectedToSQL) {
					if (database.getAuthorizationLevel() >= 1) {
						TimeRegistrationsFrame timeRegistrationFrame = new TimeRegistrationsFrame();
					}
				}
			}
		});
		
		table.getModel().addTableModelListener(new TableModelListener() {

			  public void tableChanged(TableModelEvent e) {
				  OnAfterValidateTable();
			  }
		});
		
		settings.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SettingsFrame settingsFrame = new SettingsFrame();
			}
		});
				
		/* Moving the GUI */
		  addMouseListener(new MouseAdapter() {
		        public void mousePressed(MouseEvent e) {
		            initialClick = e.getPoint();
		            getComponentAt(initialClick);
		        }
		    });
		
		   addMouseMotionListener(new MouseMotionAdapter() {
		        @Override
		        public void mouseDragged(MouseEvent e) {

		            // get location of Window
		            int thisX = getLocation().x;
		            int thisY = getLocation().y;

		            // Determine how much the mouse moved since the initial click
		            int xMoved = e.getX() - initialClick.x;
		            int yMoved = e.getY() - initialClick.y;

		            // Move window to this position
		            int X = thisX + xMoved;
		            int Y = thisY + yMoved;
		            setLocation(X, Y);
		        }
		    });
	}
	
	//Functions
	private void OnAfterValidateTable() { 
		 //Fetch last end time
		 String lastEndTime = database.fetchLastEndTimeFromToday();
		 if (table.getValueAt(table.getEditingRow(), 1) == null && table.getValueAt(table.getEditingRow(), 0) != null) {
			 table.setValueAt(lastEndTime, table.getEditingRow(), 1);
		 }
		 
		 String rowData [] = new String [8];
		 rowData[0] = (String) table.getValueAt(table.getEditingRow(), 0);
		 rowData[1] = (String) table.getValueAt(table.getEditingRow(), 1);
		 rowData[2] = (String) table.getValueAt(table.getEditingRow(), 2);
		 rowData[3] = (String) table.getValueAt(table.getEditingRow(), 3);
		 rowData[4] = (String) table.getValueAt(table.getEditingRow(), 4);
		
		if (rowData[0] != null && rowData[1] != null && rowData[2] != null && rowData[4] != null) {
			//Edit existing Dataset or Insert a new Dataset if all fields are filled
			database.changeTimeRegistrationDataset(rowData, table.getEditingColumn());
			//Refresh the Barchart if all fields are filled
			refreshBarChart();
		}
	}
	
	private void OnAfterSQLConnect() {
		if (isConnectedToSQL) {
			memberName = database.fetchForAndLastname(userid)[0] + " " + database.fetchForAndLastname(userid)[1];
			weekHours = database.fetchAllTimeRegistrationsFromTheWeek();
		}
	}
	
	private String [][] getTableData() {
		if (!isConnectedToSQL) {
			String [][] data = {
					{"NO","CONNECTION","TO","DATABASE",":("},
					{"NO","CONNECTION","TO","DATABASE",":("},
					{"NO","CONNECTION","TO","DATABASE",":("},
					{"NO","CONNECTION","TO","DATABASE",":("},
					{"NO","CONNECTION","TO","DATABASE",":("},
					{"NO","CONNECTION","TO","DATABASE",":("},
					{"NO","CONNECTION","TO","DATABASE",":("},
					{"NO","CONNECTION","TO","DATABASE",":("},
					{"NO","CONNECTION","TO","DATABASE",":("},
					{"NO","CONNECTION","TO","DATABASE",":("},
					{"NO","CONNECTION","TO","DATABASE",":("},
					{"NO","CONNECTION","TO","DATABASE",":("},
					{"NO","CONNECTION","TO","DATABASE",":("},
					{"NO","CONNECTION","TO","DATABASE",":("},
					{"NO","CONNECTION","TO","DATABASE",":("},
					{"NO","CONNECTION","TO","DATABASE",":("},
					{"NO","CONNECTION","TO","DATABASE",":("},
					{"NO","CONNECTION","TO","DATABASE",":("},
					{"NO","CONNECTION","TO","DATABASE",":("},
					{"NO","CONNECTION","TO","DATABASE",":("},
			};
			return data;
		}else {
			String[][] data = database.fetchAllTimeRegistrationsFromToday();
			return data;
		}
	}
	
	private void disableSQLObjectsIfNoConnectionFound() {
		if (!isConnectedToSQL) {
			table.disable();
		}
	}
				
	public void getTimeRegistrationsFromSelectedDay() {
//		String [][] tableData = database.fetchTimeRegistrationsFromSelectedDay(day);
//		for (int i=0; i < tableData.length; i++) {
//			model.setValueAt(tableData[i], i, i);
//		}
	}
	
	private void setupBarChart() {
		barRenderer.setBaseCreateEntities(true);
		barRenderer.setSeriesPaint(0, Color.RED);
		barRenderer.setBaseFillPaint(Color.YELLOW);
		barRenderer.setAutoPopulateSeriesFillPaint(true);
		barRenderer.setBasePaint(Color.YELLOW);
		barRenderer.setSeriesFillPaint(0, Color.YELLOW);
		barRenderer.setBaseOutlinePaint(Color.YELLOW);
		barRenderer.setItemMargin(0.1);
		if (!isConnectedToSQL) {
			barRenderer.setBase(8);
		}
	}
	
	public void refreshBarChart() {
		if (isConnectedToSQL) {
			float [] weekHours = database.fetchAllTimeRegistrationsFromTheWeek();
		}
		dataset.setValue(weekHours[0], "", "Montag");
		dataset.setValue(weekHours[1], "", "Dienstag");
		dataset.setValue(weekHours[2], "", "Mittwoch");
		dataset.setValue(weekHours[3], "", "Donnerstag");
		dataset.setValue(weekHours[4], "", "Freitag");
	}	
}

