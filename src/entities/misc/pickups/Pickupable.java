package entities.misc.pickups;

import java.util.List;

import display.Animation;
import entities.Entity;
import entities.player.Player;
import misc.ImageTools;
import world.Camera;
import world.Properties;

public abstract class Pickupable extends Entity {
	
	private Animation animation;
	
	public Pickupable(String iconName, float x, float y, float width, float height) {
		super(x, y, width, height);	
		animation = new Animation(ImageTools.getImage(iconName+".png"));
		this.setProperty(Properties.KEY_INVULNERABLE, Properties.VALUE_INVULNERABLE_TRUE);
		this.setProperty(Properties.KEY_HAS_COLLISION, Properties.VALUE_HAS_COLLISION_FALSE);
		this.addTag("pickupable");
	}
	
	public abstract void onPickup(Player player);

	@Override
	public void draw(Camera camera) {
		camera.drawImage(animation.getCurrentFrame(), getX(), getY() + oscilation, getWidth(), getHeight());
	}

	private float oscilation = 0.0f;
	@Override
	public void update(float dt) {
		animation.animate(dt);
		List<Entity> players = this.getRegion().getEntities().get("player");
		for (Entity p : players)
			if (this.colliding(p)) {
				this.onPickup((Player)p);
				this.destroy();
				break;
			}
		oscilation = (float)Math.sin(this.getAge() * 2) / 3f;
	}

}
