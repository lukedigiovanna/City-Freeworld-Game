package entities.npcs;

import java.util.List;
import java.awt.image.BufferedImage;

import entities.Entity;
import entities.Health;
import entities.Human;
import entities.HumanAnimationPack;
import entities.Path;
import misc.MathUtils;
import weapons.Weapon;
import world.Properties;



public class NPC extends Human {
	
	public NPC(float x, float y) {
		super(x,y,HumanAnimationPack.CHARACTER_0);
		this.health = new Health(20,20);
		this.setProperty(Properties.KEY_INVULNERABLE, Properties.VALUE_INVULNERABLE_FALSE);
		addTag("NPC");
	}

	private float timer = MathUtils.random(5.0f,15.0f);
	private float timerCount = 0.0f;
	
	@Override
	public void update(float dt) {
		super.update(dt);
		
		timerCount += dt;
		
		if (timerCount >= timer) {
			timer = MathUtils.random(5.0f,15.0f);
			timerCount = 0;
			float distance = MathUtils.random(1.0f,3.0f);
			float angle = MathUtils.random(0,(float)Math.PI*2);
			Path p = new Path();
			p.add(getX() + (float)Math.cos(angle)*distance, getY() + (float)Math.sin(angle) * distance);
			this.queuePath(p);
		}
		
		this.thisWeapon.releaseTrigger();
		
		if (angerAt != null) {
			this.setRotation(this.angleTo(angerAt));
			this.thisWeapon.pullTrigger();
		}
		
		List<Entity> players = this.getRegion().getEntities().get("player");
		if (players.size() > 0)
			angerAt = players.get(0);
		
		thisWeapon.update(dt);
	}
	
	private Entity angerAt = null;

	private Weapon thisWeapon = new Weapon(this,Weapon.Type.GLOCK_21);
	
	@Override
	public Weapon getSelectedWeapon() {
		return thisWeapon;
	}
}
