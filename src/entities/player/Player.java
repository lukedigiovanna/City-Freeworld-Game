package entities.player;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.List;

import entities.*;
import entities.vehicles.Vehicle;
import weapons.Weapon;
import weapons.WeaponManager;
import display.Animation;
import main.Program;
import main.Settings;
import misc.*;
import world.Camera;
import world.Properties;

public class Player extends Entity {
	
	private static final List<BufferedImage> 
		WALK_0 = ImageTools.getImages("assets/images/characters/character_0", "walk_"),
		IDLE_0 = ImageTools.getImages("assets/images/characters/character_0", "idle_"),
		HOLDING_SHORT_GUN_0 = ImageTools.getImages("assets/images/characters/character_0", "holding_short_gun_"),
		HOLDING_LONG_GUN_0 = ImageTools.getImages("assets/images/characters/character_0", "holding_long_gun_"),
		WALK_1 = ImageTools.getImages("assets/images/characters/character_1", "walk_"),
		IDLE_1 = ImageTools.getImages("assets/images/characters/character_1", "idle_"),
		HOLDING_SHORT_GUN_1 = ImageTools.getImages("assets/images/characters/character_1", "holding_short_gun_"),
		HOLDING_LONG_GUN_1 = ImageTools.getImages("assets/images/characters/character_1", "holding_long_gun_");
	
	private String name = "Earl";
	private BankAccount bankAct;
	private WeaponManager weaponManager;
	private BufferedImage profilePicture;
	
	public Player(float x, float y) {
		super(x,y,11.0f/16,12.0f/16);
		this.bankAct = new BankAccount(this);
		this.weaponManager = new WeaponManager(this);
		this.setProperty(Properties.KEY_HITBOX_HAS_ROTATION, Properties.VALUE_HITBOX_HAS_ROTATION_FALSE);
		this.profilePicture = ImageTools.getImage("profile_1.png");
		this.setLightEmissionValue(0.2f);
		addTag("player");
	}
	
	private Animation walk =                         new Animation(WALK_0,8),
					  idle =                         new Animation(IDLE_0,1),
					  holdingLongGun =   new Animation(HOLDING_LONG_GUN_0,1),
					  holdingShortGun = new Animation(HOLDING_SHORT_GUN_0,1);

	private Animation curAni = walk;
	
	@Override
	public void draw(Camera camera) {
		if (riding == null) {
			camera.drawImage(curAni.getCurrentFrame(),getX(),getY(),getWidth(),getHeight());
			Weapon selected = this.getSelectedWeapon();
			if (selected != null && curAni == holdingLongGun || curAni == holdingShortGun) {
				float height = 0.125f;
				camera.drawImage(selected.getType().display, getX()+getWidth()-0.25f, centerY()-height/2, 0.5f, height);
			}
		}
	}
	
	public String getMoneyDisplay() {
		double money = MathUtils.round(bankAct.getMoney(), 0.01);
		String monStr = money+"";
		if (money % 0.1 == 0)
			monStr+="0";
		if (money % 1.0 == 0)
			monStr+="0";
		return "$"+monStr;
	}

	public BufferedImage getProfilePicture() {
		return this.profilePicture;
	}
	
	public String getName() {
		return this.name;
	}

	public String getReputation() {
		return "None";
	}

