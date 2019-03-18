package GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import Database.Database;
import Functions.CheckPlausibility;
import Functions.CreateWorkspace;
import Functions.General;

public class MainFrame extends JFrame {
	
	//GUI Size
	final short width = 600;
	final short height = 800;
	final short chartPanelWidth = 490;
	
	int day = 0;
	int week = 0;
	
	
	int lastEditedRow = 0;
	boolean isTablePlausible = true;
	String [] wrongColumnValues = new String [6];
		
	//Class Declarations
	Database database = new Database();
    General general = new General();
	JPanel jp = new JPanel();
	DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	JFreeChart chart = ChartFactory.createBarChart3D("Working Hours: "+general.getDateFromWeek(week)[0]+" - " + general.getDateFromWeek(week)[1] ,"","", dataset, PlotOrientation.VERTICAL, false, false, false);
	CategoryPlot catplot = chart.getCategoryPlot();
	BarRenderer barRenderer = (BarRenderer) catplot.getRenderer();
	ChartPanel chartPanel = new ChartPanel(chart);
	String[] columnNames = {"Project", "Start Time", "End Time", "Pause","Description"};
	CreateWorkspace createWorkspace = new CreateWorkspace();
	CheckPlausibility checkPlausibility = new CheckPlausibility();
	DefaultTableModel model;
	JTable table;
	String [][] data;
	DefaultTableCellRenderer tableRenderer = new DefaultTableCellRenderer();
	JScrollPane scrollPane;
	ListSelectionModel selectionModel;
	
	//Variables
	private java.awt.Point initialClick;
	String userid = System.getProperty("user.name");
	
	String memberName = "No Connection";
    Date date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
	boolean isConnectedToSQL = false;
	float dayInMillis = 86400000;
    float [] weekHours = {0,0,0,0,0};
    final byte iconWidth = 32, iconHeight = 32;
    final Color background = new java.awt.Color(224, 74, 74);
     
    
    //GUI Elements
	JButton closeBtn, minimizeBtn, post, lastDay, nextDay, timeRegistrationTable,settings, lastWeek, nextWeek;
	JLabel title,border, postedtimes_title, statistic_title, hoursOfTheMonth, hoursOfTheWeek, hoursFromToday;
	
	
	public MainFrame() {
		
		isConnectedToSQL = database.checkConnection();
		
	    //table = new JTable(new DefaultTableModel(getTableData(), columnNames));
	 	
		table = new JTable(new DefaultTableModel(getTableData(), columnNames)) {
		    @Override
		    public boolean isCellEditable(int row, int column) {    
		    	
		    	if (row > 0) {
		    		if (column == 1) {
		    			return false;
		    		}
		    		if (column == 2 && table.getValueAt(row, 1) == "") {
		    			return false;
		    		}
		    	}
		    	return true;
		    };
			
			
		    @Override
		    public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
		        Component comp = super.prepareRenderer(renderer, row, col);
		        Object value = getModel().getValueAt(row, col);
		        if (lastEditedRow == row && !isTablePlausible) {
		        	if (value.equals(wrongColumnValues[0]) || value.equals(wrongColumnValues[1]) || value.equals(wrongColumnValues[2]) ||
		        		value.equals(wrongColumnValues[3]) || value.equals(wrongColumnValues[4])){
		        		comp.setBackground(Color.RED);
		        	}else {
		        		comp.setBackground(Color.WHITE);
		        	}
		        } else {
		            comp.setBackground(Color.WHITE);
		        }
		        
		        return comp;
		    }
		};
		
		 model = (DefaultTableModel) table.getModel();

	    scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setViewportView(table);
		
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
		scrollPane.setBounds((width-490)/2,250,490,320);
		//37
		chartPanel.setBounds(general.centerObject(490), 600, 490, 200);
		
		closeBtn = new JButton();
		closeBtn.setBounds(568, 0, iconWidth, iconHeight);
		closeBtn.setBorderPainted(false);
		closeBtn.setBorder(null);
		closeBtn.setContentAreaFilled(false);
		closeBtn.setIcon(new ImageIcon(getClass().getResource("/close_operation.png")));
		
