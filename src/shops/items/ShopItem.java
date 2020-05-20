package shops.items;

import entities.player.Player;

public abstract class ShopItem {
	private float price;
	private String name;
	
	public ShopItem() {
		
	}
	
	public abstract void purchase(Player player);
}
