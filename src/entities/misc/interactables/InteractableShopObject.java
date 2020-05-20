package entities.misc.interactables;

import shops.Shop;
import world.Camera;

public class InteractableShopObject extends InteractableObject {
	private Shop.Type type;
	
	public InteractableShopObject(Shop.Type type, float x, float y) {
		super(x,y);
	}
	
	@Override
	public void use() {
		
	}
	
	@Override
	public void draw(Camera c) {
		super.draw(c);
	}
}
