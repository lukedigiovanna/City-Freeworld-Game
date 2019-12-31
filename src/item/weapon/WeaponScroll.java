package item.weapon;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import entities.player.Player;
import main.Program;

/**
 * The inventory type thing for weapons
 */
public class WeaponScroll {
	
	public static final int[] SCROLL_PRIMARY = {Weapon.CATEGORY_RIFLE,Weapon.CATEGORY_SUB_MACHINE_GUN,Weapon.CATEGORY_SHOT_GUN,Weapon.CATEGORY_HEAVY_SPECIAL},
							  SCROLL_SECONDARY = {Weapon.CATEGORY_HAND_GUN,Weapon.CATEGORY_LIGHT_SPECIAL};
	
	private static final int MAXIMUM_NUMBER_OF_WeaponS = 6;
	
	private Player player;
	
	private int selectedIndex = 0; //the selected Weapon
	
	private Weapon[] weapons;
	
	public WeaponScroll(Player player) {
		weapons = new Weapon[MAXIMUM_NUMBER_OF_WeaponS];
		this.player = player;
	}
	
	public Weapon[] getWeapons() {
		return this.weapons;
	}
	
	public Weapon getSelectedWeapon() {
		return weapons[selectedIndex];
	}
	
	public void addPosition() {
		selectedIndex = (selectedIndex+1)%weapons.length;
	}
	
	public void removePosition() {
		selectedIndex--;
		if (selectedIndex < 0)
			selectedIndex = weapons.length-1;
	}
	
	public void update(float dt) {
		if (getSelectedWeapon() != null)
			getSelectedWeapon().update(dt);
	}
	
	private int size = 400;
	
	public void draw(Graphics2D g) {
		g.setColor(Color.GRAY);
		g.fillOval(Program.DISPLAY_WIDTH/2-size/2,Program.DISPLAY_HEIGHT/2-size/2,size,size);
		g.setStroke(new BasicStroke(5));
		//highlight the selected weapon
		double selAngle = (double)selectedIndex/weapons.length*Math.PI*2;
		g.setColor(new Color(0,255,255,100));
		g.fillArc(Program.DISPLAY_WIDTH/2-size/2, Program.DISPLAY_HEIGHT/2-size/2, size, size, 360-(int)Math.toDegrees(selAngle), -(int)Math.toDegrees(1.0/weapons.length*Math.PI*2)+1);
		for (int i = 0; i < weapons.length; i++) {
			double theta = (double)i/weapons.length*Math.PI*2;
			int radius = size/2-5;
			g.setColor(Color.BLACK);
			g.drawLine(Program.DISPLAY_WIDTH/2, Program.DISPLAY_HEIGHT/2, Program.DISPLAY_WIDTH/2+(int)(Math.cos(theta)*radius), Program.DISPLAY_HEIGHT/2+(int)(Math.sin(theta)*radius));
			if (weapons[i] == null)
				continue;
			theta += (1.0/weapons.length*Math.PI);
			int iconWidth = size/3;
			int iconHeight = iconWidth * Weapon.ICON_HEIGHT / Weapon.ICON_WIDTH;
			double distance = size/2 * 0.6;
			int x = (int) (Program.DISPLAY_WIDTH/2+Math.cos(theta) * distance - iconWidth/2), y = (int) (Program.DISPLAY_HEIGHT/2+Math.sin(theta) * distance - iconHeight/2);
			g.drawImage(weapons[i].getType().icon, x, y, iconWidth, iconHeight, null);
			String name = weapons[i].getType().name;
			g.setFont(new Font(Program.FONT_FAMILY,Font.ITALIC | Font.BOLD,14));
			g.drawString(name, x+iconWidth/2-g.getFontMetrics().stringWidth(name)/2, y-20);
			String ammo = weapons[i].getLoadedAmmo()+"/"+weapons[i].getAmmoStock();
			g.drawString(ammo, x+iconWidth/2-g.getFontMetrics().stringWidth(ammo)/2, y+iconHeight+16);
		}
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(14));
		g.drawOval(Program.DISPLAY_WIDTH/2-size/2, Program.DISPLAY_HEIGHT/2-size/2, size, size);
	}
}
