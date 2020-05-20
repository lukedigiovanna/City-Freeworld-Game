package main;

import json.JSONFile;

public class Settings {
	
	private static JSONFile json;
	private final static String filePath = "assets/settings.json";
	
	/**
	 * Reads from the settings file to initialize some settings
	 */
	public static void initialize() {
		json = new JSONFile(filePath);	
	}
	
	public static Object getSetting(String settingName) {
		return json.get(settingName);
	}
	
	public static String getString(String settingName) {
		return (String)getSetting(settingName);
	}
	
	public static char getChar(String settingName) {
		return getString(settingName).charAt(0);
	}
	
	public static float getFloat(String settingName) {
		return Float.parseFloat(getString(settingName));
	}
	
	public static boolean getBoolean(String settingName) {
		return Boolean.parseBoolean(getString(settingName));
	}
	
	/**
	 * Sets the runtime setting of a thing
	 * @param settingName the name of the setting
	 * @param value the value you want the setting to be set at
	 * @return true if it changed the setting, false if the setting doesn't exist
	 */
	public static void setSetting(String settingName, String value) {
		json.set(settingName, value);
		json.save();
	}
}
