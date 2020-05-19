package weapons;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.Serializable;

import entities.player.Player;
import main.Program;

/**
 * Contains player stuff like guns, knives, grenades, other misc weapons...
 */
public class WeaponManager implements Serializable {
	private static final long serialVersionUID = 1L;

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
		Weapon[] weapons = scrolls[0].getWeapons();
		weapons[0] = new Weapon(player,Weapon.Type.AK_47);
		weapons[1] = new Weapon(player,Weapon.Type.GLOCK_21);
		weapons[2] = new Weapon(player,Weapon.Type.DESERT_EAGLE);
		weapons[3] = new Weapon(player,Weapon.Type.REVOLVER);
//		weapons[4] = new Weapon(player,Weapon.Type.AK_47);
//		weapons[5] = new Weapon(player,Weapon.Type.GLOCK_21);
	}
	
	public Weapon getSelectedWeapon() {
		return scrolls[currentScroll].getSelectedWeapon();
	}
	
	public void update(float dt) {
		scrolls[currentScroll].update(dt);
	}
	
	/**
	 * Listens for keyboard input to determine whether or not to open the UI
	 */
	public void listen() {
		uiActive = Program.keyboard.keyDown(KeyEvent.VK_TAB);
		
		if (uiActive) {
			if (Program.keyboard.keyPressed(KeyEvent.VK_RIGHT))
				scrolls[currentScroll].addPosition();
			if (Program.keyboard.keyPressed(KeyEvent.VK_LEFT))
				scrolls[currentScroll].removePosition();
			if (Program.keyboard.keyPressed(KeyEvent.VK_SPACE))
				currentScroll = (currentScroll+1)%scrolls.length;
		}
	}
	
	public void draw(Graphics2D g) {
		if (uiActive) {
			scrolls[currentScroll].draw(g);
		}
	}
}
