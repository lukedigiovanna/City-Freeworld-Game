package entities.player;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.List;

import display.ui.UI;
import entities.*;
import entities.misc.Tag;
import entities.misc.interactables.InteractableObject;
import entities.npcs.NPC;
import entities.projectiles.Grenade;
import entities.vehicles.Vehicle;
import game.Game;
import weapons.Weapon;
import weapons.WeaponManager;
import main.Program;
import main.Settings;
import misc.*;
import phone.Phone;
import world.Camera;
import world.Properties;

public class Player extends Human {
	
	private String name = "Earl";
	private BankAccount bankAct;
	private Phone phone;
	private transient BufferedImage profilePicture;
	
	private WeaponManager weaponManager;
	
	private float policeHeat; //measures how wanted the player is 0 means none, up to 5 which is very heavy
	
	private int xpPoints; //how many raw XP points the player has, NOT their level
	private int xpLevel;
	
	public Player(float x, float y) {
		super(x,y,HumanAnimationPack.CHARACTER_0);
		this.bankAct = new BankAccount(this);
		this.phone = new Phone(this);
		this.profilePicture = ImageTools.getImage("profile_1.png");
		this.weaponManager = new WeaponManager(this);
		this.health = new Health(this,50);
		this.health.setRegenerationRate(2.0f);
		addTag("player");
	}
	
	public void loadAssets() {
		super.loadAssets();
		profilePicture = ImageTools.getImage("profile_1.png");
	}
	
	public void addMoney(float amount) {
		this.popTextParticle("+$"+MathUtils.round(amount, 0.01), Color.GREEN.darker());
		this.bankAct.addMoney(amount);
	}
	
	public int getXPLevel() {
		return this.xpLevel;
	}
	
	public int getXPPoints() {
		return this.xpPoints;
	}
	
	private int xpForNextLevel = 10; //a1
	private float rFactor = 1.5f; //r for geometric series
	private int xpToNextLevel = 0;
	public void addXP(int points) {
		this.xpPoints+=points;
		this.xpToNextLevel+=points;
		updateXPLevel();
	}
	
	private void updateXPLevel() {
		if (this.xpToNextLevel >= this.xpForNextLevel) {
			this.xpToNextLevel -= this.xpForNextLevel; //remove the xp for this level
			this.xpLevel++; //add the level
			this.xpForNextLevel*=rFactor; //increase the amount of xp necessary for next level
			updateXPLevel(); //call again for carry over XP
		}
	}
	
	public float getPercentToNextLevel() {
		return (float)this.xpToNextLevel/this.xpForNextLevel;
	}
	
	public String getHeatString() {
		if (this.policeHeat > 4)
			return "KILL!";
		else if (this.policeHeat > 3)
			return "RUN!";
		else if (this.policeHeat > 2)
			return "HIDE!";
		else if (this.policeHeat > 1)
			return "WATCH!";
		else
			return "OK!";
	}
	
	public String getMoneyDisplay() {
		double money = MathUtils.round(bankAct.getMoney(), 0.01);
		String monStr = money+"";
		if (money % 0.1 == 0)
			monStr+="0";
		if (money % 1.0 == 0 && money > 0)
			monStr+="0";
		return "$"+monStr;
	}
	
	public float getPoliceHeat() {
		return this.policeHeat;
	}
	
	public boolean isCaught() {
		return false;
	}

