package item.weapon;

import java.awt.Graphics2D;

import entities.player.Player;
import main.Program;

/**
 * Contains player stuff like guns, knives, grenades, other misc weapons...
 */
public class WeaponManager {
	private int numOfScrolls = 2;
	private WeaponScroll[] scrolls;
	
	private int currentScroll = 0;
	
	private Player player;
	
	private boolean uiActive = false;
	
	public WeaponManager(Player player) {
		this.player = player;
		scrolls = new WeaponScroll[numOfScrolls];
		for (int i = 0; i < scrolls.length; i++)
			scrolls[i] = new WeaponScroll(player);
	}
	
	public Weapon getSelectedWeapon() {
		return scrolls[currentScroll].getSelectedWeapon();
	}
	
	/**
	 * Listens for keyboard input to determine whether or not to open the UI
	 */
	public void listen() {
		uiActive = Program.keyboard.keyDown('q');
	}
	
	public void draw(Graphics2D g) {
		if (uiActive) {
			scrolls[currentScroll].draw(g);
		}
	}
}
