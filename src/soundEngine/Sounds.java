package soundEngine;

import java.util.HashMap;

public class Sounds {
	
	/**
	 * Associates a string key to a sound file
	 */
	private static HashMap<String,Sound> sounds;
	
	/**
	 * Creates the sound files
	 */
	public static void initialize() {
		sounds = new HashMap<String,Sound>();
		sounds.put("gun_shot", new Sound("assets/sounds/gunfire.wav"));
		sounds.put("gun_click", new Sound("assets/sounds/gunclick.wav"));
	}
	
	public static Sound get(String key) {
		return sounds.get(key);
	}
}