	private float speed = 2.0f;
	private float rotationalSpeed = (float)Math.PI*1.2f;
	@Override
	public void update(float dt) {
		char up = Settings.getSetting("move_up").charAt(0);
		char down = Settings.getSetting("move_down").charAt(0);
		char left = Settings.getSetting("move_left").charAt(0);
		char right = Settings.getSetting("move_right").charAt(0);
		
		//this.getRegion().addParticles(Particle.Type.SPARKLES, Color.RED, (int)(20 * dt), 0.1f, getX(),getY(),getWidth(),getHeight());
		
		if (Program.keyboard.keyPressed('p')) {
			Path p = new Path();
			p.add(getX(),getY());
			p.add(getX()-3.0f,getY());
			p.add(getX()-3.0f,getY()-3.0f);
			this.queuePath(p);
		}
		
		if (!this.isFollowingPath()) {
			this.getVelocity().zero();
			
			if (this.riding == null) {
				float mag = 0.0f;
				float r = 0.0f;
				speed = 2;
				if (Program.keyboard.keyDown(KeyEvent.VK_SHIFT))
					speed = 4;
				if (Program.keyboard.keyDown(up))
					mag += speed;
				if (Program.keyboard.keyDown(down))
					mag -= speed/2;
				if (Program.keyboard.keyDown(left))
					r -= rotationalSpeed;
				if (Program.keyboard.keyDown(right))
					r += rotationalSpeed;
				
				this.getVelocity().r = r;
				this.getVelocity().setMagnitude(mag);
				this.getVelocity().setAngle(this.getRotation());
				if (mag < 0)
					this.getVelocity().setAngle(this.getVelocity().getAngle() + (float)Math.PI);
				
				if (mag == 0)
					curAni = idle;
				else
					curAni = walk;
			} else { //then we are in a car
				if (Program.keyboard.keyDown(up))
					this.riding.accelerate(dt);
				
				if (Program.keyboard.keyDown(down))
					this.riding.brake(dt);
				
				if (Program.keyboard.keyDown(left))
					this.riding.turnLeft(dt);
				
				if (Program.keyboard.keyDown(right))
					this.riding.turnRight(dt);
				
				this.setPosition(riding.centerX()-this.getWidth()/2, riding.centerY()-this.getHeight()/2);
			}
			
			if (this.getVelocity().r == 0) {
				int positions = 16;
				double size = Math.PI*2/positions;
				float angle = this.getRotation();
				while (angle < 0)
					angle += Math.PI * 2;
				while (angle >= Math.PI * 2)
					angle -= Math.PI * 2;
				for (int i = 0; i < positions; i++) {
					double theta = i/(double)positions*Math.PI*2;
					if (angle > theta-size/2 && angle <= theta + size/2) {
						this.setRotation((float)theta);
					}
					if (theta == 0) {
						if (angle < size/2 || angle > Math.PI*2-size/2)
							this.setRotation((float)theta);
					}
				}
			}
			
			if (Program.keyboard.keyPressed(KeyEvent.VK_F)) {
				if (riding == null)
					findVehicle();
				else
					exitVehicle();
			}	
		}
	
		Weapon selected = this.getSelectedWeapon();
		if (selected != null) {
			if (Program.keyboard.keyDown(KeyEvent.VK_UP) && riding == null) 
				selected.pullTrigger();
			else
				selected.releaseTrigger();
			
			if (Program.keyboard.keyPressed(KeyEvent.VK_R)) {
				selected.reload();
			}
			
			if (curAni == idle) {
				if (selected.isLong()) 
					curAni = holdingLongGun;
				else
					curAni = holdingShortGun;
			}
		}
	
		curAni.animate(dt*this.getVelocity().getLength()/2);
		
		this.weaponManager.listen();
		this.weaponManager.update(dt);
	}
	
	public WeaponManager getWeaponManager() {
		return this.weaponManager;
	}
	
	public Weapon getSelectedWeapon() {
		return this.weaponManager.getSelectedWeapon();
	}
	
	/**
	 * Looks around the player for an available car to enter
	 * If there is a car close enough then it will get in
	 */
	private void findVehicle() {
		List<Entity> vehicles = this.getRegion().getEntities().get("vehicle");
		Entity closest = null;
		float maxDistance = 3.0f; //maximum distance the car can be away
		float distanceToVehicle = 99999.0f; //start the distance high
		for (Entity vehicle : vehicles) {
			//can't enter a vehicle that is moving -- that makes no sense
			if (vehicle.getVelocity().getLength() != 0)
				continue;
			//get the distance
			float distance = this.distanceTo(vehicle);
			if (distance < maxDistance && distance < distanceToVehicle) {
				distanceToVehicle = distance;
				closest = vehicle;
			}
		}
		//if the vehicle is found then enter the vehicle
		if (closest != null) 
			enterVehicle((Vehicle)closest);
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
		this.riding.setDriver(null);
		this.riding = null;
	}
}
