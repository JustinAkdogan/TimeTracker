package Functions;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JOptionPane;

public class General {
	
	public float getTotalHoursOfToday(float [] weekTimes) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK)-1;
		float hoursOfToday = 0;
		
		switch(dayOfWeek) {
			case 1: hoursOfToday =  weekTimes[0]; break;
			case 2: hoursOfToday =	weekTimes[1]; break;
			case 3: hoursOfToday =  weekTimes[2]; break;
			case 4: hoursOfToday = 	weekTimes[3]; break;
			case 5: hoursOfToday =  weekTimes[4]; break;
		}
		hoursOfToday = (float) (Math.round(hoursOfToday * 100)/100.0);
		return hoursOfToday;
	}
	
	public float getTotalHoursOfTheWeek(float [] weekTimes) {
		float weekHours = 0;
		for (short i=0; i < 5; i++) {
			weekHours += weekTimes[i];
		}
		weekHours = (float) (Math.round(weekHours * 100)/100.0);
		return weekHours;
	}
	
	public float getTotalHoursOfTheMonth(float [] monthTimes) {
		float monthHours = 0;
		for (short i=0; i < 30; i++) {
			monthHours += monthTimes[i];
		}
		monthHours = (float) (Math.round(monthHours * 100)/100.0);
		return monthHours;
	}
	
	public int centerObject(int objectWidth) {
		final short width = 600;
		   int position = (width - objectWidth) / 2;
		   return position;
	}
	
	//This function calculates the total hours of one day for the Bar chart
	public float calculateHoursForBarChart(Time savedStartTime, Time savedEndTime ) {
		int savedStartHours = savedStartTime.getHours() * (60 * 60);
		int savedStartMinutes = savedStartTime.getMinutes() * 60;
		int savedStartSeconds = savedStartHours + savedStartMinutes;
		
		int savedEndHours = savedEndTime.getHours() * (60 * 60);
		int savedEndMinutes = savedEndTime.getMinutes() * 60;
		int savedEndSeconds = savedEndHours + savedEndMinutes;
		
		float differenceInSeconds = savedEndSeconds - savedStartSeconds;	
		float differenceInHours = differenceInSeconds / 3600;
		
		return differenceInHours;
	}
	
	public int checkAuthorizationKey(String key) {
		if (key.contains("admin123")) {
			JOptionPane.showMessageDialog(null, "You're now registered as Administrator", "Account upgrade succeed", JOptionPane.PLAIN_MESSAGE);
			return 2;
		}else if (key.contains("verwaltung123")) {
			JOptionPane.showMessageDialog(null, "You're now registered as Manager", "Account upgrade succeed", JOptionPane.PLAIN_MESSAGE);
			return 1;
		}else if (!key.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Wrong authorization key!", "Account upgrade failed", JOptionPane.PLAIN_MESSAGE);
		}
		return 0;
	}


}
