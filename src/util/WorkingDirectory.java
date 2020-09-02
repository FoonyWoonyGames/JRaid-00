package util;

public class WorkingDirectory {

	public static String getWorkingDirectory() {
		String dir;
		String OS = (System.getProperty("os.name").toUpperCase());
		
		if(OS.contains("WIN")) {
			dir = System.getenv("AppData");
		} else {
			dir = System.getProperty("user.home");

		    dir += "/Library/Application Support";
		}
		return dir + "/Foony Woony Games/Raid";
	}
}
