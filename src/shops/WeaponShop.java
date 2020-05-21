package shops;

import shops.items.ShopItem;

public class WeaponShop extends Shop {
	public WeaponShop() {
		super("Guns 'n Stuff");
		
		ShopSection pistols = new ShopSection("Handguns");
		pistols.add(ShopItem.get("glock21"));
		pistols.add(ShopItem.get("desert_eagle"));
		pistols.add(ShopItem.get("revolver"));
		this.add(pistols);
		ShopSection rifles = new ShopSection("Rifles");
		rifles.add(ShopItem.get("ak47"));
		rifles.add(ShopItem.get("m4"));
		this.add(rifles);
		ShopSection shotguns = new ShopSection("Shot Guns");
		shotguns.add(ShopItem.get("sawed_off"));
		shotguns.add(ShopItem.get("pump_action"));
		this.add(shotguns);
	}
}
