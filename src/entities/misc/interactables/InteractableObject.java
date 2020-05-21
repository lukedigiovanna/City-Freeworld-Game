package entities.misc.interactables;

import java.awt.Color;

import entities.Entity;
import entities.player.Player;
import game.Game;
import shops.Shop;
import world.Camera;
import world.Properties;

/*
 * Allows the player to interact with this object
 * Players will search for interactables
 * Opens a UI depending on the type
 */
public abstract class InteractableObject extends Entity {	
	
	public InteractableObject(float x, float y) {
		super(x,y,0.0f,0.0f);
	
		this.addTag("interactable");
		
		//set properties
		this.setProperty(Properties.KEY_HAS_COLLISION, Properties.VALUE_HAS_COLLISION_FALSE);
		this.setProperty(Properties.KEY_INVULNERABLE, Properties.VALUE_INVULNERABLE_TRUE);
		
	}
	
	public abstract void use(Player player); //what happens when the player tries to interact with this
	
	private boolean test = false;
	public void update(float dt) {
		if (!test) {
			this.playSound("coffin");
			test = true;
		}
	}
	
	public void draw(Camera c) {
		
	}
}
