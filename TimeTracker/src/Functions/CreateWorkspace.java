package Functions;

import java.io.File;
import java.io.IOException;

public class CreateWorkspace {
	
	Settings settings = new Settings();
	
	final String programPath = "C:\\Zeiterfassung";
	
	public CreateWorkspace() {
		
		//settings.readAndGetSettings();
		
		File settings_file = new File(programPath + "\\Settings.ini");
		File sys_folder = new File(programPath);
		
		if(!sys_folder.exists()) {
			sys_folder.mkdir();
		}
		
		if(!settings_file.exists()) {
			try {
				settings_file.createNewFile();
				settings.setStandardSettings();
			} catch (IOException e) {
				
			}
		}else if (settings_file.length() < 1) {
			settings.setStandardSettings();
		}
	}
}
