package soundEngine;

import entities.player.Player;
import world.World;
import world.WorldObject;

/**
 * Handles playing sounds relative to a player in the world
 * @author DGA100
 *
 */
public class SoundEngine {
	private Player player; //where the sound is being perceived.
	
	private boolean muted = false;
	
	public SoundEngine(Player player) {
		this.player = player;
	}
	
	public void playSound(Sound sound, WorldObject pointOfOrigin) {
		if (muted)
			return;
		SoundManager.play(sound);
	}
	
	public void playSound(String sound, WorldObject pointOfOrigin) {
		if (muted)
			return;
		SoundManager.play(sound);
	}
	
	public void mute() {
		this.muted = true;
	}
	
	public void unmute() {
		this.muted = false;
	}
}
