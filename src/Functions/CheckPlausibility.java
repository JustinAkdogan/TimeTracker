package Functions;

import java.time.Duration;
import java.time.LocalTime;

import javax.swing.JOptionPane;

public class CheckPlausibility {

	public boolean checkIfStartTimeIsBeforeLastEndTime(String startTimeStr, String endTimeStr) {
		if (startTimeStr.length() > 0 && endTimeStr.length() > 0) {
			LocalTime startTime = LocalTime.of(splitStringIntoHoursAndMinutes(startTimeStr)[0], splitStringIntoHoursAndMinutes(startTimeStr)[1]);	
			LocalTime endTime = LocalTime.of(splitStringIntoHoursAndMinutes(endTimeStr)[0], splitStringIntoHoursAndMinutes(endTimeStr)[1]);	
			if (Duration.between(startTime, endTime).toMillis() > 0) {
				return false;
			}else {
				JOptionPane.showMessageDialog(null, "Please change your Start Time", "Start Time is before End Time", JOptionPane.OK_CANCEL_OPTION);
			}
		}
		return true;
	}
	
	public int [] splitStringIntoHoursAndMinutes(String timeString) {
		
		String hours = timeString.substring(0, timeString.indexOf(":"));
		String minutes = "";
		int [] time = new int [2];
		if (countSeparators(timeString) > 1) {
			minutes = timeString.substring(timeString.indexOf(":")+1,  timeString.lastIndexOf(":"));
		}else {
			minutes = timeString.substring(timeString.indexOf(":")+1,  timeString.length());
		}
		time[0] = Integer.parseInt(hours);
		time[1] = Integer.parseInt(minutes);
		
		return time;
	}
	
	private int countSeparators(String timeString) {
		int counter = 0;
		for (int i=0; i < timeString.length(); i++) {
			if (timeString.substring(i, i+1).contains(":")) {
				counter++;
			}
		}
		return counter;
	}
	

}
