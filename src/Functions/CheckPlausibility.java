package Functions;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalTime;

import javax.swing.JOptionPane;

public class CheckPlausibility {

	public boolean checkIfStartTimeIsBeforeLastEndTime(Object startTimeObj, Object endTimeObj) {
		LocalTime startTime = LocalTime.parse(""+startTimeObj);
		LocalTime endTime = LocalTime.parse(""+endTimeObj);
		if (Duration.between(startTime, endTime).toMillis() < 0) {
			JOptionPane.showMessageDialog(null, "Please change your Start Time", "Start Time is before End Time", JOptionPane.OK_CANCEL_OPTION);
			return false;
		}
		return true;
	}
	

}