		minimizeBtn = new JButton();
		minimizeBtn.setBounds(532, 0, iconWidth, iconHeight);
		minimizeBtn.setBorderPainted(false);
		minimizeBtn.setBorder(null);
		minimizeBtn.setContentAreaFilled(false);
		minimizeBtn.setIcon(new ImageIcon(getClass().getResource("/minimize_operation.png")));
		
	
		title = new JLabel("Time Tracker: " + memberName);
		title.setBounds((width-350)/2,0,350,32);
		title.setFont(new Font("Calibri",Font.PLAIN,22));
		title.setForeground(Color.WHITE);
		
		border = new JLabel("_____________________________________________________________________________________________________________________________");
		border.setBounds(0,12,width+100,32);
		border.setHorizontalAlignment(JLabel.CENTER);
		border.setFont(new Font("Calibri",Font.PLAIN,22));
		border.setForeground(Color.WHITE);
		
		statistic_title = new JLabel("Statistic");
		statistic_title.setBounds(general.centerObject(80),50, 80, 30);
		statistic_title.setForeground(Color.white);
		statistic_title.setFont(new Font("Calibri", Font.PLAIN, 22)); 
		
		hoursOfTheMonth = new JLabel("Month: 160.00 hrs.");
		hoursOfTheMonth.setBounds(general.centerObject(120),70,120,50);
		hoursOfTheMonth.setForeground(Color.white);
		hoursOfTheMonth.setFont(new Font("Calibri", Font.PLAIN, 14)); 
		
		hoursOfTheWeek = new JLabel("Week: " + general.getTotalHoursOfTheWeek(weekHours) + " hrs.");
		hoursOfTheWeek.setBounds(general.centerObject(120),90,120,50);
		hoursOfTheWeek.setForeground(Color.white);
		hoursOfTheWeek.setFont(new Font("Calibri", Font.PLAIN, 14)); 
		
		hoursFromToday = new JLabel("Today: " + general.getTotalHoursOfToday(weekHours) + "  hrs.");
		hoursFromToday.setBounds(general.centerObject(120),110,120,50);
		hoursFromToday.setForeground(Color.white);
		hoursFromToday.setFont(new Font("Calibri", Font.PLAIN, 14)); 
		
		postedtimes_title = new JLabel();
		postedtimes_title.setBounds(general.centerObject(280),200,280,32);
		postedtimes_title.setFont(new Font("Calibri", Font.PLAIN, 22)); 
		postedtimes_title.setForeground(Color.white);
		postedtimes_title.setText("Registered Times: " + calculateSelectedDay());
		
		lastDay = new JButton();
		lastDay.setBounds(0, general.centerVerticallyOnObj(scrollPane.getY(),scrollPane.getHeight(),iconHeight), iconWidth, iconHeight);
		lastDay.setBackground(new java.awt.Color(224, 74, 74,0));
		lastDay.setBorderPainted(false);
		lastDay.setBorder(null);
		lastDay.setContentAreaFilled(false);
		lastDay.setIcon(new ImageIcon(getClass().getResource("/back.png")));
		
		nextDay = new JButton();
		nextDay.setBounds(568, general.centerVerticallyOnObj(scrollPane.getY(),scrollPane.getHeight(),iconHeight), iconWidth, iconHeight);
		nextDay.setBackground(new java.awt.Color(224, 74, 74,0));
		nextDay.setBorderPainted(false);
		nextDay.setBorder(null);
		nextDay.setContentAreaFilled(false);
		nextDay.setIcon(new ImageIcon(getClass().getResource("/next.png")));
		
		timeRegistrationTable = new JButton();
		timeRegistrationTable.setBounds(5, 0, iconWidth, iconHeight);
		timeRegistrationTable.setBackground(new java.awt.Color(224, 74, 74,0));
		timeRegistrationTable.setBorderPainted(false);
		timeRegistrationTable.setBorder(null);
		timeRegistrationTable.setContentAreaFilled(false);
		timeRegistrationTable.setIcon(new ImageIcon(getClass().getResource("/list.png")));
		
