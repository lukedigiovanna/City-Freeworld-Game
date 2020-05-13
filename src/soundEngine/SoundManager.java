package soundEngine;

import java.util.ArrayList;
import java.util.List;

import main.Settings;

/**
 * For sound effects and stuff game-wise
 * Does not include music sounds.
 */
public class SoundManager {
	private static float masterVolume = 0.0f; //the maximum volume a sound should be played at (0 is max)
	private static List<Sound> sounds;
	
	public static void initialize() {
		sounds = new ArrayList<Sound>();
	}
	
	private static int playVolume = 50; //on a scale of 0 to 50 (50 is the loudest)
	public static void setPlayVolume(int volume) {
		playVolume = volume;
	}
	
	public static void play(Sound sound) {
		if ((boolean)Settings.getSetting("muted"))
			return;
		if (sound == null)
			return;
		sound = sound.copy();
		sound.play();
		sounds.add(sound);
	}
	
	public static void play(String soundName) {
		play(Sounds.get(soundName));
	}
	
	public static void pauseAll() {
		for (Sound s : sounds)
			s.pause();
	}
	
	public static void unpauseAll() {
		for (Sound s : sounds)
			s.play();
	}
	
	public static void update() {
		for (int i = 0; i < sounds.size(); i++) {
			if (sounds.get(i).dead()) {
				sounds.remove(i);
				i--;
			}
		}
	}
	
	public static void muteAll() {
		setVolume(Sound.MIN_VOLUME-1f);
	}
	
	/**
	 * Sets the general volume for sounds.
	 * @param value
	 */
	public static void setVolume(float value) {
		for (Sound s : sounds)
			s.setVolume(value);
	}
}
