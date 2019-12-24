package soundEngine;

import java.util.ArrayList;
import java.util.List;

/**
 * For sound effects and stuff game-wise
 * Does not include music sounds.
 */
public class SoundManager {
	private float masterVolume = 0.0f; //the maximum volume a sound should be played at (0 is max)
	private List<Sound> sounds;
	
	public SoundManager() {
		sounds = new ArrayList<Sound>();
	}
	
	public void pauseAll() {
		
	}
	
	public void muteAll() {
		
	}
	
	/**
	 * Sets the general volume for sounds.
	 * @param value
	 */
	public void setVolume(float value) {
		
	}
}