		settings = new JButton();
		settings.setBounds(50, 0, iconWidth, iconHeight);
		settings.setBackground(new java.awt.Color(224, 74, 74,0));
		settings.setBorderPainted(false);
		settings.setBorder(null);
		settings.setContentAreaFilled(false);
		settings.setIcon(new ImageIcon(getClass().getResource("/settings.png")));
		
		lastWeek = new JButton();
		lastWeek.setBounds(0, general.centerVerticallyOnObj(chartPanel.getY(),chartPanel.getHeight(),iconHeight), iconWidth, iconHeight);
		lastWeek.setBackground(new java.awt.Color(224, 74, 74,0));
		lastWeek.setBorderPainted(false);
		lastWeek.setBorder(null);
		lastWeek.setContentAreaFilled(false);
		lastWeek.setIcon(new ImageIcon(getClass().getResource("/back.png")));
		
		nextWeek = new JButton();
		nextWeek.setBounds(568, general.centerVerticallyOnObj(chartPanel.getY(),chartPanel.getHeight(),iconHeight), iconWidth, iconHeight);
		nextWeek.setBackground(new java.awt.Color(224, 74, 74,0));
		nextWeek.setBorderPainted(false);
		nextWeek.setBorder(null);
		nextWeek.setContentAreaFilled(false);
		nextWeek.setIcon(new ImageIcon(getClass().getResource("/next.png")));
						
		//table.setBounds((width-490)/2,250,490,320);
		
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
		//add(table);
		add(scrollPane);
		add(chartPanel);
		add(lastDay);
		add(nextDay);
		add(lastWeek);
		add(nextWeek);
		add(jp);
		validate();
		
