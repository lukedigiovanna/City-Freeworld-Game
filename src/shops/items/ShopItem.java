package shops.items;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import entities.player.Player;
import misc.ImageTools;
import weapons.Weapon;

public abstract class ShopItem {
	private float price;
	private String name;
	private String id;
	private BufferedImage icon;
	
	public ShopItem(BufferedImage icon, String id, String displayName, float price) {
		this.id = id;
		this.name = displayName;
		this.price = price;
		
		this.icon = icon;
		
		items.put(id, this);
	}
	
	public ShopItem(String id, String displayName, float price) {
		this(ImageTools.getImage("items/"+id+".png"),id,displayName,price);
	}
	
	public String getName() {
		return this.name;
	}
	
	public float getPrice() {
		return this.price;
	}
	
	public BufferedImage getIcon() {
		return this.icon;
	}
	
	/**
	 * Returns true if the transaction was successful
	 * @param player
	 * @return
	 */
	public boolean attemptPurchase(Player player) {
		if (player.getBankAccount().useMoney(this.price)) { //only returns true if there was enough money in the acct
			purchase(player); //then complete the purchase
			return true;
		} else {
			return false;
		}
	}
	
	public abstract void purchase(Player player);
	
	private static Map<String,ShopItem> items;
	
	public static void initialize() {
		items = new HashMap<String,ShopItem>();
		
		new WeaponShopItem(Weapon.Type.GLOCK_21,400);
		new WeaponShopItem(Weapon.Type.DESERT_EAGLE,700);
		new WeaponShopItem(Weapon.Type.REVOLVER,800);
		new WeaponShopItem(Weapon.Type.AK_47,2500);
		new WeaponShopItem(Weapon.Type.M4,2800);
		new WeaponShopItem(Weapon.Type.SAWED_OFF,1100);
		new WeaponShopItem(Weapon.Type.PUMP_ACTION,1400);
	}
	
	public static ShopItem get(String id) {
		return items.get(id);
	}
}
