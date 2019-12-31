package item.weapon;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import entities.player.Player;
import main.Program;

/**
 * The inventory type thing for weapons
 */
public class WeaponScroll {
	
	public static final int[] SCROLL_PRIMARY = {Gun.CATEGORY_RIFLE,Gun.CATEGORY_SUB_MACHINE_GUN,Gun.CATEGORY_SHOTGUN,Gun.CATEGORY_HEAVY_SPECIAL},
							  SCROLL_SECONDARY = {Gun.CATEGORY_HAND_GUN,Gun.CATEGORY_LIGHT_SPECIAL};
	
	private static final int MAXIMUM_NUMBER_OF_GUNS = 6;
	
	private Player player;
	
	private int selectedIndex = 0; //the selected gun
	
	private Weapon[] weapons;
	
	public WeaponScroll(Player player) {
		weapons = new Weapon[MAXIMUM_NUMBER_OF_GUNS];
		this.player = player;
	}
	
	public Weapon getSelectedWeapon() {
		return weapons[selectedIndex];
	}
	
	private int size = 350;
	
	public void draw(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillOval(Program.DISPLAY_WIDTH/2-size/2, Program.DISPLAY_HEIGHT/2-size/2, size, size);
		g.setColor(Color.GRAY);
		g.fillOval(Program.DISPLAY_WIDTH/2-size/2+10,Program.DISPLAY_HEIGHT/2-size/2+10,size-20,size-20);
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(5));
		for (int i = 0; i < weapons.length; i++) {
			double theta = (double)i/weapons.length*Math.PI*2;
			int radius = size/2-5;
			g.drawLine(Program.DISPLAY_WIDTH/2, Program.DISPLAY_HEIGHT/2, Program.DISPLAY_WIDTH/2+(int)(Math.cos(theta)*radius), Program.DISPLAY_HEIGHT/2+(int)(Math.sin(theta)*radius));
		}
	}
}
