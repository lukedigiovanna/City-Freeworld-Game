package item.weapon;

/**
 * Includes all firearms such as pistols that fire bullets
 */
public class Gun extends Weapon {

	public static final int CATEGORY_HAND_GUN = 0,
							CATEGORY_SUB_MACHINE_GUN = 1,
							CATEGORY_RIFLE = 2,
							CATEGORY_SHOTGUN = 3,
							CATEGORY_HEAVY_SPECIAL = 4,
							CATEGORY_LIGHT_SPECIAL = 5;
	
	public static enum Type {
		GLOCK_18("Glock 18",CATEGORY_HAND_GUN,5.0f,1.5f,17);
		
		float fireRate,
			  reloadTime;
		int magSize;
		int category;
		String name;
		
		Type(String name, int category, float fireRate, float reloadTime, float magSize) {
			
		}
	}
	
	/**
	 * Variables for the amount of bullets available to shoot
	 * Mag Size: How many bullets can fit in a mag
	 * Loaded In Mag: The current amount of bullets in the mag
	 * Stock: The total amount of bullets available for the gun
	 */
	private int magSize, loadedInMag, stock;
	
	/**
	 * The maxmimum number of bullets the gun can shoot in a second
	 */
	private float fireRate;
	
	/**
	 * Style One: One press = one shot
	 * Style Burst: Fires multiple bullets quickly, then stops
	 * Style Constant: Continuously fire while fire key is held
	 */
	public static final int FIRE_STYLE_ONE = 0, FIRE_STYLE_BURST = 1, FIRE_STYLE_CONSTANT = 2;
	/**
	 * The way the gun shoots
	 */
	private int fireStyle;
	
	/**
	 * Determines whether there is a supressor on the gun
	 */
	private boolean suppressed = false;
	
	public Gun(Type type) {
		
	}
	
}
