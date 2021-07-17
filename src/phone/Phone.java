package phone;

import java.awt.Color;
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
	private static final int SCREEN_X_POS = 5, SCREEN_Y_POS = 9;
	private static final int X_DISPLAY = Program.DISPLAY_WIDTH - PHONE_DISPLAY_WIDTH - 50,
							Y_DISPLAY = Program.DISPLAY_HEIGHT;
	public static final int SCREEN_WIDTH = 35, SCREEN_HEIGHT = 45;
	
	private static final BufferedImage phoneImage = ImageTools.getImage("assets/images/phone/phone.png");
	
	private BufferedImage image;
	private BufferedImage screenImage;
	
	private float openSlideTimer = 0f;
	private static final float slideTime = .75f;
	private boolean open = false;

	private Player player;
	
	private List<PhoneApp> apps;
	private PhoneApp currentApp; //the app that is currently open on the phone
	
	public Phone(Player player) {
		this.image = new BufferedImage(PHONE_WIDTH, PHONE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		this.screenImage = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		
		this.player = player;
		
		this.apps = new ArrayList<PhoneApp>();
		this.apps.add(new ContactsApp());
		this.apps.add(new EmailApp());
		this.apps.add(new MessagesApp());
		this.apps.add(new MapsApp());
		this.apps.add(new InternetApp());
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
			UI input = UICodex.get("phone");
			
			//draw stuff onto the phone
			Graphics2D pg = this.image.createGraphics();
			pg.drawImage(phoneImage,0,0,PHONE_WIDTH,PHONE_HEIGHT,null);
			
			if (MathUtils.colliding(mx, my, 19, 55, 6, 6))
				if (input.isMousePressed())
					this.currentApp = null;
			
			if (this.currentApp == null) { //draw the home screen
				//loop through each app
				for (int i = 0; i < this.apps.size(); i++) {
					int x = i % APPS_PER_ROW, y = i / APPS_PER_ROW;
					int px = SCREEN_X_POS + 2 + x * (APP_SIZE+3), py = SCREEN_Y_POS + 2 + y * (APP_SIZE+3);
					
					BufferedImage icon = this.apps.get(i).getIcon();
					if (MathUtils.colliding(mx, my, px, py, APP_SIZE, APP_SIZE)) {
						icon = ImageTools.darken(icon, 0.5f);
						if (input.isMousePressed())
							this.currentApp = this.apps.get(i);
					}
					
					pg.drawImage(icon,px,py,APP_SIZE,APP_SIZE,null);
				}
			} else {
				Graphics2D sg = this.screenImage.createGraphics();
				sg.setColor(new Color(0,0,0,0));
				sg.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
				this.currentApp.draw(sg); //draw the app then
				
				int rgb = Color.black.getRGB();
				this.screenImage.setRGB(0, 0, rgb);
				this.screenImage.setRGB(0, SCREEN_HEIGHT-1, rgb);
				this.screenImage.setRGB(SCREEN_WIDTH-1, 0, rgb);
				this.screenImage.setRGB(SCREEN_WIDTH-1, SCREEN_HEIGHT-1, rgb);
				
				pg.drawImage(this.screenImage,SCREEN_X_POS, SCREEN_Y_POS, SCREEN_WIDTH, SCREEN_HEIGHT, null);
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
		this.currentApp = null; //reset this
	}
}
