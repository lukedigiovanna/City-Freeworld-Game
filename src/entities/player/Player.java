package entities.player;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.List;

import entities.*;
import entities.npcs.NPC;
import entities.projectiles.Grenade;
import entities.vehicles.Vehicle;
import weapons.Weapon;
import weapons.WeaponManager;
import main.Program;
import main.Settings;
import misc.*;
import world.Camera;
import world.Properties;

public class Player extends Human {
	
	private String name = "Earl";
	private BankAccount bankAct;
	private transient BufferedImage profilePicture;
	
	private WeaponManager weaponManager;
	
	private float policeHeat; //measures how wanted the player is 0 means none, up to 5 which is very heavy
	
	private int xpPoints; //how many raw XP points the player has, NOT their level
	
	public Player(float x, float y) {
		super(x,y,HumanAnimationPack.CHARACTER_0);
		this.bankAct = new BankAccount(this);
		this.profilePicture = ImageTools.getImage("profile_1.png");
		this.weaponManager = new WeaponManager(this);
		this.health = new Health(this,50);
		this.health.setRegenerationRate(2.0f);
		this.setProperty(Properties.KEY_HAS_RIGID_BODY, Properties.VALUE_HAS_RIGID_BODY_TRUE);
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
		return MathUtils.max(0, (int)Math.log10(this.xpPoints));
	}
	
	public void addXP(int points) {
		this.xpPoints+=points;
	}
	
	public int getHeat() {
		return (int)this.policeHeat;
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

	private float speed = 2.0f;
	private float rotationalSpeed = (float)Math.PI*1.2f;
	@Override
	public void update(float dt) {
		super.update(dt);
		
		char up = ((String)Settings.getSetting("move_up")).charAt(0);
		char down = ((String)Settings.getSetting("move_down")).charAt(0);
		char left = ((String)Settings.getSetting("move_left")).charAt(0);
		char right = ((String)Settings.getSetting("move_right")).charAt(0);
		
		if (Program.keyboard.keyPressed('p')) {
			Path p = new Path();
			p.add(getX(),getY());
			p.add(getX()-3.0f,getY());
			p.add(getX(),getY()-3.0f);
			p.add(getX(),getY());
			this.queuePath(p);
		}
		
		this.addXP(1);
		
		if (Program.keyboard.keyPressed(KeyEvent.VK_SPACE)) {
			Grenade g = new Grenade(this,getX(),getY(),getRotation());
			this.getRegion().add(g);
		}
		
		if (!this.isFollowingPath()) {
			this.getVelocity().zero();
			
			if (this.getRiding() == null) {
				this.enableHitbox();
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
				this.walkForward(mag);
				this.getVelocity().setAngle(this.getRotation());
				if (mag < 0)
					this.getVelocity().setAngle(this.getVelocity().getAngle() + (float)Math.PI);
			} else { //then we are in a car
				this.disableHitbox();
				
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
			
			if (Program.keyboard.keyPressed(KeyEvent.VK_E)) {
				attemptRobbery();
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
	
	public void attemptRobbery() {
		List<Entity> npcs = this.getRegion().getEntities().get("npc");
		Entity closest = null;
		float maxDistance = 3.0f; //maximum distance the NPC can be away
		float distanceToNPC = 99999.0f; //start the distance high
		for (Entity npc : npcs) {
			//get the distance
			float distance = this.squaredDistanceTo(npc);
			if (distance < maxDistance * maxDistance && distance < distanceToNPC * distanceToNPC) {
				distanceToNPC = distance;
				closest = npc;
			}
		}
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
}
