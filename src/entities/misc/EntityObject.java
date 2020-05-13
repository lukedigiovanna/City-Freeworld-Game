/**
 * 
 */
package entities.misc;

import display.Animation;
import display.textures.*;
import entities.Entity;
import world.Camera;
import world.Properties;
import world.regions.Region;

public class EntityObject extends Entity {
	
	private transient Texture texture;
	private transient Animation animation;
	
	private int id;
	
	public EntityObject(int id, float x, float y, float r) {
		super(x,y);
		this.setRotation(r);
		this.id = id;
		this.texture = TexturePack.current().getObjectTexture(id);
		this.animation = this.texture.getAnimation().copy();
		this.setDimension(this.texture.getDimension().copy());
		this.setLightEmissionValue(texture.getLightEmissionValue());
		this.setVerticalHeight(texture.getVerticalHeight());
		//System.out.println(this.getVerticalHeight());
		this.setHitboxToDimension();
		this.setProperty(Properties.KEY_INVULNERABLE, Properties.VALUE_INVULNERABLE_TRUE);
		this.setProperty(Properties.KEY_REGENERATE_HITBOX, Properties.VALUE_REGENERATE_HITBOX_FALSE);
		//this.setProperty(Properties.KEY_HAS_RIGID_BODY, Properties.VALUE_HAS_RIGID_BODY_TRUE);
		this.addTag("entity_object");
	}
	
	public void loadAssets() {
		this.texture = TexturePack.current().getObjectTexture(id);
		this.animation = this.texture.getAnimation().copy();
		System.out.println("loaded!");
	}

	@Override
	public void draw(Camera camera) {
		if (animation == null)
			this.loadAssets(); //protective measure
		camera.drawImage(animation.getCurrentFrame(), this.getX(), this.getY(), this.getWidth(), this.getHeight());
	}

	@Override
	public void update(float dt) {
		if (animation == null)
			this.loadAssets();
		animation.animate(dt);
	}
}
