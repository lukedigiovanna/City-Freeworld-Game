package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import display.Display;
import entities.Entity;
import entities.player.Player;
import main.Keyboard;
import main.Program;
import misc.Color8;
import misc.ImageTools;
import misc.MathUtils;
import weapons.Weapon;
import world.Camera;
import world.World;
import world.regions.Minimap;

/**
 * Runs by the client computer to draw
 * the world and game display.
 *
 */
public class GameDrawer {
	
	private Game game;
	
	private BufferedImage gameScreen;
	private BufferedImage pauseBackground;
	
	private Camera camera;
	
	private Minimap minimap;
	
	private Player player;
	
	public GameDrawer(Game game, Player player) {
		this.game = game;
		this.gameScreen = new BufferedImage(Program.DISPLAY_WIDTH,Program.DISPLAY_HEIGHT,BufferedImage.TYPE_INT_ARGB);
		this.player = player;
		
		this.minimap = new Minimap(player);
		
		float worldViewWidth = 26.0f;
		camera = new Camera(player, 0, 0, worldViewWidth, worldViewWidth/(CAMERA_PIXEL_WIDTH/(float)CAMERA_PIXEL_HEIGHT),CAMERA_PIXEL_WIDTH,CAMERA_PIXEL_HEIGHT);
	}
	
	private FrameTimer ft = new FrameTimer();
	
	private void updateCamera(float dt) {
		boolean gamePaused = game.isPaused();
		if (!gamePaused) {
			camera.linkToRegion(player.getRegion());
			camera.adjustPosition(dt);
		}
	}
	
	private Keyboard kbd;
	
	private void checkKeys() {
		if (kbd == null)
			kbd = new Keyboard(Program.panel);
		if (kbd.keyDown(KeyEvent.VK_CONTROL)) {
			if (kbd.keyPressed(KeyEvent.VK_H))
				camera.toggleHitboxes();
			if (kbd.keyPressed(KeyEvent.VK_W))
				camera.toggleWalls();
			if (kbd.keyPressed(KeyEvent.VK_F))
				camera.toggleFieldOfView();
			if (kbd.keyPressed(KeyEvent.VK_R))
				camera.toggleRoads();
		}
		
		if (kbd.keyDown('c'))
			camera.zoom(0.01f);
		if (kbd.keyDown('x'))
			camera.zoom(-0.01f);
	}
	
	/*
	 * rendering stuff
	 */
	
	private static int cameraBorderSize = 20;
	
	public static final float CAMERA_PERCENT_WIDTH = 1.0f, CAMERA_PERCENT_HEIGHT = 1.0f;
	public static final int CAMERA_PIXEL_WIDTH = (int)(CAMERA_PERCENT_WIDTH*Program.DISPLAY_WIDTH), CAMERA_PIXEL_HEIGHT = (int)(CAMERA_PERCENT_HEIGHT*Program.DISPLAY_HEIGHT);
	
