package phone;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public abstract class PhoneApp {
	private PhoneAppInfo info;
	
	public PhoneApp(PhoneAppInfo info) {
		this.info = info;
	}
	
	public BufferedImage getIcon() {
		return this.info.icon;
	}
	
	public String getName() {
		return this.info.displayName;
	}
	
	public String getID() {
		return this.info.id;
	}
	
	/**
	 * Draws the current state of this app to the specified phone
	 * @param g - the graphics object of the phone
	 */
	public abstract void draw(Graphics2D g);
}
