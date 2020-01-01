package main;

import java.io.*;

public class Settings {
	
	private static String[] defaultSettings = {
		//these are the default settings regardless of what the file says	
		"max_fps:60",
		"vsync_enabled:false",
		"music_volume:1.0",
		"sound_volume:1.0",
		"master_volume:1.0",
		"screen_brightness:0.5",
		//control settings
		"move_up:w",
		"move_left:a",
		"move_right:d",
		"move_down:s",
		"open_inventory:e"
	};
	
	private static String[] runtimeSettings;
	
	private static String filePath = "assets/settings.txt";
	
	/**
	 * Reads from the settings file to initialize some settings
	 */
	public static void initialize() {
		//this initialized the runtime settings array.. these settings aren't actually used
		runtimeSettings = new String[defaultSettings.length];
		for (int i = 0; i < runtimeSettings.length; i++) {
			runtimeSettings[i] = defaultSettings[i];
		}
		
		setSettingsFromFile();
	}
	
	public static String getSetting(String settingName) {
		for (String s : runtimeSettings) {
			if (s.substring(0,s.indexOf(":")).contentEquals(settingName)) {
				return s.substring(s.indexOf(":")+1);
			}
		}
		return null; //if that setting does not exist
	}
	
	public static void setSettingsFromFile() {
		try {
			File file = new File(filePath);
			BufferedReader in = new BufferedReader(new FileReader(file));
			
			String s;
			int index = 0;
			while ((s = in.readLine()) != null) { //read every line
				runtimeSettings[index] = s;
				index++;
			}
			
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets the runtime setting of a thing
	 * @param settingName the name of the setting
	 * @param value the value you want the setting to be set at
	 * @return true if it changed the setting, false if the setting doesn't exist
	 */
	public static boolean setSetting(String settingName, String value) {
		//open up a stream to our settings file.. 
		try {
			File file = new File(filePath);
			BufferedReader in = new BufferedReader(new FileReader(file));
			//find the setting name
			String st;
			int index = 0;
			while ((st = in.readLine()) != null) {
				String sName = st.substring(0,st.indexOf(":"));
				if (sName.contentEquals(settingName)) { //set the value
					runtimeSettings[index] = settingName+":"+value;
					//update our settings file
					updateSettingsFile();
					return true; //we found the 
				}
				index++;
			}
			in.close();
			//if we can't find the setting name then return false 
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	/**
	 * Sets the settings file to be the settings determined during runtime.
	 * For example: if a user changes the volume setting we want to update
	 * that to the settings file so it changes permanently across game executions.
	 */
	public static void updateSettingsFile() {
		// open up a print writer to the settings file
		try {
			PrintWriter pw = new PrintWriter(filePath,"UTF-8");
			for (String s : runtimeSettings) 
				pw.println(s);
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Clears up the settings file and writes the default
	 * settings onto it. This method is for developer use only
	 */
	public static void setFileToDefaults() {
		// open up a print writer to the settings file
		try {
			PrintWriter pw = new PrintWriter(filePath,"UTF-8");
			for (String s : defaultSettings) 
				pw.println(s);
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
