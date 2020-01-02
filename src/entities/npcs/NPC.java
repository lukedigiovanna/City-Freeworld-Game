package entities.npcs;

import java.awt.image.BufferedImage;

import entities.Entity;
import entities.Health;
import entities.Human;
import entities.HumanAnimationPack;
import entities.Path;
import misc.ImageTools;
import misc.MathUtils;
import weapons.Weapon;
import world.Camera;
import world.Properties;

public class NPC extends Human {
	
	public NPC(float x, float y) {
		super(x,y,HumanAnimationPack.CHARACTER_1);
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
	}

	@Override
	public Weapon getSelectedWeapon() {
		return null;
	}
}