	private float pauseTimer = 0;
	private BufferedImage pauseScreenBackgroundStill = null;
	public void draw(Graphics2D sg) {
		Graphics2D g = this.gameScreen.createGraphics();
		
		float dt = ft.mark();
		updateCamera(dt);
		
		if (game.isPaused()) {
			if (pauseTimer == 0)
				pauseScreenBackgroundStill = ImageTools.toBufferedImage(gameScreen);
			pauseTimer+=dt;
			//darken the screen
			if (pauseTimer < 0.75f)
				pauseBackground = ImageTools.darken(pauseScreenBackgroundStill, 1-pauseTimer/1.0f);
			g.drawImage(pauseBackground, 0, 0, CAMERA_PIXEL_WIDTH, CAMERA_PIXEL_HEIGHT, null);
			if (pauseTimer > 0.75) {
				g.setColor(Color.RED);
				g.setFont(new Font(Program.FONT_FAMILY,Font.BOLD,Program.DISPLAY_HEIGHT/10));
				Display.drawText(g, "PAUSED", 0.5f, 0.4f, Display.CENTER_ALIGN);
				for (PauseButton b : pButs) {
					b.check();
					b.draw(g);
				}
			}
		} else {
			pauseTimer = 0;
			
			checkKeys();
			
			pauseBackground = null;
			
			World world = game.getWorld();
			
			camera.draw();	
			BufferedImage cameraView = camera.getView();
			
			g.drawImage(cameraView, 0, 0, CAMERA_PIXEL_WIDTH, CAMERA_PIXEL_HEIGHT, null);
		
			g.setColor(Color8.GRAY);
			g.setStroke(new BasicStroke(cameraBorderSize*2));
			g.drawRect(0, 0, CAMERA_PIXEL_WIDTH, CAMERA_PIXEL_HEIGHT);
			
			//draw the profile bar
			int cameraHeight = CAMERA_PIXEL_HEIGHT-150-cameraBorderSize;
			int barHeight = 150, barWidth = 340;
			float padding = 0.1f;
			int pixelPadding = (int)(padding*barHeight);
			int profileHeight = barHeight - pixelPadding * 2;
			int ppX = cameraBorderSize+pixelPadding, ppY = cameraHeight+pixelPadding,
					ppS = (int)(profileHeight);
			g.setColor(new Color(0,0,0,175));
			g.fillRect(cameraBorderSize, cameraHeight, barWidth, barHeight);
			int[] xPoints = {barWidth+cameraBorderSize, barWidth+cameraBorderSize, barWidth+cameraBorderSize+150};
			int[] yPoints = {Program.DISPLAY_HEIGHT-barHeight-cameraBorderSize,Program.DISPLAY_HEIGHT-cameraBorderSize,Program.DISPLAY_HEIGHT-cameraBorderSize};
			g.fillPolygon(xPoints,yPoints,3);
			g.setStroke(new BasicStroke(cameraBorderSize/2,BasicStroke.CAP_ROUND,BasicStroke.JOIN_BEVEL));
			int topY = Program.DISPLAY_HEIGHT-barHeight-cameraBorderSize;
			g.setColor(Color.GRAY);
			g.drawLine(cameraBorderSize, topY, cameraBorderSize+barWidth, topY);
			g.drawLine(cameraBorderSize+barWidth, topY, xPoints[2], yPoints[2]);
			
			g.setColor(Color.GRAY);
			g.fillRect(ppX-2, ppY-2, ppS+4, ppS+4);
			g.drawImage(player.getProfilePicture(), ppX, ppY, ppS, ppS, null);
				

			this.drawSplicedImage(g, BORDER, ppX+ppS+pixelPadding, ppY+ppS-20, (int)(barWidth - ppS - pixelPadding * 2), 20, 0f, 0f);
			this.drawSplicedImage(g, GREEN_BAR, ppX+ppS+pixelPadding, ppY+ppS-20, (int)(barWidth - ppS - pixelPadding * 2), 20, 0, 1-player.getHealth().getPercent());
			this.drawSplicedImage(g, RED_BAR, ppX+ppS+pixelPadding, ppY+ppS-20, (int)(barWidth - ppS - pixelPadding * 2), 20, 0, 1-player.getHealth().getDisplayPercent());
			//this.drawSplicedImage(g, YELLOW_BAR, ppX+ppS+pixelPadding, ppY+ppS-20, (int)(barWidth -ppS - pixelPadding * 2), 20, player.getHealth().getPercent(), 1 - player.getHealth().getDisplayPercent());
//			g.setColor(Color.GREEN);
//			g.fillRect(ppX+ppS+pixelPadding, ppY+ppS-20, (int)((barWidth - ppS - pixelPadding * 2) * player.getHealth().getPercent()), 20);
//			g.setColor(Color.RED);
//			g.fillRect(ppX+ppS+pixelPadding, ppY+ppS-20, (int)((barWidth - ppS - pixelPadding * 2) * player.getHealth().getDisplayPercent()), 20);
//			g.setColor(Color.YELLOW);
//			g.fillRect(ppX+ppS+pixelPadding+(int)((barWidth - ppS - pixelPadding * 2) * player.getHealth().getPercent()), ppY+ppS-20, (int)((barWidth - ppS - pixelPadding * 2) * (player.getHealth().getDisplayPercent() - player.getHealth().getPercent())), 20);
			
			g.setColor(Color.WHITE);
			g.setFont(new Font(Program.FONT_FAMILY,Font.BOLD,20));
			g.drawString(player.getMoneyDisplay(),ppX+ppS+pixelPadding, ppY+ppS-40);
			
			int weaponSpace = 180;
			Weapon selected = player.getSelectedWeapon();
			if (selected != null) {
				int iconWidth = 90,
					iconHeight = iconWidth * Weapon.ICON_HEIGHT / Weapon.ICON_WIDTH;
				g.drawImage(selected.getType().icon, Program.DISPLAY_WIDTH - weaponSpace, Program.DISPLAY_HEIGHT-cameraBorderSize-10-iconHeight,iconWidth,iconHeight,null);
				String ammo = selected.getLoadedAmmo()+"/"+selected.getAmmoStock();
				g.setFont(new Font(Program.FONT_FAMILY,Font.BOLD | Font.ITALIC,20));
				g.setColor(Color.BLACK);
				int ax = Program.DISPLAY_WIDTH-cameraBorderSize-10-g.getFontMetrics().stringWidth(ammo),
					ay = Program.DISPLAY_HEIGHT-cameraBorderSize-10-iconHeight/2+10;
				int aSize = 1;
				g.drawString(ammo, ax-aSize, ay-aSize);
				g.drawString(ammo, ax-aSize, ay+aSize);
				g.drawString(ammo, ax+aSize, ay-aSize);
				g.drawString(ammo, ax+aSize, ay+aSize);
				g.setColor(Color.LIGHT_GRAY);
				g.drawString(ammo, ax, ay);
				int reloadBarWidth = g.getFontMetrics().stringWidth(ammo), reloadBarHeight = 5;
				g.setColor(Color.WHITE);
				g.fillRect(ax,ay+6,(int)(reloadBarWidth*selected.getReloadPercent()),reloadBarHeight);
			}
			
			minimap.draw();
			g.drawImage(minimap.getImage(), 0,  0, 250, 250, null);
			
			String time = world.getStringTime();
			g.setColor(Color.WHITE);
			g.setFont(new Font(Program.FONT_FAMILY, Font.BOLD | Font.ITALIC, 14));
			g.drawString(time, 125-g.getFontMetrics().stringWidth(time)/2, 270);
			
			player.getWeaponManager().draw(g);
		}
		
		sg.drawImage(gameScreen, 0, 0, Program.DISPLAY_WIDTH, Program.DISPLAY_HEIGHT, null);
		
		String[] info = {
				"TPS: "+(int)game.getTPS(),
				"TICK WAIT: "+(int)game.getWait()+"ms",
				"TICK EFFICIENCY: "+(int)((float)game.getWait()/Game.IDEAL_REFRESH_RATE*100)+"%",
				"MEM: "+Program.getUsedMemory()+" MB",
		};
		
		sg.setColor(Color.WHITE);
		sg.setFont(new Font(Program.FONT_FAMILY,Font.BOLD,18));
		for (int i = 0; i < info.length; i++) {
			String s = info[i];
			sg.drawString(s, Program.DISPLAY_WIDTH-10-sg.getFontMetrics().stringWidth(s), 40+20*i);
		}
	}
	
