package weapons;

import java.awt.image.BufferedImage;

import entities.Entity;
import entities.misc.Particle;
import entities.projectiles.Bullet;
import entities.projectiles.Projectile;
import misc.ImageTools;
import misc.MathUtils;
import soundEngine.Sound;
import soundEngine.SoundManager;

/**
 * Includes all firearms such as pistols that fire bullets
 */
public class Weapon {

	public static final int CATEGORY_HAND_GUN = 0,
							CATEGORY_SUB_MACHINE_GUN = 1,
							CATEGORY_RIFLE = 2,
							CATEGORY_SHOT_GUN = 3,
							CATEGORY_HEAVY_SPECIAL = 4,
							CATEGORY_LIGHT_SPECIAL = 5;
	
	public static int ICON_WIDTH = 40,
					  ICON_HEIGHT = 15;
	
	public static enum Type {
		GLOCK_21("Glock 21","glock21",CATEGORY_HAND_GUN,FIRE_STYLE_ONE,5.0f,1.5f,17,10.0f,0.025f),
		DESERT_EAGLE("Desert Eagle","desert_eagle",CATEGORY_HAND_GUN,FIRE_STYLE_ONE,5.0f,1.5f,7,15.0f,0.15f),
		REVOLOVER("Revolver","revolver",CATEGORY_HAND_GUN,FIRE_STYLE_ONE,5.0f,2.0f,6,12.5f,0.125f),
		
		AK_47("AK-47","ak47",CATEGORY_RIFLE,FIRE_STYLE_CONSTANT,12.0f,4.0f,30,3.0f,0.2f);
		
		public float fireRate,
			  reloadTime,
			  firePower,
			  accuracy; //0 is dead on, 1 is very spread out.
		public int magSize;
		public int category, fireStyle;
		public String name;
		public BufferedImage icon;
		public BufferedImage display;
		
		Type(String name, String iconName, int category, int fireStyle, float fireRate, float reloadTime, int magSize, float firePower, float accuracy) {
			this.name = name;
			this.category = category;
			this.fireRate = fireRate;
			this.reloadTime = reloadTime;
			this.magSize = magSize;
			this.firePower = firePower;
			this.fireStyle = fireStyle;
			this.accuracy = accuracy;
			this.icon = ImageTools.getImage("assets/images/weapons/icons/"+iconName+".png");
			this.display = ImageTools.getImage("assets/images/weapons/displays/"+iconName+".png");
		}
	}
	
	/**
	 * The type of Weapon from the enum Type
	 */
	private Type type;
	
	/**
	 * Variables for the amount of bullets available to shoot
	 * Loaded In Mag: The current amount of bullets in the mag
	 * Stock: The total amount of bullets available for the Weapon
	 */
	private int loadedInMag, stock;
	
	/**
	 * The timer to count for when it can fire again
	 */
	private float fireTime;
	
	/**
	 * Style One: One press = one shot
	 * Style Burst: Fires multiple bullets quickly, then stops
	 * Style Constant: Continuously fire while fire key is held
	 */
	public static final int FIRE_STYLE_ONE = 0, FIRE_STYLE_BURST = 1, FIRE_STYLE_CONSTANT = 2;
	
	/**
	 * Says whether or not the weapon should be firing
	 */
	private boolean triggerPulled = false;
	
	/**
	 * The entity who is currently handling the Weapon
	 */
	private Entity owner;
	
	/**
	 * The total radians of inaccuracy possible
	 */
	private float inAccuracyRange;
	
	public Weapon(Entity owner, Type type) {
		this.owner = owner;
		this.type = type;
		this.loadedInMag = type.magSize;
		this.stock = type.magSize * 2;
		this.inAccuracyRange = type.accuracy * (float)Math.PI/8;
	}
	
	private int shotsFiredStreak = 0;
	
	private int maxBurst = 5;
	
	public void update(float dt) {
		fireTime += dt;
		
		if (reload) {
			if (this.loadedInMag >= this.type.magSize) {
				reload = false;
			} else {
				reloadTimer += dt;
				if (reloadTimer >= type.reloadTime) {
					//put as many bullets into the mag as we can
					int needed = type.magSize - this.loadedInMag;
					int used = MathUtils.min(needed, stock);
					stock -= used;
					this.loadedInMag += used;
					this.reloadTimer = 0.0f;
					reload = false;
				}
			}
		}
		
		//for making the click sound when the player tries to shoot with no ammo
		if (this.triggerPulled && this.getLoadedAmmo() == 0 && shotsFiredStreak == 0) {
			SoundManager.play(Sound.GUN_CLICK);
			shotsFiredStreak++;
		}
		
		if (this.triggerPulled && this.getLoadedAmmo() > 0 && fireTime >= 1/type.fireRate) {
			switch (this.type.fireStyle) {
			case FIRE_STYLE_ONE:
				if (shotsFiredStreak == 0) {
					shoot();
					shotsFiredStreak++;
				}
				break;
			case FIRE_STYLE_BURST:
				if (shotsFiredStreak < maxBurst) {
					shoot();
					shotsFiredStreak++;
				}
				break;
			case FIRE_STYLE_CONSTANT:
				shoot();
				shotsFiredStreak++;
			}
		}
	}
	
	private void shoot() {
		float inaccuracyOffset = MathUtils.random(-inAccuracyRange, inAccuracyRange);
		float angle = owner.getRotation() + inaccuracyOffset;
		float dx = (float) ((owner.getWidth()/2+0.2f)*Math.cos(angle)),
			  dy = (float) ((owner.getHeight()/2+0.2f)*Math.sin(angle));
		float x = owner.centerX() + dx, y = owner.centerY() + dy;
		Projectile p = new Bullet(owner, x,y,angle);
		p.setDamage(type.firePower);
		owner.getRegion().addParticles(Particle.Type.GUNFIRE, null, 1, 0, x, y, 0, 0);
		owner.getRegion().add(p);
		this.loadedInMag--;
		this.fireTime = 0;
		SoundManager.play(Sound.GUN_SHOT);
	}
	
	private boolean reload = false;
	
	private float reloadTimer = 0.0f;
	
	public void reload() {
		reload = true;
	}
	
	public void pullTrigger() {
		this.triggerPulled = true;
	}
	
	public void releaseTrigger() {
		this.triggerPulled = false;
		shotsFiredStreak = 0;
	}
	
	public float getReloadPercent() {
		return this.reloadTimer/this.type.reloadTime;
	}
	
	public Type getType() {
		return type;
	}
	
	public int getLoadedAmmo() {
		return this.loadedInMag;
	}
	
	public int getAmmoStock() {
		return this.stock;
	}
	
	/**
	 * Returns whether the size of the gun is long or not
	 * Used for determining which gun holding position
	 * the player should be in.
	 * @return
	 */
	public boolean isLong() {
		return !(type.category == CATEGORY_HAND_GUN || type.category == CATEGORY_LIGHT_SPECIAL);
	}
}
