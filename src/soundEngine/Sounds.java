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
		sounds.put("gun_shot", new Sound("assets/sounds/gunfire.wav",100));
		sounds.put("gun_click", new Sound("assets/sounds/gunclick.wav",60));
		sounds.put("coffin", new Sound("assets/sounds/coffin.wav",100));
		sounds.put("not_today", new Sound("assets/sounds/not_today.wav",100));
		for (Sound sound : sounds.values()) {
			sound.mute();
			sound.play();
			sound.unmute();
		}
	}
	
	public static Sound get(String key) {
		return sounds.get(key);
	}
}
