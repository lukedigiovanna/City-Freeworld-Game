package shops;

import shops.items.AdultStoreShopItem;

public class AdultStore extends Shop {
	public AdultStore() {
		super("Mish's Playhouse");
		
		ShopSection anals = new ShopSection("Anal");
		anals.add(new AdultStoreShopItem(AdultStoreShopItem.Item.STRAP_ON));
		anals.add(new AdultStoreShopItem(AdultStoreShopItem.Item.BUTT_PLUG));
		this.add(anals);
		ShopSection bondage = new ShopSection("Bondage");
		bondage.add(new AdultStoreShopItem(AdultStoreShopItem.Item.ROPE));
		bondage.add(new AdultStoreShopItem(AdultStoreShopItem.Item.HANDCUFFS));
		this.add(bondage);
		ShopSection pain = new ShopSection("Pain");
		pain.add(new AdultStoreShopItem(AdultStoreShopItem.Item.WHIP));
		pain.add(new AdultStoreShopItem(AdultStoreShopItem.Item.SHOCK_COLLAR));
		this.add(pain);
	}
}
