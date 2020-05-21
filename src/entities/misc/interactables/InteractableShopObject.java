package entities.misc.interactables;

import java.awt.Color;

import entities.misc.Particle;
import entities.player.Player;
import shops.Shop;
import world.Camera;

public class InteractableShopObject extends InteractableObject {
	private String type;
	
	public InteractableShopObject(String type, float x, float y) {
		super(x,y);
		this.type = type;
	}
	
	@Override
	public void use(Player player) {
		this.getRegion().addParticles(Particle.Type.SPARKLES, Color.CYAN, 10, 0.5f, getX(), getY(), getWidth(), getHeight());
		Shop.openShop(this.type,player);
	}
	
	@Override
	public void draw(Camera c) {
		super.draw(c);
	}
}
