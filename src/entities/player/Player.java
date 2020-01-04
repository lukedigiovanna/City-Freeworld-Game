package entities.player;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.List;

import entities.*;
import entities.misc.TextParticle;
import entities.vehicles.Vehicle;
import weapons.Weapon;
import weapons.WeaponManager;
import display.Animation;
import main.Program;
import main.Settings;
import misc.*;
import world.Camera;
import world.Properties;

public class Player extends Human {
	
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
	private BufferedImage profilePicture;
	
	private WeaponManager weaponManager;
	
	public Player(float x, float y) {
		super(x,y,HumanAnimationPack.CHARACTER_0);
		this.bankAct = new BankAccount(this);
		this.profilePicture = ImageTools.getImage("profile_1.png");
		this.weaponManager = new WeaponManager(this);
		this.health = new Health(500);
		this.setProperty(Properties.KEY_HAS_RIGID_BODY, Properties.VALUE_HAS_RIGID_BODY_TRUE);
		addTag("player");
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
		super.update(dt);
		
		char up = Settings.getSetting("move_up").charAt(0);
		char down = Settings.getSetting("move_down").charAt(0);
		char left = Settings.getSetting("move_left").charAt(0);
		char right = Settings.getSetting("move_right").charAt(0);
		
		if (Program.keyboard.keyPressed('p')) {
			Path p = new Path();
			p.add(getX(),getY());
			p.add(getX()-3.0f,getY());
			p.add(getX(),getY()-3.0f);
			p.add(getX(),getY());
			this.queuePath(p);
		}
		
		if (!this.isFollowingPath()) {
			this.getVelocity().zero();
			
			if (this.getRiding() == null) {
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
			} else { //then we are in a car
				if (Program.keyboard.keyDown(up))
					this.getRiding().accelerate(dt);
				
				if (Program.keyboard.keyDown(down))
					this.getRiding().brake(dt);
				
				if (Program.keyboard.keyDown(left))
					this.getRiding().turnLeft(dt);
				
				if (Program.keyboard.keyDown(right))
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
			
			if (Program.keyboard.keyPressed(KeyEvent.VK_F)) {
				if (getRiding() == null)
					findVehicle();
				else
					exitVehicle();
			}	
		}
		
		Weapon selected = this.getSelectedWeapon();
		if (selected != null) {
			if (Program.keyboard.keyDown(KeyEvent.VK_UP) && getRiding() == null) 
				selected.pullTrigger();
			else
				selected.releaseTrigger();
			
			if (Program.keyboard.keyPressed(KeyEvent.VK_R)) {
				selected.reload();
			}
		}
	
		this.weaponManager.listen();
		this.weaponManager.update(dt);
	}
	
	public Weapon getSelectedWeapon() {
		return this.weaponManager.getSelectedWeapon();
	}
	
	public WeaponManager getWeaponManager() {
		return this.weaponManager;
	}
}
