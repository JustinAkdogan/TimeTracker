package GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Database.Database;
import Functions.General;
import Functions.Settings;

public class SettingsFrame extends JFrame {
	
	General general = new General();
	Settings settings = new Settings();
	
	final short width = 600;
	final short height = 400;
	private java.awt.Point initialClick;
	
	JButton closeBtn, minimizeBtn, testSQLConnection, saveSettings;
	JLabel title,border, connection_type_title,server_title, database_title, username_title, password_title,
	forname_title,lastname_title,authorizationkey_title,status;
	JTextField connection_type,server,database,username,password,forname,lastname;
	JPasswordField authorizationkey;
	JPanel jp = new JPanel();
	Database databaseConnection = new Database();
	String inputBackup [];
	
	String computername = System.getProperty("user.name");
	
	final byte iconWidth = 32, iconHeight = 32;
	
	public SettingsFrame() {
				
		//Layout
		setSize(width, height);
		setLocationRelativeTo(null);
		setUndecorated(true);
		setVisible(true);
		jp.setBackground(new java.awt.Color(224, 74, 74));	
		
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
		
		title = new JLabel("Setup");
		title.setBounds(general.centerObject(100),0,100,32);
		title.setFont(new Font("Calibri",Font.PLAIN,22));
		title.setForeground(Color.WHITE);
		
		border = new JLabel("_____________________________________________________________________________________________________________________________");
		border.setBounds(0,12,width+100,32);
		border.setHorizontalAlignment(JLabel.CENTER);
		border.setFont(new Font("Calibri",Font.PLAIN,22));
		border.setForeground(Color.WHITE);
		
		connection_type_title = new JLabel("Connection Type");
		connection_type_title.setBounds(50,110,120,32);
		connection_type_title.setFont(new Font("Calibri",Font.PLAIN,14));
		connection_type_title.setForeground(Color.WHITE);
		
		database_title = new JLabel("Database");
		database_title.setBounds(170,110,120,32);
		database_title.setFont(new Font("Calibri",Font.PLAIN,14));
		database_title.setForeground(Color.WHITE);
		
		username_title = new JLabel("Username");
		username_title.setBounds(290,110,120,32);
		username_title.setFont(new Font("Calibri",Font.PLAIN,14));
		username_title.setForeground(Color.WHITE);
		
		password_title = new JLabel("Password");
		password_title.setBounds(410,110,120,32);
		password_title.setFont(new Font("Calibri",Font.PLAIN,14));
		password_title.setForeground(Color.WHITE);
		
		server_title = new JLabel("Server");
		server_title.setBounds(50,50,120,32);
		server_title.setFont(new Font("Calibri",Font.PLAIN,14));
		server_title.setForeground(Color.WHITE);
		
		forname_title = new JLabel("Forname");
		forname_title.setBounds(50,230,120,32);
		forname_title.setFont(new Font("Calibri",Font.PLAIN,14));
		forname_title.setForeground(Color.WHITE);
		forname_title.setVisible(false);
		
		lastname_title = new JLabel("Lastname");
		lastname_title.setBounds(223,230,120,32);
		lastname_title.setFont(new Font("Calibri",Font.PLAIN,14));
		lastname_title.setForeground(Color.WHITE);
		lastname_title.setVisible(false);
		
		authorizationkey_title = new JLabel("Key (Optional)");
		authorizationkey_title.setBounds(396,230,120,32);
		authorizationkey_title.setFont(new Font("Calibri",Font.PLAIN,14));
		authorizationkey_title.setForeground(Color.WHITE);
		authorizationkey_title.setVisible(false);
		
		
		
		server = new JTextField("");
		server.setBounds(50,80,460,32);
		
		connection_type = new JTextField("");
		connection_type.setBounds(50,140,100,32);
				
		database = new JTextField("");
		database.setBounds(170,140,100,32);
		
		username = new JTextField("");
		username.setBounds(290,140,100,32);
		
		password = new JTextField("");
		password.setBounds(410,140,100,32);
		
		testSQLConnection = new JButton();
		testSQLConnection.setBounds(general.centerObject(32), 190, iconWidth, iconHeight);
		testSQLConnection.setBorderPainted(false);
		testSQLConnection.setBorder(null);
		testSQLConnection.setContentAreaFilled(false);
		testSQLConnection.setIcon(new ImageIcon(getClass().getResource("/search.png")));
		
		forname = new JTextField("");
		forname.setBounds(50,260,153,32);
		forname.setVisible(false);
		
		lastname = new JTextField("");
		lastname.setBounds(223,260,153,32);
		lastname.setVisible(false);
		
		authorizationkey = new JPasswordField();
		authorizationkey.setBounds(396,260,115,32);
		authorizationkey.setVisible(false);
		
		saveSettings = new JButton();
		saveSettings.setBounds(general.centerObject(32), 330, iconWidth, iconHeight);
		saveSettings.setBorderPainted(false);
		saveSettings.setBorder(null);
		saveSettings.setContentAreaFilled(false);
		saveSettings.setIcon(new ImageIcon(getClass().getResource("/save.png")));
		saveSettings.setVisible(false);
		
		add(minimizeBtn);
		add(closeBtn);
		add(title);
		add(border);
		add(connection_type_title);
		add(database_title);
		add(username_title);
		add(password_title);
		add(server_title);
		add(connection_type);
		add(server);
		add(database);
		add(username);
		add(password);
		add(testSQLConnection);
		add(forname);
		add(lastname);
		add(authorizationkey);
		add(forname_title);
		add(lastname_title);
		add(authorizationkey_title);
		add(saveSettings);
		add(jp);
		
		fillTextFields();
		showMemberFieldsIfIsConnectedToDatabase();
		
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
		
		testSQLConnection.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				onAfterValidateTestSQLConnection();
			}
		});
		
		saveSettings.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				onAfterValidateSaveSettings();
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
	
	private void onAfterValidateTestSQLConnection() {
		settings.changeSettings(getDatabaseTextFieldValues());
		if (showMemberFieldsIfIsConnectedToDatabase()) {
			JOptionPane.showMessageDialog(null, "Connection to database was succuessful.", "Test Successful", JOptionPane.OK_OPTION);
			general.restartApplication();
		}else {
			JOptionPane.showMessageDialog(null, "Connection to database failed.", "Test failed", JOptionPane.OK_OPTION);
		}
	}
	
	private void onAfterValidateSaveSettings() {
		if (!databaseConnection.checkIfUserExists()) {
			databaseConnection.createNewUser(getMemberTextFieldValues());
		}else {
			databaseConnection.updateUserData(getMemberTextFieldValues());
		}
	}
	
	private boolean showMemberFieldsIfIsConnectedToDatabase() {
		boolean isConnected = databaseConnection.checkConnection();
		forname.setVisible(isConnected);
		lastname.setVisible(isConnected);
		authorizationkey.setVisible(isConnected);
		forname_title.setVisible(isConnected);
		lastname_title.setVisible(isConnected);
		authorizationkey_title.setVisible(isConnected);
		saveSettings.setVisible(isConnected);
		if (isConnected) {
			String memberData [] = databaseConnection.fetchForAndLastname(computername);
			forname.setText(memberData[0]);
			lastname.setText(memberData[1]);
			if (databaseConnection.getAuthorizationLevel() > 0) {
				authorizationkey_title.setVisible(false);
				authorizationkey.setVisible(false);
			}
		}
		return isConnected;
	}
	
	private String [] getDatabaseTextFieldValues() {
		String field [] = new String [6];
		field[1] = connection_type.getText();
		field[2] = server.getText();
		field[3] = database.getText();
		field[4] = username.getText();
		field[5] = password.getText();
		return field;
	}
	
	private String [] getMemberTextFieldValues() {
		String field [] = new String [3];
		field[0] = forname.getText();
		field[1] = lastname.getText();
		field[2] = authorizationkey.getText();
		return field;
	}
	
	private void fillTextFields() {
		String [] settingLines = settings.readAndGetSettings(true);
		connection_type.setText(settingLines[1]);
		server.setText(settingLines[2]);
		database.setText(settingLines[3]);
		username.setText(settingLines[4]);
		password.setText(settingLines[5]);
	}
}
