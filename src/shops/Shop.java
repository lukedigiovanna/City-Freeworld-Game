package shops;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import main.Program;

public abstract class Shop {
	public static enum Type {
		WEAPONS_SHOP, CAR_SHOP;
	}
	
	private String name;
	
	public Shop() {
		
	}
	
	public void draw(Graphics2D g) {
		g.setColor(Color.LIGHT_GRAY);
		int margins = 100;
		g.fillRoundRect(margins, margins, Program.DISPLAY_WIDTH-margins*2, Program.DISPLAY_HEIGHT-margins*2, 25, 25);
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(25));
		g.drawRoundRect(margins, margins, Program.DISPLAY_WIDTH-margins*2, Program.DISPLAY_HEIGHT-margins*2, 25, 25);
	}
}
