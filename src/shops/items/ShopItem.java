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
	
	public ShopItem(String id, String displayName, float price) {
		this.id = id;
		this.name = displayName;
		this.price = price;
		
		this.icon = ImageTools.getImage("items/"+id+".png");
		
		items.put(id, this);
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
	
	public void attemptPurchase(Player player) {
		if (player.getBankAccount().useMoney(this.price)) //only returns true if there was enough money in the acct
			purchase(player); //then complete the purchase
	}
	
	public abstract void purchase(Player player);
	
	private static Map<String,ShopItem> items;
	
	public static void initialize() {
		items = new HashMap<String,ShopItem>();
		
		new WeaponShopItem(Weapon.Type.GLOCK_21,400);
		new WeaponShopItem(Weapon.Type.DESERT_EAGLE,700);
		new WeaponShopItem(Weapon.Type.REVOLVER,800);
		new WeaponShopItem(Weapon.Type.AK_47,2500);
	}
	
	public static ShopItem get(String id) {
		return items.get(id);
	}
}
