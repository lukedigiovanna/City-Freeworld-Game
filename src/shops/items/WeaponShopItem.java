package shops.items;

import entities.player.Player;
import weapons.Weapon;

public class WeaponShopItem extends ShopItem {

	private Weapon.Type weaponType;
	
	public WeaponShopItem(Weapon.Type weaponType, float price) {
		super(weaponType.icon,weaponType.id, weaponType.name, price);
		this.weaponType = weaponType;
	}
	
	public void purchase(Player player) {
		System.out.println(weaponType.name); 
		player.getWeaponManager().getWeaponScroll().addWeapon(new Weapon(player,weaponType));
	}
}