	public void draw(Camera c) {
		if (this.getRiding() != null)
			return; //don't draw if we are in a car
		
		c.setColor(Color.CYAN);
		c.setStrokeWidth(0.025f);
		c.drawOval(getX()-0.1f, getY()-0.1f, getWidth()+0.2f, getHeight()+0.2f);
		super.draw(c);
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
	
	public BankAccount getBankAccount() {
		return this.bankAct;
	}

	private float speed = 2.0f;
	private float rotationalSpeed = (float)Math.PI*1.2f;
	@Override
	public void update(float dt) {
		super.update(dt);
		
		if (this.health.isDead()) {
			this.setProperty(Properties.KEY_HAS_COLLISION, Properties.VALUE_HAS_COLLISION_FALSE);
			this.health.setRegenerationRate(0); //no regen.
			return; //don't try any other stuff.. were dead.
		}
		
		char up = Settings.getChar("move_up"), down = Settings.getChar("move_down"), left = Settings.getChar("move_left"), right = Settings.getChar("move_right");
		
	
		UI input = Game.getInput();
		
		if (input.keyPressed(KeyEvent.VK_SPACE)) {
			Grenade g = new Grenade(this,getX(),getY(),getRotation());
			this.getRegion().add(g);
		}
		
		if (!this.isFollowingPath()) {
			this.getVelocity().zero();
			
			if (this.getRiding() == null) {
				this.enableHitbox();
//				float mag = 0.0f;
//				float r = 0.0f;
//				
//				speed = 2;
//				if (input.keyDown(KeyEvent.VK_SHIFT))
//					speed = 4;
//				
//				if (input.keyDown(up))
//					mag += speed;
//				if (input.keyDown(down))
//					mag -= speed/2;
//				if (input.keyDown(left))
//					r -= rotationalSpeed;
//				if (input.keyDown(right))
//					r += rotationalSpeed;
//				
//				this.getVelocity().r = r;
//				this.walkForward(mag);
//				this.getVelocity().setAngle(this.getRotation());
//				if (mag < 0)
//					this.getVelocity().setAngle(this.getVelocity().getAngle() + (float)Math.PI);
				float angleToMouse = (float)MathUtils.normalizeAngle(this.angleTo(this.getRegion().getMousePosition()));
				
				float speed = 1.5f;
				if (input.keyDown(KeyEvent.VK_SHIFT))
					speed*=2;
				
				float mag = 0;
				
				if (input.keyDown(up))
					mag+=speed;
				if (input.keyDown(down))
					mag-=speed/2;
				
				float thisAngle = (float)MathUtils.normalizeAngle(this.getRotation());
				
				//finds the direction to rotate that is the shortest amount to our goal from our current rotation
				int dir = -1;
				float posDistance = angleToMouse - thisAngle;
				if (angleToMouse < thisAngle)
					posDistance = ((float)Math.PI * 2 - thisAngle) + angleToMouse;
				if (posDistance < Math.PI)
					dir = 1;
				
				//if we are 'close enough' to our goal then dont rotate
				if (Math.abs(thisAngle-angleToMouse) < 0.1)
					dir = 0;
				
				float rotSpeed = (float)Math.PI;
				
				this.walkForward(mag);
				this.getVelocity().setR(rotSpeed * dir);
				this.getVelocity().setAngle(thisAngle);
				if (mag < 0)
					this.getVelocity().setAngle((float)Math.PI+thisAngle);
			} else { //then we are in a car
				this.disableHitbox();
				
				if (input.keyDown(up))
					this.getRiding().accelerate(dt);
				
				if (input.keyDown(down))
					this.getRiding().brake(dt);
				
				if (input.keyDown(left))
					this.getRiding().turnLeft(dt);
				
				if (input.keyDown(right))
					this.getRiding().turnRight(dt);
				
				this.setPosition(getRiding().centerX()-this.getWidth()/2, getRiding().centerY()-this.getHeight()/2);
			}
			
			if (this.getVelocity().r == 0) {
				int positions = 64;
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
			
			char findCar = Settings.getChar("find_car"),
				 attemptRobbery = Settings.getChar("attempt_robbery"),
				 interact = Settings.getChar("interact"),
				 togglePhone = Settings.getChar("toggle_phone");
			
			if (input.keyPressed(findCar)) {
				if (getRiding() == null)
					findVehicle();
				else
					exitVehicle();
			}	
			
			if (input.keyPressed(attemptRobbery)) {
				attemptRobbery();
			}
			
			if (input.keyPressed(interact)) {
				InteractableObject i = (InteractableObject)this.findClosest(3, "interactable");
				if (i != null)
					i.use(this);
			}
			
			if (input.keyPressed(togglePhone)) {
				this.phone.open();
			}
		}
		
		Weapon selected = this.getSelectedWeapon();
		if (selected != null) {
			if (/*input.keyDown(KeyEvent.VK_UP)*/ input.isMouseDown() && getRiding() == null) 
				selected.pullTrigger();
			else
				selected.releaseTrigger();
			
			if (input.keyPressed(KeyEvent.VK_R)) {
				selected.reload();
			}
		}
	
		this.weaponManager.listen();
		this.weaponManager.update(dt);
		
		this.phone.update(dt);
	}
	
	public void attemptRobbery() {
		Entity closest = this.findClosest(3, "npc");
		//if the NPC is found then try to rob it
		if (closest != null) {
			NPC npc = (NPC)closest;
			npc.rob(this);
		}
	}
	
	public Weapon getSelectedWeapon() {
		return this.weaponManager.getSelectedWeapon();
	}
	
	public WeaponManager getWeaponManager() {
		return this.weaponManager;
	}
	
	public Phone getPhone() {
		return this.phone;
	}
}
