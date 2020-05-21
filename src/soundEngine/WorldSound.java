package soundEngine;

import entities.Entity;
import entities.player.Player;
import misc.MathUtils;

/**
 * Contains a reference to the source of the sound and can be updated
 * based on the movement of the object.
 */
public class WorldSound {
	private Sound sound; //reference to the sound file to be played
	
	private Entity source;
	
	public WorldSound(Sound sound, Entity source) {
		this.sound = sound;
		this.source = source;
	}
	
	public Sound getSound() {
		return this.sound;
	}
	
	public Entity getSource() {
		return this.source;
	}
	
	private boolean dead = false;
	public boolean isDead() {
		return this.dead;
	}
	
	/**
	 * Updates the volume of the sound based on the distance to the games player
	 */
	public void update() {
		if (this.source.isDestroyed()) {
			this.dead = true;
			return;
		}
		Player player = source.getPlayer();
		float distanceSquared = player.squaredDistanceTo(source);
		distanceSquared = (float)Math.sqrt(distanceSquared);
		//use the inverse square law to determine the strength of the sound
		float strength = MathUtils.min(1, 5 / distanceSquared);
		//now set the strength of the sound
		sound.setStrength(strength);
	}
}
