package shops.items;

import java.awt.image.BufferedImage;

import entities.player.Player;
import misc.ImageTools;

public class AdultStoreShopItem extends ShopItem {

	private Item item;
	
	public AdultStoreShopItem(Item item) {
		super(item.image, item.id, item.displayName, item.price);
		this.item = item;
	}
	
	public static enum Item {
		STRAP_ON("strapon", "Strap ON", 20.0f),
		BUTT_PLUG("buttplug", "BUTT PLUG!", 25.0f),
		ROPE("rope", "Bondage Rope", 10.0f),
		HANDCUFFS("handcuffs", "Handcuffs", 20.0f),
		WHIP("whip", "Whip", 35.0f),
		SHOCK_COLLAR("shock_collar", "Shock Collar", 50.0f);
		
		public BufferedImage image;
		public String displayName;
		public String id;
		public float price;
		
		Item(String id, String displayName, float price) {
			image = ImageTools.getImage("items/"+id+".png");
			this.displayName = displayName;
			this.id = id;
			this.price = price;
		}
	}

	@Override
	public void purchase(Player player) {
		switch (this.item) {
		case STRAP_ON:
			System.out.println("You should bring that to mish!");
			break;
		default:
			break;
		}
	}

}
