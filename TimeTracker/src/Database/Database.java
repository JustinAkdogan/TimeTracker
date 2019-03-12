package Database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JOptionPane;

import Functions.General;
import Functions.Settings;

public class Database {
	
	Connection con;
	General general = new General();
	Settings settings = new Settings();
	final String userid = System.getProperty("user.name");
	
	//#TODO Probleme mit öffnen aller Zeiterfassungen
	public Database() {
		if (checkConnection()) {
			String [] sqlConnection = settings.readAndGetSettings(true);
			try {
				this.con = DriverManager.getConnection("jdbc:"+ sqlConnection[1] +"://"+ sqlConnection[2] +"/"+ sqlConnection[3],sqlConnection[4],sqlConnection[5]);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public boolean checkConnection() {
		final String CHECK_SQL_QUERY = "SELECT 1";
		try {
			Class.forName("com.mysql.jdbc.Driver");	
			String [] sqlConnection = settings.readAndGetSettings(true);
			this.con = DriverManager.getConnection("jdbc:"+ sqlConnection[1] +"://"+ sqlConnection[2] +"/"+ sqlConnection[3],sqlConnection[4],sqlConnection[5]);
			try {
				final PreparedStatement statement = con.prepareStatement(CHECK_SQL_QUERY);
				return true;
			}catch(SQLException | NullPointerException e) {
				
			}
		}catch(Exception ex) {
			System.out.println(ex);
		}
		return false;
	}
		
	//This function is for the Bar chart to fill them with information
	public float [] fetchAllTimeRegistrationsFromTheWeek() {
		Statement st;
		ResultSet rs;
		float dayInMillis = 86400000;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK)-2; //#TODO
		int dayOfWeekPlus1 = dayOfWeek;
		dayOfWeekPlus1++;
		float [] weekTimes = new float [5];
		Time savedStartTime = null;
		Time savedEndTime = null;
		try {
			for (int i = 0; i < dayOfWeekPlus1; i++) {	
				st = con.createStatement();
				rs = st.executeQuery("SELECT * FROM zeiterfassungen WHERE date='" + sdf.format(System.currentTimeMillis()-((dayOfWeek - i) * (dayInMillis)))+"' AND userid='"+ userid +"'");
				while(rs.next()) {
					if (rs.isFirst()) {
						savedStartTime = rs.getTime("start");
					}
					if (rs.isLast()) {
						savedEndTime = rs.getTime("end");
					}
				}
				if (savedStartTime != null && savedEndTime != null) {
					weekTimes[i] = general.calculateHoursForBarChart(savedStartTime,savedEndTime);
				}
					
			}
			return weekTimes;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
		
	//This function gets the last Endtime from today to preassign the next Starttime in the table
	public String fetchLastEndTimeFromToday() {
		Statement st;
		ResultSet rs;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			st = con.createStatement();
			rs = st.executeQuery("SELECT * FROM zeiterfassungen WHERE date='" + sdf.format(System.currentTimeMillis())+"' AND userid='"+ userid +"'");
			String lastEndTime = "";
			while(rs.next()) {
				lastEndTime = rs.getString("end").substring(0, 5);
			}
			return lastEndTime;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//This function get all Time registrations of tdoay from the current user to fill the table with information
	public String [] [] fetchAllTimeRegistrationsFromToday() {
		Statement st;
		ResultSet rs;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			st = con.createStatement();
			rs = st.executeQuery("SELECT * FROM zeiterfassungen WHERE date='" + sdf.format(System.currentTimeMillis())+"' AND userid='"+ userid +"'");
			String records [][] = new String [20][5]; //#TODO 
			int counter = 1;
			records[0][0] = "Projekt"; //#TODO
			records[0][1] = "Startzeit";
			records[0][2] = "Endzeit";
			records[0][3] = "Pause";
			records[0][4] = "Beschreibung";
			while(rs.next()) {
				records[counter][0] = rs.getString("projectid");
				records[counter][1] = rs.getString("start");
				records[counter][2] = rs.getString("end");
				records[counter][3] = rs.getString("pause");
				records[counter][4] = rs.getString("description");
				counter++;
			}
			return records;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
		
	//This function checks if the current user is new to the system
	public boolean checkIfUserExists() {
		Statement st;
		ResultSet rs;
		try {
			st = con.createStatement();
			rs = st.executeQuery("SELECT * FROM mitarbeiter WHERE computername='"+ userid +"'");
			if (!rs.next()) {
//				createNewUser();
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	//This function creates a new User
	public void createNewUser(String memberData []) {
		PreparedStatement st;
		ResultSet rs;
		memberData[2] = ""+general.checkAuthorizationKey(memberData[2]);
	     try {	
			String query = "INSERT INTO mitarbeiter (forname,lastname,computername,authorization) VALUES (?,?,?,?)";
			st = con.prepareStatement(query);
			st.setString(1, memberData[0]);
			st.setString(2, memberData[1]);
			st.setString(3, userid);
			st.setString(4, memberData[2]);
			int success = st.executeUpdate();
			if (success > 0) {
				JOptionPane.showMessageDialog(null, "You're now registered as a User. Please Restart!", "Registration succeed", JOptionPane.PLAIN_MESSAGE);
				System.exit(1);
			}else {
				JOptionPane.showMessageDialog(null, "Please contact your Systemadministrator", "Registration failed", JOptionPane.PLAIN_MESSAGE);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//This function gets the fullname of the current user
	public String [] fetchForAndLastname(String UserID) {
		Statement st;
		ResultSet rs;
		String [] memberName= new String [2];
		try {
			st = con.createStatement();
			rs = st.executeQuery("SELECT * FROM mitarbeiter WHERE computername='"+ UserID +"'");
			if (!rs.next()) {
				//createNewUser(); #TODO
			}
			if (rs.first()) {
				memberName[0] = rs.getString("forname");
				memberName[1] =	rs.getString("lastname");
			}
			return memberName;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return memberName;
	}
	
	//This function gets the AuthorizationLevel from the current user
	public int getAuthorizationLevel() {
		Statement st;
		ResultSet rs;
		int authorizationLevel = 0;
		try {
			st = con.createStatement();
			rs = st.executeQuery("SELECT * FROM mitarbeiter WHERE computername='"+ userid +"'");
			if (rs.first()) {
				authorizationLevel = rs.getInt("authorization");
			}
			return authorizationLevel;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	//This function gets all Time Registrations ordered by date
	public String [][] fetchAllTimeRegistrationEntries(){
		Statement st;
		ResultSet rs;
		try {
			st = con.createStatement();
			rs = st.executeQuery("SELECT * FROM zeiterfassungen ORDER BY date DESC");
			int counter = 0;
			int datasets = 0;
			String records [][] = new String [countAllTimeRegistrationDatasets()][7];
			while(rs.next()) {
				String fullname = fetchForAndLastname(rs.getString("userid"))[0]+ " " + fetchForAndLastname(rs.getString("userid"))[1];
				records[counter][0] = rs.getString("date");
				records[counter][1] = fullname; //fetchForAndLastname(rs.getString("userid"));
				records[counter][2] = rs.getString("projectid");
				records[counter][3] = rs.getString("start");
				records[counter][4] = rs.getString("end");
				records[counter][5] = rs.getString("pause");
				records[counter][6] = rs.getString("description");
				counter++;
			}
			return records;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//This function count all Time Registration Datasets to create the array in the function above
	public int countAllTimeRegistrationDatasets() {
		Statement st;
		ResultSet rs;
		try {
			st = con.createStatement();
			rs = st.executeQuery("SELECT * FROM zeiterfassungen");
			int counter = 0;
			while(rs.next()) {
				counter++;
			}
			return counter;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	//This function edit a existing dataset if the table data has changed
	public void changeTimeRegistrationDataset(String fields [], int changedColumn) {
		PreparedStatement st;
		if (fields[3] != null) {
			fields[3] = fields[3].replaceAll(",", ".");
		}
		try {
			String query = "UPDATE zeiterfassungen SET projectid=?,start=?,end=?,pause=?,description=? WHERE ";
			if (changedColumn != 0) { query += "projectid='" + fields[0] + "' AND ";}
			if (changedColumn != 1) { query += "start='" + fields[1] + "' AND ";}
			if (changedColumn != 2) { query += "end='" + fields[2] + "' AND ";}
			if (changedColumn != 3) { 
				if (changedColumn != 4) {
					query += "pause='" + fields[3] + "' AND ";
				}else {
					query += "pause='" + fields[3] + "'";
				}
			}
			if (changedColumn != 4) { query += "description='" + fields[4] + "'";}
			st = con.prepareStatement(query);
			st.setString(1, fields[0]);
			st.setString(2, fields[1]);
			st.setString(3, fields[2]);
			st.setString(4, fields[3]);
			st.setString(5, fields[4]);
			int success = st.executeUpdate();
			if (success <= 0) {
				createNewTimeRegistrationDataset(fields);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//This function creates a new Dataset
	public void createNewTimeRegistrationDataset(String fields []) {
		PreparedStatement st;
		ResultSet rs;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (fields[3] == null) {
			fields[3] = "0";
		}
	     try {	
			String query = "INSERT INTO zeiterfassungen (userid,projectid,start,end,pause,description,date) VALUES (?,?,?,?,?,?,?)";
			st = con.prepareStatement(query);
			st.setString(1, userid);
			st.setString(2, fields[0]);
			st.setString(3, fields[1]);
			st.setString(4, fields[2]);
			st.setString(5, fields[3]);
			st.setString(6, fields[4]);
			st.setString(7, sdf.format(System.currentTimeMillis()));
			st.execute();		
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String [][] filterTimeRegistrationDatasets(String fields[]) {
		Statement st;
		ResultSet rs;
		try {
			st = con.createStatement();
			String query = "SELECT * FROM zeiterfassungen WHERE ";
			if (!fields[0].isEmpty()) {
				query += "date='" + fields[0] + "' AND ";
			}
			if (!fields[1].isEmpty()) {
				query += "userid='" + fields[1] + "' AND ";
			}
			if (!fields[2].isEmpty()) {
				query += "projectid='" + fields[2] + "' AND ";
			}
			if (!fields[3].isEmpty()) {
				query += "start='" + fields[3] + "' AND ";
			}
			if (!fields[4].isEmpty()) {
				query += "end='" + fields[4] + "' AND ";
			}
			if (!fields[5].isEmpty()) {
				query += "pause='" + fields[5] + "' AND ";
			}
			if (!fields[6].isEmpty()) {
				query += "description='" + fields[6] + "'";
			}
			if (fields[6].isEmpty() || fields[5].isEmpty() || fields[4].isEmpty() || fields[3].isEmpty() || fields[2].isEmpty() || fields[1].isEmpty() || fields[0].isEmpty()) {
				if (query.substring(query.length()-4, query.length()).contains("AND")) {
					query = query.substring(0, query.length()-4);
				}
			}
			rs = st.executeQuery(query);
			int datasets = 0;
			String records [][] = new String [countFilteredTimeRegistrationDatasets(query)][7]; //#TODO
			int rowCounter = 0;
			while(rs.next()) {
				String fullname = fetchForAndLastname(rs.getString("userid"))[0]+ " " + fetchForAndLastname(rs.getString("userid"))[1];
				records[rowCounter][0] = rs.getString("date");
				records[rowCounter][1] = fullname;//fetchForAndLastname(rs.getString("userid"))[0]+ " " + fetchForAndLastname(rs.getString("userid"))[1];
				records[rowCounter][2] = rs.getString("projectid");
				records[rowCounter][3] = rs.getString("start");
				records[rowCounter][4] = rs.getString("end");
				records[rowCounter][5] = rs.getString("pause");
				records[rowCounter][6] = rs.getString("description");
				rowCounter++;
			}
			return records;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	private int countFilteredTimeRegistrationDatasets(String query) {
		Statement st;
		ResultSet rs;
		try {
			st = con.createStatement();
			rs = st.executeQuery(query);
			int datasets = 0;
			while(rs.next()) {
				datasets++;
			}
			return datasets;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
		
	}
	
	public String [][] fetchTimeRegistrationsFromSelectedDay(int days) {
		Statement st;
		ResultSet rs;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		float dayInMillis = 86400000;
		try {
			st = con.createStatement();
			String query = "SELECT * FROM zeiterfassungen WHERE userid='" + userid + "' AND date=' " + sdf.format(System.currentTimeMillis()-((days) * (dayInMillis))) + "'";
			String tableData [][] = new String [20][6];
			rs = st.executeQuery(query);
			
			int counter = 0;
			while(rs.next()) {
//				if (counter == 0) {
//					//tableData[0][0] = setColumnNames(); 
//				}else {
					tableData[0][0] = rs.getString("projectid");
					tableData[0][1] = rs.getString("start");
					tableData[0][2] = rs.getString("end");
					tableData[0][3] = rs.getString("pause");
					tableData[0][4] = rs.getString("description");
					counter++;
				//}
			}
			return tableData;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String [] setColumnNames() {
		String columnNames [] = {"Projekt","Startzeit","Endzeit","Pause","Beschreibung"}; 
		return columnNames;
	}
	
	public void updateUserData(String fields []) {
		PreparedStatement st;
		try {
			String query;
			fields[2] = ""+general.checkAuthorizationKey(fields[2]);
			if (fields[2].contains("0")) {
				fields[2] = ""+getAuthorizationLevel();
			}
			query = "UPDATE mitarbeiter SET forname=?,lastname=?,authorization=? WHERE computername='"+ userid + "'";
			st = con.prepareStatement(query);
			st.setString(1, fields[0]);
			st.setString(2, fields[1]);
			st.setString(3, fields[2]);
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
//	public void fetchHoursOfTheMonth() {
//		Statement st;
//		ResultSet rs;
//		float dayInMillis = (float) 86400000;
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
//		String userid = System.getProperty("user.name");
//		Calendar c = Calendar.getInstance();
//		c.setTime(new Date());
//		int dayOfMonth = c.get(Calendar.DAY_OF_MONTH); 
//		int dayOfMonthMinus1 = dayOfMonth--;
//		float [] monthTimes = new float [30];
//		Time savedStartTime = null;
//		Time savedEndTime = null;
//		try {
//			for (int i = 0; i < 30; i++) {
//				st = con.createStatement();
//				rs = st.executeQuery("SELECT * FROM zeiterfassungen WHERE date='" + sdf.format(System.currentTimeMillis()-((dayOfMonthMinus1 - i) * (dayInMillis)))+"' AND userid='"+ userid +"'");
//				String lastEndTime = "";
//				while(rs.next()) {
//					if (rs.isFirst()) {
//						savedStartTime = rs.getTime("start");
//					}
//					if (rs.isLast()) {
//						savedEndTime = rs.getTime("end");
//					}
//				}
//				if (savedStartTime != null && savedEndTime != null) {
//					monthTimes[i] = getTotalHours(savedStartTime,savedEndTime);
//					System.out.println(monthTimes[i]);
//				}	
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
//		

}

