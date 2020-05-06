package entities.npcs;

import java.awt.Color;
import java.util.List;

import entities.*;
import entities.misc.TextParticle;
import entities.misc.pickups.CashPickup;
import entities.player.Player;
import misc.MathUtils;
import weapons.Weapon;
import world.Properties;



public class NPC extends Human {
	
	public NPC(float x, float y) {
		super(x,y,HumanAnimationPack.CHARACTER_0);
		this.health = new Health(this,20);
		this.setProperty(Properties.KEY_INVULNERABLE, Properties.VALUE_INVULNERABLE_FALSE);
		addTag("npc");
		
		moneyOnHand = MathUtils.round(MathUtils.random(30f,250f), 0.01f);
	}

	private float moneyOnHand = 0; //how much money the NPC has (which can be robbed)
	
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
		
		if (this.canSee(focusedAt)) {
			float angle = this.angleTo(focusedAt);
			if (this.state == State.ANGRY) {
				this.setRotation(angle);
				if (this.distanceTo(focusedAt) > 3) {
					float speed = 1.0f;
					this.walkForward(speed);
				} else {
					this.setVelocity(0,0);
					this.getSelectedWeapon().pullTrigger();
				}
			} else if (this.state == State.SCARED) {
				this.setRotation(angle + (float)Math.PI);
				if (this.distanceTo(focusedAt) < 10) {
					float speed = 2.0f;
					this.walkForward(speed);
				} else {
					this.state = State.IDLE;
					this.focusedAt = null;
					this.setVelocity(0,0);
				}
			}
			this.clearPaths();
		} else if (!this.isFollowingPath()) {
			this.setVelocity(0,0);
		}
		
		if (this.beingRobbed) {
			robTimer += dt;
			particleTimer += dt;
			this.getVelocity().zero();
			float angle = this.angleTo(focusedAt);
			angle += Math.PI/8 * Math.cos(robTimer * Math.PI * 4);
			this.setRotation(angle);
			if (particleTimer > 0.25f) {
				particleTimer %= 0.25f;
				this.popTextParticle("!", Color.CYAN);
			}
			if (robTimer > 5.0f) {
				robber.addMoney(this.moneyOnHand);
				this.moneyOnHand = 0.0f;
				this.state = State.SCARED;
				this.beingRobbed = false;
				robTimer = 0;
			}
		}
		
		thisWeapon.update(dt);
	}
	
	private float fightWillingness = MathUtils.random(0,1.0f);
	private boolean beingRobbed = false;
	private float robTimer = 0.0f;
	private float particleTimer = 0.0f;
	private Player robber;
	public void rob(Player robber) {
		if (beingRobbed)
			return;
		if (Math.random() * fightWillingness < 0.6f) { //most people will succumb
			//go into rob mode
			beingRobbed = true;
			this.robber = robber;
		} else {
			//fight the robber	
			this.state = State.ANGRY;
		}
		this.focusedAt = robber;
	}
	
	public static enum State {
		ANGRY(),
		SCARED(),
		IDLE()
	}
	
	private Entity focusedAt = null;
	private State state = State.IDLE;
	
	private Weapon thisWeapon = new Weapon(this,Weapon.Type.AK_47);
	
	@Override
	public Weapon getSelectedWeapon() {
		if (this.state == State.ANGRY)
			return thisWeapon;
		else
			return null;
	}
	
	@Override
	public void destroy() {
		if (this.health.isDead()) { //only if killed from hit depletion, not other game mechanics
			//summon our cash
			if (this.moneyOnHand > 0)
				this.getRegion().add(new CashPickup(this.getX(),this.getY(),this.moneyOnHand));
		}
		super.destroy();
	}
}
