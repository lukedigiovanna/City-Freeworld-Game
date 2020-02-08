/**
 * 
 */
package entities.misc;

import display.Animation;
import display.textures.*;
import entities.Entity;
import world.Camera;
import world.Properties;

public class EntityObject extends Entity {
	
	private Texture texture;
	private Animation animation;
	
	public EntityObject(int id, float x, float y) {
		super(x,y);
		this.texture = TexturePack.current().getObjectTexture(id);
		this.animation = this.texture.getAnimation().copy(); 
		this.setDimension(this.texture.getDimension().copy());
		this.setLightEmissionValue(texture.getLightEmissionValue());
		this.setVerticalHeight(texture.getVerticalHeight());
		//System.out.println(this.getVerticalHeight());
		this.setHitboxToDimension();
		this.setProperty(Properties.KEY_INVULNERABLE, Properties.VALUE_INVULNERABLE_TRUE);
		//this.setProperty(Properties.KEY_HAS_RIGID_BODY, Properties.VALUE_HAS_RIGID_BODY_TRUE);
		this.addTag("entity_object");
	}

	@Override
	public void draw(Camera camera) {
		camera.drawImage(animation.getCurrentFrame(), this.getX(), this.getY(), this.getWidth(), this.getHeight());
	}

	@Override
	public void update(float dt) {
		animation.animate(dt);
	}
}
