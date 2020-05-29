package phone;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import display.ui.UI;
import display.ui.UICodex;
import display.ui.UIController;
import entities.player.Player;
import main.Program;
import main.Settings;
import misc.ImageTools;
import misc.MathUtils;

public class Phone {
	private static final int PHONE_WIDTH = 44, PHONE_HEIGHT = 64;
	private static final int PHONE_SCALE = 6, PHONE_DISPLAY_WIDTH = PHONE_WIDTH * PHONE_SCALE, PHONE_DISPLAY_HEIGHT = PHONE_HEIGHT * PHONE_SCALE;
	private static final int APPS_PER_ROW = 3;
	private static final int APP_SIZE = 8;
	private static final int APP_X_POS = 7, APP_Y_POS = 11;
	private static final int X_DISPLAY = Program.DISPLAY_WIDTH - PHONE_DISPLAY_WIDTH - 50,
							Y_DISPLAY = Program.DISPLAY_HEIGHT;
	
	private static final BufferedImage phoneImage = ImageTools.getImage("assets/images/phone/phone.png");
	
	private BufferedImage image;
	
	private float openSlideTimer = 0f;
	private static final float slideTime = .75f;
	private boolean open = false;

	private Player player;
	
	private List<PhoneApp> apps;
	private PhoneApp currentApp; //the app that is currently open on the phone
	
	public Phone(Player player) {
		this.image = new BufferedImage(PHONE_WIDTH, PHONE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		
		this.player = player;
		
		this.apps = new ArrayList<PhoneApp>();
		this.apps.add(new ContactsApp());
		this.apps.add(new ContactsApp());
		this.apps.add(new ContactsApp());
		this.apps.add(new ContactsApp());
		this.apps.add(new ContactsApp());
		this.apps.add(new ContactsApp());
		this.apps.add(new ContactsApp());
		this.apps.add(new ContactsApp());
		this.apps.add(new ContactsApp());
	}
	
	public void draw(Graphics2D g) {
		if (this.openSlideTimer > 0) {
			double expPercent;
			if (this.open)
				expPercent = Math.sqrt(this.openSlideTimer/slideTime);
			else
				expPercent = 1-Math.sqrt(1-this.openSlideTimer/slideTime);
			int slideAmount = (int)(expPercent * (PHONE_DISPLAY_HEIGHT + 50));
			
			int drawX = X_DISPLAY, drawY = Y_DISPLAY-slideAmount;
			
			int mx = (Program.mouse.getX() - drawX)/PHONE_SCALE, my = (Program.mouse.getY() - drawY)/PHONE_SCALE; //mouse position relative to the phone image
			
			//draw stuff onto the phone
			Graphics2D pg = this.image.createGraphics();
			pg.drawImage(phoneImage,0,0,PHONE_WIDTH,PHONE_HEIGHT,null);
			
			if (this.currentApp == null) { //draw the home screen
				//loop through each app
				for (int i = 0; i < this.apps.size(); i++) {
					int x = i % APPS_PER_ROW, y = i / APPS_PER_ROW;
					int px = APP_X_POS + x * (APP_SIZE+3), py = APP_Y_POS + y * (APP_SIZE+3);
					
					BufferedImage icon = this.apps.get(i).getIcon();
					if (mx >= px && mx < px + APP_SIZE && my >= py && my < py + APP_SIZE)
						icon = ImageTools.darken(icon, 0.5f);
					pg.drawImage(icon,px,py,APP_SIZE,APP_SIZE,null);
				}
			} else {
				this.currentApp.draw(pg); //draw the app then
			}
			
			g.drawImage(this.image, drawX, drawY, PHONE_DISPLAY_WIDTH, PHONE_DISPLAY_HEIGHT, null);
		}
	}
	
	public void update(float dt) {
		if (open)
			this.openSlideTimer+=dt;
		else if (this.openSlideTimer > 0) 
			this.openSlideTimer-=dt;
		this.openSlideTimer = MathUtils.clip(0, slideTime, this.openSlideTimer);
		UI input = UICodex.get("phone");
		if (input.keyPressed(Settings.getChar("toggle_phone")) || input.keyPressed(KeyEvent.VK_ESCAPE))
			this.close();
	}
	
	/**
	 * Opens up the UI for the phone
	 */
	public void open() {
		UIController.setActiveUI("phone");
		this.open = true;
		this.openSlideTimer = 0f;
	}
	
	/**
	 * Closes the UI for the phone
	 */
	public void close() {
		UIController.setDefault();
		this.open = false;
	}
}
