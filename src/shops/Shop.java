package shops;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import display.ui.UI;
import display.ui.UICodex;
import display.ui.UIController;
import entities.player.Player;
import main.Program;
import misc.MathUtils;
import shops.items.ShopItem;

public class Shop {
	
	private String name;
	
	private List<ShopSection> sections;
	
	public Shop(String name) {
		this.sections = new ArrayList<ShopSection>();
		this.name = name;
	}
	
	private int selectedIndex = 0;
	
	private static int itemsPerRow = 5;
	public void draw(Graphics2D g) {
		g.setColor(Color.LIGHT_GRAY);
		int margins = 100;
		g.fillRoundRect(margins, margins, Program.DISPLAY_WIDTH-margins*2, Program.DISPLAY_HEIGHT-margins*2, 25, 25);
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(25));
		g.drawRoundRect(margins, margins, Program.DISPLAY_WIDTH-margins*2, Program.DISPLAY_HEIGHT-margins*2, 25, 25);
		g.setColor(Color.BLACK);
		g.setFont(new Font(Program.FONT_FAMILY,Font.BOLD | Font.ITALIC, 28));
		int y = margins+56;
		g.drawString(this.name, Program.DISPLAY_WIDTH/2-g.getFontMetrics().stringWidth(this.name)/2, y);
		y+=30;
		g.setColor(Color.DARK_GRAY);
		g.setFont(new Font(Program.FONT_FAMILY,Font.ITALIC,22));
		String s = "Balance: $"+MathUtils.pad(player.getBankAccount().getMoney(),0,2);
		g.drawString(s, Program.DISPLAY_WIDTH/2-g.getFontMetrics().stringWidth(s)/2, y);
		y+=26;
		int width = (Program.DISPLAY_WIDTH-margins*2-150)/itemsPerRow-10, height = 100;
		int index = 0;
		for (int i = 0; i < this.sections.size(); i++) {
			g.setColor(Color.BLACK);
			g.setFont(new Font(Program.FONT_FAMILY,Font.BOLD,18));
			ShopSection section = this.sections.get(i);
			s = section.getName();
			g.drawString(s, margins+50, y);
			y+=20;
			int x = margins+75;
			int count = 0;
			g.setFont(new Font(Program.FONT_FAMILY,Font.BOLD,10));
			for (int j = 0; j < section.getItems().size(); j++) {
				g.setColor(Color.BLACK);
				if (index == this.selectedIndex)
					g.setColor(Color.CYAN);
				g.setStroke(new BasicStroke(6));
				g.drawRoundRect(x, y, width, height,6,6);
				ShopItem item = section.getItems().get(j);
				g.drawImage(item.getIcon(),x+10,y+10,width-20,height-35,null);
				s = item.getName()+" / $"+item.getPrice();
				g.setColor(Color.DARK_GRAY);
				g.drawString(s, x+width/2-g.getFontMetrics().stringWidth(s)/2, y+height-12);
				
				x+=width+10;
				count++;
				if (count >= itemsPerRow) {
					count = 0;
					x = margins + 75;
					y+=height+10;
				}
				index++;
			}
			y+=height;
			y+=26;
		}
	}
	
	public int getNumberOfItems() {
		int count = 0;
		for (ShopSection section : sections) {
			count+=section.getItems().size();
		}
		return count;
	}
	
	public ShopItem getSelectedItem() {
		int index = this.selectedIndex;
		for (ShopSection section : sections) {
			if (index < section.getItems().size())
				return section.getItems().get(index);
			else {
				index -= section.getItems().size();
			}
		}
		return null;
	}
	
	private static Map<String,Shop> shops;
	
	private static Shop currentShop;
	
	public static void initialize() {
		//first initialize the shop items
		ShopItem.initialize();
		
		shops = new HashMap<String,Shop>();
		
		/*
		 * Set up the weapons shop
		 */
		
		Shop weaponShop = new Shop("Guns 'n Stuff");
		ShopSection pistols = new ShopSection("Handguns");
		pistols.add(ShopItem.get("glock21"));
		pistols.add(ShopItem.get("desert_eagle"));
		pistols.add(ShopItem.get("revolver"));
		pistols.add(ShopItem.get("revolver"));
		pistols.add(ShopItem.get("revolver"));
		pistols.add(ShopItem.get("revolver"));
		pistols.add(ShopItem.get("revolver"));
		pistols.add(ShopItem.get("revolver"));
		pistols.add(ShopItem.get("revolver"));
		pistols.add(ShopItem.get("revolver"));
		pistols.add(ShopItem.get("revolver"));
		weaponShop.sections.add(pistols);
		ShopSection rifles = new ShopSection("Rifles");
		rifles.add(ShopItem.get("ak47"));
		weaponShop.sections.add(rifles);
		
		shops.put("weapon_shop", weaponShop);
	}
	
	private static Player player;
	
	public static void openShop(String id, Player inPlayer) {
		currentShop = shops.get(id);
		player = inPlayer;
		UIController.setActiveUI("shop");
	}
	
	public static void closeShop() {
		currentShop.selectedIndex = 0;
		currentShop = null;
		UIController.setDefault();
	}
	
	/**
	 * Draws the current shop, if one is active
	 * @param g
	 */
	public static void drawShop(Graphics2D g) {
		if (currentShop != null) {
			currentShop.draw(g);
		}
	}
	
	public static void checkInput() {
		if (currentShop != null) {
			UI input = UICodex.get("shop");
			if (input.keyPressed(KeyEvent.VK_ESCAPE)) {
				closeShop();
				return;
			}
			if (input.keyPressed(KeyEvent.VK_LEFT)) {
				currentShop.selectedIndex--;
			}
			if (input.keyPressed(KeyEvent.VK_RIGHT)) {
				currentShop.selectedIndex++;
			}
			//this is broken... wont work with sections of differing sizes
//			if (input.keyPressed(KeyEvent.VK_UP)) {
//				if (currentShop.selectedIndex-itemsPerRow >= 0)
//					currentShop.selectedIndex-=itemsPerRow;
//			}
//			if (input.keyPressed(KeyEvent.VK_DOWN)) {
//				if (currentShop.selectedIndex+itemsPerRow < currentShop.getNumberOfItems()-1)
//					currentShop.selectedIndex+=itemsPerRow;
//			}
			currentShop.selectedIndex = MathUtils.clip(0, currentShop.getNumberOfItems()-1, currentShop.selectedIndex);
			if (input.keyPressed(KeyEvent.VK_ENTER)) {
				currentShop.getSelectedItem().attemptPurchase(player);
			}
		}
	}
}