	private static final BufferedImage 
		BORDER = ImageTools.getImage("health_bar/border.png"),
		RED_BAR = ImageTools.getImage("health_bar/red_bar.png"),
		GREEN_BAR = ImageTools.getImage("health_bar/green_bar.png"),
		YELLOW_BAR = ImageTools.getImage("health_bar/yellow_bar.png");
	private void drawSplicedImage(Graphics2D g, BufferedImage image, int x, int y, int width, int height, float leftPercent, float rightPercent) {
		leftPercent = MathUtils.clip(0, 1, leftPercent);
		rightPercent = MathUtils.clip(0, 1, rightPercent);
		BufferedImage newImg = ImageTools.spliceHorizontal(image, leftPercent, rightPercent);
		float widthPercent = (float)newImg.getWidth()/image.getWidth();
		int margin = (int)(1f/image.getWidth()*width*(int)((image.getWidth() * leftPercent)));
		g.drawImage(newImg, x + margin, y, (int)(width * widthPercent), height, null);
	}
	
	private abstract class PauseButton extends display.component.Button {

		public PauseButton(String s, int y) {
			super(s, Program.DISPLAY_WIDTH/2, y, 0, 30, display.component.Component.FORM_CENTER);
		}

		private Color c = Color.cyan;
		
		@Override
		public void draw(Graphics2D g) {
			g.setFont(new Font(Program.FONT_FAMILY,Font.BOLD,getHeight()));
			setWidth(g.getFontMetrics().stringWidth(this.getText()));
			g.setColor(c);
			g.drawString(getText(), getX(), getY()+getHeight()); 
		}
		
		@Override
		public void onMouseDown() { c = Color.gray; }
		@Override
		public void onMouseOver() { c = Color.white; }
		@Override
		public void onMouseOut() { c = Color.cyan; }
	}
	
	private PauseButton[] pButs = { new PauseButton("RESUME",Program.DISPLAY_HEIGHT/2) {
		public void onMouseUp() {
			game.unpause();
		}
	}, /*new PauseButton("SAVE",Program.DISPLAY_HEIGHT/2+30) {
		public void onMouseUp() {
			game.getWorld().save();
		}
	},*/ /*new PauseButton("SAVE AND QUIT",Program.DISPLAY_HEIGHT/2+60) {
		public void onMouseUp() {
			game.getWorld().save();
			game.quit();
			
			display.DisplayController.setScreen(display.DisplayController.Screen.MAIN);
		}
	}, */new PauseButton("QUIT",Program.DISPLAY_HEIGHT/2+30) {
		public void onMouseUp() {
			game.quit();
			display.DisplayController.setScreen(display.DisplayController.Screen.MAIN);
		}
	}};
}
