package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	JButton closeBtn, minimizeBtn, startSearch;
	JLabel title,border,filter_title,timeRegistration_title;
	JTextField date,membername,projectid,start,end,pause,description;
	JPanel jp = new JPanel(null);
	Database database = new Database();
	General general = new General();
	private java.awt.Point initialClick;
	
	Object[][] data = database.fetchAllTimeRegistrationEntries();
	
	String [] columnNames = {"Datum", "Mitarbeiter", "Projekt", "Start", "Ende", "Pause", "Beschreibung" };
	
	DefaultTableModel model = new DefaultTableModel(data, columnNames);
	
	
	public TimeRegistrationsFrame() {
		
		JTable table = new JTable(model);
		
		
		JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		//Layout
		setSize(width, height);
		setLocationRelativeTo(null);
		setUndecorated(true);
		setVisible(true);
		
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
		
	
		title = new JLabel("Mitarbeiter Spionagetool");
		title.setBounds(general.centerObject(250),0,250,32);
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
		
		membername = new JTextField("Justin.Akdogan");
		membername.setBounds(140,100,120,32);
		
		projectid = new JTextField("DL1800274");
		projectid.setBounds(270,100,70,32);
		
		start = new JTextField("08:30");
		start.setBounds(350,100,40,32);
		
		end = new JTextField("11:30");
		end.setBounds(400,100,40,32);
		
		pause = new JTextField("0.33");
		pause.setBounds(450,100,40,32);
		
		description = new JTextField("Merge Einweisung");
		description.setBounds(50,150,390,32);
		
		startSearch = new JButton();
		startSearch.setBounds(455, 150, 32, 32);
		startSearch.setBorderPainted(false);
		startSearch.setBorder(null);
		startSearch.setContentAreaFilled(false);
		startSearch.setIcon(new ImageIcon(getClass().getResource("/search.png")));
		
		timeRegistration_title = new JLabel("Zeiterfassungen");
		timeRegistration_title.setBounds(general.centerObject(150),210,150,32);
		timeRegistration_title.setFont(new Font("Calibri",Font.PLAIN,22));
		timeRegistration_title.setForeground(Color.WHITE);
		
		table.disable();
		scrollPane.setBounds(general.centerObject(500), 250, 500, 500);
	
		
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
		   String fields [] = new String [7];
		   fields[0] = date.getText();
		   fields[1] = membername.getText();
		   fields[2] = projectid.getText();
		   fields[3] = start.getText();
		   fields[4] = end.getText();
		   fields[5] = pause.getText();
		   fields[6] = description.getText();
		   return fields;
	   }
}
