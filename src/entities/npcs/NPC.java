package entities.npcs;

import java.util.List;

import entities.*;
import misc.MathUtils;
import weapons.Weapon;
import world.Properties;



public class NPC extends Human {
	
	public NPC(float x, float y) {
		super(x,y,HumanAnimationPack.CHARACTER_0);
		this.health = new Health(20,20);
		this.setProperty(Properties.KEY_INVULNERABLE, Properties.VALUE_INVULNERABLE_FALSE);
		addTag("npc");
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
		
//		if (angerAt != null) {
//			this.setRotation(this.angleTo(angerAt));
//			this.thisWeapon.pullTrigger();
//		}
		
		List<Entity> players = this.getRegion().getEntities().get("player");
		if (players.size() > 0)
			angerAt = players.get(0);
		
		if (this.canSee(angerAt)) {
			float angle = this.angleTo(angerAt);
			this.setRotation(angle);
			if (this.distanceTo(angerAt) > 3) {
				float speed = 1.0f;
				this.walkForward(speed);
			} else {
				this.setVelocity(0,0);
				this.getSelectedWeapon().pullTrigger();
			}
			this.clearPaths();
		} else if (!this.isFollowingPath()) {
			this.setVelocity(0,0);
		}
		
		thisWeapon.update(dt);
	}
	
	private Entity angerAt = null;

	private Weapon thisWeapon = new Weapon(this,Weapon.Type.AK_47);
	
	@Override
	public Weapon getSelectedWeapon() {
		return thisWeapon;
	}
}
