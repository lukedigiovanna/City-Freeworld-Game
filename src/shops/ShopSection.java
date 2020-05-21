package shops;

import java.util.ArrayList;
import java.util.List;

import shops.items.ShopItem;

public class ShopSection {
	private String name;
	private List<ShopItem> items;
	
	public ShopSection(String name) {
		this.items = new ArrayList<ShopItem>();
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public List<ShopItem> getItems() {
		return this.items;
	}
	
	public void add(ShopItem item) {
		this.items.add(item);
	}
}