		jp.setBackground(background);	
		
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
				day++;
				getTimeRegistrationsFromSelectedDay();
			}
		});
		
		nextDay.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				day--;
				getTimeRegistrationsFromSelectedDay();
			}
		});
		
		lastWeek.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				week++;
				refreshBarChart();
			}
		});
		
		nextWeek.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				week--;
				refreshBarChart();
			}
		});
		
		timeRegistrationTable.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				openAllTimeRegistrations();
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
	
	private void openAllTimeRegistrations() {
		if (isConnectedToSQL) {
			TimeRegistrationsFrame timeRegistrationFrame = new TimeRegistrationsFrame();
		}
	}
	
	private void OnAfterValidateTable() { 
		if (table.isEnabled()) {
			fetchLastEndTime(); 
			correctStartAndEndTime();
			correctPause();
			checkIfInputIsTooLong();
			if (isRowPlausible()) {
				addRowIfNeeded();	
				updateOrInsertRecord();
			}
		}
	}
	
	private void correctPause() {
		//if (table.getValueAt(table.getEditingRow(), 3).toString() != null && table.getValueAt(table.getEditingRow(), 3).toString() != "") {
			String pauseValue = "";
			try {
				pauseValue = table.getValueAt(table.getEditingRow(), 3).toString();
				if (IsPauseIntegerOrFloat(pauseValue)) {
					if (pauseValue.contains(",")) {
						pauseValue = pauseValue.replace(",", ".");
						setValueInColumn(pauseValue, 3); 
					}else if (pauseValue.length() >= 3 && !pauseValue.contains(".")) {
						pauseValue = pauseValue.substring(0, 1) + "." + pauseValue.substring(1, 3);
						setValueInColumn(pauseValue, 3); 
					}
				}else {
					JOptionPane.showMessageDialog(null, "Pause should be written like '0' or '0.00'", "Pause is'nt Valid", 0);
					pauseValue = "0.00";
					setValueInColumn(pauseValue, 3); 
				}
			}catch(NullPointerException ex) {
				
			}
	}
	
	private void setValueInColumn(String value, int column) {
		table.setValueAt(value, table.getEditingRow(), column);
	}
	
	private boolean IsPauseIntegerOrFloat(String pauseValue) {
		
		boolean isInputValid = false;
		
		if (pauseValue.contains(",")) {
			pauseValue = pauseValue.replace(",", ".");
		}
		
		try {
			if (pauseValue != null && pauseValue != "" && !pauseValue.isEmpty()) {
				int testValue = Integer.parseInt(pauseValue);
				isInputValid = true;
			}
		}catch(NumberFormatException ex) {}
		
		try {
			if (pauseValue != null && pauseValue != "" && !pauseValue.isEmpty()) {
				Float testValue = Float.parseFloat(pauseValue);
				isInputValid = true;
			}
		}catch(NumberFormatException ex) {}
		
		return isInputValid;
	}
	
	private void correctStartAndEndTime() {
		if (table.getValueAt(table.getEditingRow(), 2) != null && table.getValueAt(table.getEditingRow(), 2) != "" && table.getEditingColumn() != 2) {
			String columnVal = table.getValueAt(table.getEditingRow(), 2).toString();
			if (columnVal.length() < 5 && columnVal != null) {
				try {
					LocalTime endTime = LocalTime.of(checkPlausibility.splitStringIntoHoursAndMinutes(columnVal)[0], checkPlausibility.splitStringIntoHoursAndMinutes(columnVal)[1]);	
					table.setValueAt(endTime, table.getEditingRow(), 2);
					isTablePlausible = true;
				}catch(StringIndexOutOfBoundsException ex) {
					wrongColumnValues[2] = table.getValueAt(table.getEditingRow(), 2).toString();
					isTablePlausible = false;
					JOptionPane.showMessageDialog(null, "Please change your End Time", "End Time is Invalid", JOptionPane.OK_CANCEL_OPTION);
				}
			}
		}else if (table.getValueAt(table.getEditingRow(), 1) != null && table.getValueAt(table.getEditingRow(), 1) != "" && table.getEditingColumn() != 1) {
			String columnVal = table.getValueAt(table.getEditingRow(), 1).toString();
			if (columnVal.length() < 5 && columnVal != null) {
				try {
					LocalTime startTime = LocalTime.of(checkPlausibility.splitStringIntoHoursAndMinutes(columnVal)[0], checkPlausibility.splitStringIntoHoursAndMinutes(columnVal)[1]);	
					table.setValueAt(startTime, table.getEditingRow(), 1);
					isTablePlausible = true;
				}catch(StringIndexOutOfBoundsException ex) {
//					wrongColumnValues[1] = table.getValueAt(table.getEditingRow(), 2).toString();
//					isTablePlausible = false;
					JOptionPane.showMessageDialog(null, "Please change your Start Time", "Start Time is Invalid", JOptionPane.OK_CANCEL_OPTION);
				}
			}
		}
	}
	
	private void correctPauseTime() {
		
	}
	
	private void checkIfInputIsTooLong() {
		int currColumn = table.getEditingColumn();
		String columnValue = ""+table.getValueAt(table.getEditingRow(), table.getEditingColumn());
		if (currColumn == 0 && columnValue.length() > 11) {
			columnValue = columnValue.substring(0, 11);
			table.setValueAt(columnValue, table.getEditingRow(), table.getEditingColumn());
			//Message?
		}
	}
	
	
	private boolean isRowPlausible() {
		lastEditedRow = table.getEditingRow();
		
		String rowValues [] = storeRowDataInArray();
		
		if (rowValues[0] != null && rowValues[1] != null && rowValues[2] != null && rowValues[3] != null && rowValues[4] != null) {		
			if (rowValues[0].length() > 0 && rowValues[1].length() > 0 && rowValues[2].length() > 0 && rowValues[3].length() > 0 && rowValues[4].length() > 0 ){
				if (!checkPlausibility.checkIfStartTimeIsBeforeLastEndTime(""+table.getValueAt(table.getEditingRow(), 1),""+table.getValueAt(table.getEditingRow(), 2))) {	
					isTablePlausible = true;
					return true;
				}else {
					wrongColumnValues[2] = table.getValueAt(table.getEditingRow(), 2).toString();
					isTablePlausible = false;
				}
			}
		}
		return false;
	}
		
	private void fetchLastEndTime() {
		final String lastEndTime = database.fetchLastEndTimeFromToday();
		
		 if (table.getValueAt(table.getEditingRow(), 0) != null && table.getEditingRow() > 0)   {
			 if (table.getValueAt(table.getEditingRow(), 1) == null || table.getValueAt(table.getEditingRow(), 1) == "" ) {
				 table.setValueAt(lastEndTime, table.getEditingRow(), 1);
			 }
		 }
	}
	
	private void addRowIfNeeded() {
		 if (table.getEditingRow()+1 == table.getRowCount()) {
			Object newRow [] = {"","","","0.00",""};
			model.addRow(newRow);
		 }
	}
	
	private void updateOrInsertRecord() {
		
		 String rowData [] = storeRowDataInArray();
		
		if (rowData[0] != null && rowData[1] != null && rowData[2] != null && rowData[4] != null) {
			database.changeTimeRegistrationDataset(rowData, table.getEditingColumn());
			refreshBarChart();
		}
	}
	
	private String [] storeRowDataInArray() {
		 String rowData [] = new String [5];
		 rowData[0] = (String) table.getValueAt(table.getEditingRow(), 0);
		 rowData[1] = ""+table.getValueAt(table.getEditingRow(), 1);
		 rowData[2] = ""+table.getValueAt(table.getEditingRow(), 2);
		 rowData[3] = (String) table.getValueAt(table.getEditingRow(), 3);
		 rowData[4] = (String) table.getValueAt(table.getEditingRow(), 4);
		 return rowData;
	}
		
	private void OnAfterSQLConnect() {
		if (isConnectedToSQL) {
			memberName = database.fetchForAndLastname(userid)[0] + " " + database.fetchForAndLastname(userid)[1];
			weekHours = database.fetchAllTimeRegistrationsFromTheWeek();
			selectionModel = table.getSelectionModel();
		}
	}
	
	private String [][] getTableData() {
		if (!isConnectedToSQL) {
			String [][] data = generateNoConnectionTableData();
			return data;
		}else {
			String[][] data = database.fetchAllTimeRegistrationsFromToday();
			return data;
		}
	}
	
	private String [][] generateNoConnectionTableData() {
		String [][] data = new String [20][5];
		for (int i=0; i < 20; i++) {
			data[i][0] = "NO";
			data[i][1] = "CONNECTION";
			data[i][2] = "TO";
			data[i][3] = "DATABASE";
			data[i][4] = ":(";
		}
		return data;
	}
	
	private void disableSQLObjectsIfNoConnectionFound() {
		if (!isConnectedToSQL) {
			table.disable();
		}
	}
				
	public void getTimeRegistrationsFromSelectedDay() {
		removeAllRows();
		addRowsWithRecordsFromSelectedDay();
		postedtimes_title.setText("Registered Times: " + calculateSelectedDay());
	}
	
	private void removeAllRows() {
		table.disable();
		int wholeRows = table.getRowCount()-1;
		for (int i=wholeRows; i >= 0; i--) {
			model.removeRow(i);
		}
	}
	
	private void addRowsWithRecordsFromSelectedDay() {
		String [][] tableData = database.fetchTimeRegistrationsFromSelectedDay(day);
		for (int i=0; i < tableData.length; i++) {
			model.addRow(tableData[i]);
		}
		if (day == 0) {
			table.enable();
		}
	}
	
	private String calculateSelectedDay() {
		String date = sdf.format(System.currentTimeMillis()-((day) * (dayInMillis)));
		return date;
	}
	//#TODO 
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
			barRenderer.setBase(0);
		}
	}
	
	private void updateChartDescription() {
		chart.setTitle("Working Hours: "+general.getDateFromWeek(week)[0]+" - " + general.getDateFromWeek(week)[1]);
	}
	
	public void refreshBarChart() {
		if (isConnectedToSQL) {
			if (week == 0) {
				setBarChartValues(database.fetchAllTimeRegistrationsFromTheWeek());
			}else {
				setBarChartValues(database.fetchAllTimeRegistrationsFromTheSelectedWeek(week));
			}
			updateChartDescription();
		}
	}	
	private void setBarChartValues(float [] newWeekHours) {
		dataset.setValue(newWeekHours[0], "", "Mon.");
		dataset.setValue(newWeekHours[1], "", "Tue.");
		dataset.setValue(newWeekHours[2], "", "Wed.");
		dataset.setValue(newWeekHours[3], "", "Thu.");
		dataset.setValue(newWeekHours[4], "", "Fri.");
	}
	

}

