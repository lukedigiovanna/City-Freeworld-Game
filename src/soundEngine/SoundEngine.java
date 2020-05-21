package soundEngine;

import java.util.ArrayList;
import java.util.List;

import entities.Entity;
import entities.player.Player;
import main.Settings;
import world.World;
import world.WorldObject;

/**
 * Handles playing sounds relative to a player in the world
 * @author DGA100
 *
 */
public class SoundEngine {
	
	private boolean muted = false;
	
	private List<WorldSound> sounds;
	
	public SoundEngine() {
		sounds = new ArrayList<WorldSound>();
	}
	
	public void playSound(Sound sound, Entity pointOfOrigin) {
		if (muted)
			return;
		sound = sound.copy(); //make sure we create another sound object
		if (Settings.getBoolean("muted"))
			sound.mute();
		sound.play();
		sounds.add(new WorldSound(sound,pointOfOrigin));
	}
	
	public void playSound(String sound, Entity pointOfOrigin) {
		this.playSound(Sounds.get(sound), pointOfOrigin);
	}
	
	public void update() {
		for (int i = 0; i < this.sounds.size(); i++) {
			WorldSound sound = sounds.get(i);
			sound.update(); //updates the volume based on object movements
			if (sound.isDead()) {
				sound.getSound().pause();
				this.sounds.remove(sound);
				i--;
			}
		}
		//check if the game is muted
		if (Settings.getBoolean("muted")) {
			if (!this.muted)
				this.mute();
		}
	}
	
	/*
	 * These methods stop the clip and it can be resumed later
	 */
	public void pause() {
		for (WorldSound sound : sounds) {
			sound.getSound().pause();
		}
	}
	
	public void unpause() {
		for (WorldSound sound : sounds) {
			sound.getSound().play();
		}
	}
	
	/*
	 * These methods only affect the volume of the sound
	 * and allows the clip to continue playing
	 */
	public void mute() {
		for (WorldSound sound : sounds)
			sound.getSound().mute();
	}
	
	public void unmute() {
		for (WorldSound sound : sounds) 
			sound.getSound().unmute();
	}
}
