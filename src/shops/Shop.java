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
import main.Settings;
import misc.MathUtils;
import shops.items.ShopItem;

public class Shop {
	
	private String name;
	
	private List<ShopSection> sections;
	
	public Shop(String name) {
		this.sections = new ArrayList<ShopSection>();
		this.name = name;
	}
	
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
		UI input = UICodex.get("shop");
		int width = (Program.DISPLAY_WIDTH-margins*2-150)/itemsPerRow-10, height = 100;
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
				ShopItem item = section.getItems().get(j);
				g.setColor(Color.BLACK);
				if (Program.mouse.getX() > x && Program.mouse.getX() < x + width && Program.mouse.getY() > y && Program.mouse.getY() < y + height) {
					g.setColor(Color.GRAY);
					if (input.isMousePressed()) {
						if (item.attemptPurchase(player))
							g.setColor(Color.GREEN);
						else
							g.setColor(Color.RED);
					}
				}
				g.setStroke(new BasicStroke(6));
				g.drawRoundRect(x, y, width, height,6,6);
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
	
	public void add(ShopSection section) {
		this.sections.add(section);
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
		
		Shop weaponShop = new WeaponShop();
		shops.put("weapons_shop", weaponShop);
	}
	
	private static Player player;
	
	public static void openShop(String id, Player inPlayer) {
		currentShop = shops.get(id);
		player = inPlayer;
		UIController.setActiveUI("shop");
	}
	
	public static void closeShop() {
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
			if (input.keyPressed(KeyEvent.VK_ESCAPE) || input.keyPressed(Settings.getChar("interact"))) {
				closeShop();
				return;
			}
		}
	}
}
