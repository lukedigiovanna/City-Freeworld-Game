package item.weapon;

import java.awt.image.BufferedImage;

import item.Item;

/**
 * Over arching weapon class
 */
public class Weapon extends Item {
	//member vars for weapon displays
	private BufferedImage icon; //displayed in the scroll wheel and elsewhere
	private BufferedImage entityIcon; //displayed 
	
	//some member variables for gun statistics
	private float fireRate; //shots possible per second
	private boolean automatic;
	private float damagePerHit;
	private float reloadTime;
}
