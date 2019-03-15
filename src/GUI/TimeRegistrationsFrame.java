package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.text.Element;

import Database.Database;
import Functions.General;

public class TimeRegistrationsFrame extends JFrame{
	
	final short width = 600;
	final short height = 800;
	JButton closeBtn, minimizeBtn, startSearch, showOwnTimeRegistrations;
	JLabel title,border,filter_title,timeRegistration_title;
	JTextField date,membername,projectid,start,end,pause,description;
	JPanel jp = new JPanel(null);
	Database database = new Database();
	General general = new General();
	private java.awt.Point initialClick;
	
	Object[][] data = database.fetchAllTimeRegistrationEntries();
	
	String [] columnNames = getColumnNamesForCurrentUser();
	
	DefaultTableModel model = new DefaultTableModel(data, columnNames);
		
	final byte iconWidth = 32, iconHeight = 32;
	
	boolean showAllTimeRegistrations = true;
	
	int authorizationLevel = 0;
	
	
	public TimeRegistrationsFrame() {
		
		JTable table = new JTable(model);
		
		JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		//authorizationLevel = database.getAuthorizationLevel();
		
		//Layout
		setSize(width, height);
		setLocationRelativeTo(null);
		setUndecorated(true);
		setVisible(true);
		
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
		
		showOwnTimeRegistrations = new JButton("Show My Times");
		showOwnTimeRegistrations.setBounds(355, 150, 150, 25);
		showOwnTimeRegistrations.setBackground(new java.awt.Color(224, 74, 74));
		showOwnTimeRegistrations.setIcon(new ImageIcon("res/checked.png"));
		showOwnTimeRegistrations.setFocusPainted(false);
		showOwnTimeRegistrations.setBorderPainted(false);
		showOwnTimeRegistrations.setBorder(null);
		
		startSearch = new JButton();
		startSearch.setBounds(general.centerObject(iconWidth), 200, iconWidth, iconHeight); // Y 150
		startSearch.setBorderPainted(false);
		startSearch.setBorder(null);
		startSearch.setContentAreaFilled(false);
		startSearch.setIcon(new ImageIcon(getClass().getResource("/search.png")));
		
	
		title = new JLabel("All Time Registrations");
		title.setBounds(general.centerObject(290),0,290,32);
		title.setFont(new Font("Calibri",Font.PLAIN,22));
		title.setForeground(Color.WHITE);
		
		border = new JLabel("_____________________________________________________________________________________________________________________________");
		border.setBounds(0,12,width+100,32);
		border.setHorizontalAlignment(JLabel.CENTER);
		border.setFont(new Font("Calibri",Font.PLAIN,22));
		border.setForeground(Color.WHITE);
		
		filter_title = new JLabel("Filter");
		filter_title.setBounds(general.centerObject(150),60,150,32);
		filter_title.setFont(new Font("Calibri",Font.PLAIN,22));
		filter_title.setForeground(Color.WHITE);
		
		date = new JTextField("2019-03-07");
		date.setBounds(50,100,80,32);
		date.setForeground(Color.GRAY);
		
		membername = new JTextField("Justin.Akdogan");
		membername.setBounds(140,100,120,32);
		membername.setForeground(Color.GRAY);
		
		projectid = new JTextField("DL1800274");
		projectid.setBounds(270,100,70,32);
		projectid.setForeground(Color.GRAY);
		
		start = new JTextField("08:30");
		start.setBounds(350,100,40,32);
		start.setForeground(Color.GRAY);
		
		end = new JTextField("11:30");
		end.setBounds(400,100,40,32);
		end.setForeground(Color.GRAY);
		
		pause = new JTextField("0.33");
		pause.setBounds(450,100,40,32);
		pause.setForeground(Color.GRAY);
		
		description = new JTextField("Merge Einweisung");
		description.setBounds(50,150,290,32);
		description.setForeground(Color.GRAY);
		
		timeRegistration_title = new JLabel("Time Registrations");
		timeRegistration_title.setBounds(general.centerObject(200),260,200,32);
		timeRegistration_title.setFont(new Font("Calibri",Font.PLAIN,22));
		timeRegistration_title.setForeground(Color.WHITE);
		
		table.disable();
		scrollPane.setBounds(general.centerObject(500), 300, 500, 450);
		
		changeSetupToAuthorizationLevel();
	
		
		//Adding Elements
		jp.add(closeBtn);
		jp.add(minimizeBtn);
		jp.add(title);
		jp.add(border);
		jp.add(scrollPane);
		jp.add(date);
		jp.add(membername);
		jp.add(projectid);
		jp.add(start);
		jp.add(end);
		jp.add(pause);
		jp.add(description);
		jp.add(startSearch);
		jp.add(filter_title);
		jp.add(timeRegistration_title);
		jp.add(showOwnTimeRegistrations);
		add(jp);
		setPreferredSize(new Dimension(600,800));
		pack();
		setVisible(true);
	
		jp.setBackground(new java.awt.Color(224, 74, 74));	
		
		//Trigger
		closeBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		
		minimizeBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setState(Frame.ICONIFIED);
			}
		});
		
		startSearch.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String [][] test = database.filterTimeRegistrationDatasets(getInput());
				int wholeRows = table.getRowCount()-1;
				for (int i=wholeRows; i >= 0; i--) {
					model.removeRow(i);
				}
				for (int i=0; i < test.length; i++) {
					model.addRow(test[i]);
				}
			}
		});
		
		showOwnTimeRegistrations.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				showAllTimeRegistrations = !showAllTimeRegistrations;
				if (showAllTimeRegistrations) {
					showOwnTimeRegistrations.setIcon(new ImageIcon(getClass().getResource("/checked.png")));
				}else {
					showOwnTimeRegistrations.setIcon(new ImageIcon(getClass().getResource("/unchecked.png")));
				}
			}
		});
		
		//Placeholder Fields
		
		date.addFocusListener(new FocusListener() {
		    @Override
		    public void focusGained(FocusEvent e) {
		        if (date.getText().equals("2019-03-07")) {
		        	date.setText("");
		        	date.setForeground(Color.BLACK);
		        }
		    }
		    @Override
		    public void focusLost(FocusEvent e) {
		        if (date.getText().isEmpty()) {
		        	date.setForeground(Color.GRAY);
		        	date.setText("2019-03-07");
		        }
		    }
		 });
		
		membername.addFocusListener(new FocusListener() {
		    @Override
		    public void focusGained(FocusEvent e) {
		        if (membername.getText().equals("Justin.Akdogan")) {
		        	membername.setText("");
		        	membername.setForeground(Color.BLACK);
		        }
		    }
		    @Override
		    public void focusLost(FocusEvent e) {
		        if (membername.getText().isEmpty()) {
		        	membername.setForeground(Color.GRAY);
		        	membername.setText("Justin.Akdogan");
		        }
		    }
		 });
		
		projectid.addFocusListener(new FocusListener() {
		    @Override
		    public void focusGained(FocusEvent e) {
		        if (projectid.getText().equals("DL1800274")) {
		        	projectid.setText("");
		        	projectid.setForeground(Color.BLACK);
		        }
		    }
		    @Override
		    public void focusLost(FocusEvent e) {
		        if (projectid.getText().isEmpty()) {
		        	projectid.setForeground(Color.GRAY);
		        	projectid.setText("DL1800274");
		        }
		    }
		 });
		
		start.addFocusListener(new FocusListener() {
		    @Override
		    public void focusGained(FocusEvent e) {
		        if (start.getText().equals("08:30")) {
		        	start.setText("");
		        	start.setForeground(Color.BLACK);
		        }
		    }
		    @Override
		    public void focusLost(FocusEvent e) {
		        if (start.getText().isEmpty()) {
		        	start.setForeground(Color.GRAY);
		        	start.setText("08:30");
		        }
		    }
		 });
		
		end.addFocusListener(new FocusListener() {
		    @Override
		    public void focusGained(FocusEvent e) {
		        if (end.getText().equals("11:30")) {
		        	end.setText("");
		        	end.setForeground(Color.BLACK);
		        }
		    }
		    @Override
		    public void focusLost(FocusEvent e) {
		        if (end.getText().isEmpty()) {
		        	end.setForeground(Color.GRAY);
		        	end.setText("11:30");
		        }
		    }
		 });
		
		pause.addFocusListener(new FocusListener() {
		    @Override
		    public void focusGained(FocusEvent e) {
		        if (pause.getText().equals("0.33")) {
		        	pause.setText("");
		        	pause.setForeground(Color.BLACK);
		        }
		    }
		    @Override
		    public void focusLost(FocusEvent e) {
		        if (pause.getText().isEmpty()) {
		        	pause.setForeground(Color.GRAY);
		        	pause.setText("0.33");
		        }
		    }
		 });
		
		description.addFocusListener(new FocusListener() {
		    @Override
		    public void focusGained(FocusEvent e) {
		        if (description.getText().equals("Merge Einweisung")) {
		        	description.setText("");
		        	description.setForeground(Color.BLACK);
		        }
		    }
		    @Override
		    public void focusLost(FocusEvent e) {
		        if (description.getText().isEmpty()) {
		        	description.setForeground(Color.GRAY);
		        	description.setText("Merge Einweisung");
		        }
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
	   private String [] getInput() {
		   String fields [] = new String [8];
		   if (database.getAuthorizationLevel() > 0) {
			   fields[0] = date.getText();
			   fields[1] = membername.getText();
			   fields[2] = projectid.getText();
			   fields[3] = start.getText();
			   fields[4] = end.getText();
			   fields[5] = pause.getText();
			   fields[6] = description.getText();
			   fields[7] = ""+showAllTimeRegistrations;
		   }else {
			   fields[0] = date.getText();
			   fields[1] = "";
			   fields[2] = projectid.getText();
			   fields[3] = start.getText();
			   fields[4] = end.getText();
			   fields[5] = pause.getText();
			   fields[6] = description.getText();
		   }
		   return fields;
	   }
	   
	   private void changeSetupToAuthorizationLevel() {
		   if (database.getAuthorizationLevel() < 1) {
			   	membername.setVisible(false);
			   	showOwnTimeRegistrations.setVisible(false);
				date.setBounds(50,100,80,32);
				projectid.setBounds(140,100,70,32);
				start.setBounds(220,100,40,32);
				end.setBounds(270,100,40,32);
				pause.setBounds(320,100,40,32);
				description.setBounds(50,150,390,32);
		   }
	   }
	   
	   private String [] getColumnNamesForCurrentUser() {
		   if (database.getAuthorizationLevel() < 1) {
			  String [] columnNames = {"Date", "Project", "Start", "End", "Pause", "Description" };
			  return columnNames;
		   }else {
			   String [] columnNames = {"Date","Worker","Projekt", "Start", "End", "Pause", "Description" };
				return columnNames;
		   }
	   }
}
