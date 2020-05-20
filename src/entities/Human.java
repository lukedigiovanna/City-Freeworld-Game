package entities;

import java.util.List;

import display.Animation;
import entities.misc.CorpseParticle;
import entities.vehicles.Vehicle;
import misc.Vector2;
import weapons.Weapon;
import world.Camera;
import world.Properties;
import world.WorldObject;
import world.regions.Region;

public abstract class Human extends Entity {
	
	public static final int WALK_ANIMATION = 0, IDLE_ANIMATION = 1, CORPSE_ANIMATION = 2,
							HOLDING_SHORT_GUN_ANIMATION = 3, HOLDING_LONG_GUN_ANIMATION = 4;
	
	private transient Animation
		curAni,
		walkAni, idleAni, corpseAni, holdingLongGun, holdingShortGun;
	
	private float walkAniSpeed = 0;
	
	private HumanAnimationPack animations;
	
	
	
	public Human(float x, float y, HumanAnimationPack animations) {
		super(x,y,animations.width,animations.height);
		
		this.addTag("human");
		
		this.animations = animations;
		this.loadAssets();
		
		this.setFieldOfView(WorldObject.DEFAULT_FIELD_OF_VIEW);
		
		this.setVerticalHeight(7.0f); //high up
		
		this.setMass(60);
		
		this.setProperty(Properties.KEY_HAS_SQUISHY_BODY, Properties.VALUE_HAS_SQUISHY_BODY_TRUE);
		this.setProperty(Properties.KEY_HITBOX_HAS_ROTATION, Properties.VALUE_HITBOX_HAS_ROTATION_FALSE);
	}
	
	public void loadAssets() {
		walkAni = animations.walk.copy();
		idleAni = animations.idle.copy();
		corpseAni = animations.corpse.copy();
		holdingLongGun = animations.holdingLongGun.copy();
		holdingShortGun = animations.holdingShortGun.copy();
		curAni = idleAni;
	}
	
	public void setAnimation(int animationNumber) {
		switch (animationNumber) {
		case WALK_ANIMATION:
			curAni = walkAni;
			break;
		case IDLE_ANIMATION:
			curAni = idleAni;
			break;
		case CORPSE_ANIMATION:
			curAni = corpseAni;
			break;
		case HOLDING_SHORT_GUN_ANIMATION:
			curAni = holdingShortGun;
			break;
		case HOLDING_LONG_GUN_ANIMATION:
			curAni = holdingLongGun;
			break;
		}
	}

	@Override
	public void draw(Camera camera) {
		if (riding == null) {
			camera.drawImage(curAni.getCurrentFrame(),getX(),getY(),getWidth(),getHeight());
			Weapon selected = this.getSelectedWeapon();
			if (selected != null && (curAni == holdingLongGun || curAni == holdingShortGun)) {
				float height = 0.125f;
				camera.drawImage(selected.getType().display, getX()+getWidth()-0.25f, centerY()-height/2, 0.5f, height);
			}
		}
	}
	
	public abstract Weapon getSelectedWeapon();
	
	public void walkForward(float speed)  {
		this.walkAniSpeed = speed;
		float a = this.getRotation();
		this.setVelocity((float)(Math.cos(a) * speed), (float)(Math.sin(a) * speed));
	}
	
	public Vehicle getRiding() {
		return this.riding;
	}
	
	private Path pathToVehicle;
	private Vehicle vehicleToEnter;
	/**
	 * Looks around the player for an available car to enter
	 * If there is a car close enough then it will get in
	 */
	public void findVehicle() {
		Entity closest = this.findClosest(3, "vehicle");
		//if the vehicle is found then enter the vehicle
		if (closest != null) {
			enterVehicle((Vehicle)closest);
			//pathFindToVehicle((Vehicle)closest);
		}
	}
	
	public void pathFindToVehicle(Vehicle vehicle) {
		this.vehicleToEnter = vehicle;
		Vector2 goalPoint = new Vector2(vehicle.centerX(), vehicle.centerY() + (float)Math.sin(vehicle.getRotation()-Math.PI/2) * vehicle.getHeight());
		pathToVehicle = new Path(this);
		pathToVehicle.add(goalPoint);
		this.queuePath(pathToVehicle);
	}
	
	/**
	 * Sets the player to be riding the vehicle; gives control to the player
	 * @param vehicle
	 */
	public void enterVehicle(Vehicle vehicle) {
		vehicle.setDriver(this);
		this.riding = vehicle;
	}
	
	/**
	 * Leaves the vehicle currently being ridden
	 * if the player is riding one
	 */
	public void exitVehicle() {
		//move the human out the left side of the vehicle
		float dir = (float)(-Math.PI/2+this.riding.getRotation());
		float dist =  1.0f;
		this.setPosition(this.getX() + (float)Math.cos(dir) * dist, this.getY() + (float)Math.sin(dir) * dist);
		this.riding.setDriver(null);
		this.riding = null;
	}
	
	@Override
	public void destroy() {
		//add a corpse particle
		CorpseParticle corpse = new CorpseParticle(this.corpseAni,getX(),getY());
		corpse.setRotation(this.getRotation()-(float)Math.PI/2);
		getRegion().add(corpse);
		
		super.destroy();
	}

	private static final float NORMAL_WALKING_SPEED = 2.0f;
	
	@Override
	public void update(float dt) {
		if (pathToVehicle != null && pathToVehicle.completed()) {
			enterVehicle(vehicleToEnter);
			pathToVehicle = null;
		}
		
		curAni = idleAni;
		
		Weapon selected = this.getSelectedWeapon();
		if (selected != null && curAni == idleAni) {
			if (selected.isLong()) 
				curAni = holdingLongGun;
			else
				curAni = holdingShortGun;
		}
		
		if (this.getVelocity().getLength() != 0)
			curAni = walkAni;
		
		if (curAni == walkAni) 
			curAni.animate(dt * this.walkAniSpeed/NORMAL_WALKING_SPEED);
		else
			curAni.animate(dt);
	}
	
	public Animation getCorpseAnimation() {
		return this.corpseAni;
	}
}
