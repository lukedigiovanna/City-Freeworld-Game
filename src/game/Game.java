package game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.List;

import display.*;
import entities.player.Player;
import weapons.Weapon;
import main.*;
import misc.*;
import soundEngine.Sound;
import world.*;

public class Game {
	
	private static final int TICKS_PER_SECOND = 20;
	private static final int IDEAL_REFRESH_RATE = 1000/TICKS_PER_SECOND;
	private long wait = IDEAL_REFRESH_RATE; //default
	
	private Thread updateLoop;
	private float elapsedTime = 0.0f;
	private FrameTimer ft;
	private World world;
	
	private boolean paused = true;
	
	public Game() {
		ft = new FrameTimer();
		world = new World(this);
		
		updateLoop = new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						long before = System.currentTimeMillis();
						if (Program.initialized() && DisplayController.getCurrentScreen() == DisplayController.Screen.GAME)
							gameLoop();
						else 
							ft.mark(); //keep the frame timer going so we dont add time that we weren't on the game screen
						long elapsed = System.currentTimeMillis()-before;
						wait = IDEAL_REFRESH_RATE-elapsed;
						Thread.sleep((long)MathUtils.floor(0, wait-1));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		updateLoop.start();
	}
	
	public Vector2 getMousePositionOnCamera() {
		int mx = Program.mouse.getX()-cameraBorderSize; //where is the x on the drawn camera
		float perc = mx/CAMERA_PIXEL_WIDTH;
		if (perc > 1)
			mx = cameraBorderSize+CAMERA_PIXEL_WIDTH;
		else if (perc < 0)
			mx = cameraBorderSize;
		int my = Program.mouse.getY()-cameraBorderSize;
		perc = my/CAMERA_PIXEL_HEIGHT;
		if (perc > 1)
			my = cameraBorderSize+CAMERA_PIXEL_HEIGHT;
		else if (perc < 0)
			my = cameraBorderSize;
		return new Vector2(mx,my);
	}
	
	public Vector2 getPercentMousePositionOnCamera() {
		Vector2 onCam = getMousePositionOnCamera();
		return new Vector2(onCam.x/(float)CAMERA_PIXEL_WIDTH,onCam.y/(float)CAMERA_PIXEL_HEIGHT);
	}
	
	public void pause() {
		paused = true;
	}
	
	public void unpause() {
		paused = false;
	}
	
	public void togglePause() {
		paused = !paused;
	}
	
	public boolean isPaused() {
		return this.paused;
	}
	
	private float tps = 0.0f, elapsedSinceLastCapture = 0.0f;
	private int captures = 0;
	
	private Sound sound = new Sound("assets/sounds/music/song1.wav");

	public void gameLoop() {
		//lets check for pausing
		if (Program.keyboard.keyPressed(KeyEvent.VK_ESCAPE))
			togglePause();
		
		//elapsed time since last loop call.. for regulating game operation speeds
		//across varying operating system performances
		float dt = ft.mark();
		
		if (this.paused) {
			sound.pause();
			return; //dont run the game loop if we are paused
		}
		
		if (Program.keyboard.keyDown(KeyEvent.VK_CONTROL)) {
			if (Program.keyboard.keyPressed(KeyEvent.VK_H))
				this.world.getCamera().toggleHitboxes();
			if (Program.keyboard.keyPressed(KeyEvent.VK_W))
				this.world.getCamera().toggleWalls();
		}
		
		
		if (Settings.getSetting("master_volume").contentEquals("1.0"))
			sound.loop();
		
		world.update(dt);
		
		if (Program.keyboard.keyDown('c'))
			world.getCamera().zoom(0.01f);
		if (Program.keyboard.keyDown('x'))
			world.getCamera().zoom(-0.01f);

		if (Program.keyboard.keyPressed('r'))
			rotate = !rotate;
		
		if (rotate)
			world.getCurrentRegion().getGrid().get((int)(Math.random()*10), (int)(Math.random()*10)).rotate(dt*(float)Math.PI*2);
	
		captures++;
		elapsedSinceLastCapture+=dt;
		if (captures >= 5) {
			tps = 5.0f/elapsedSinceLastCapture;
			captures = 0;
			elapsedSinceLastCapture = 0;
		}
		
		elapsedTime+=dt;
	}
	
	private boolean rotate = false;
	
	
	/*
	 * rendering stuff
	 */
	
	private static int cameraBorderSize = 20;
	
	public static final float CAMERA_PERCENT_WIDTH = 1.0f, CAMERA_PERCENT_HEIGHT = 1.0f;
	public static final int CAMERA_PIXEL_WIDTH = (int)(CAMERA_PERCENT_WIDTH*Program.DISPLAY_WIDTH), CAMERA_PIXEL_HEIGHT = (int)(CAMERA_PERCENT_HEIGHT*Program.DISPLAY_HEIGHT);
	
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
			paused = false;
		}
	}, new PauseButton("QUIT",Program.DISPLAY_HEIGHT/2+40) {
		public void onMouseUp() {
			display.DisplayController.setScreen(display.DisplayController.Screen.MAIN);
		}
	}};
	
	public void draw(Graphics2D g) {
		if (!paused)
			world.draw();	
		BufferedImage cameraView = world.getCamera().getView();

		if (paused) {
			//make the game gray scaled
			g.drawImage(ImageTools.colorscale(cameraView,Color.WHITE), 0, 0, CAMERA_PIXEL_WIDTH, CAMERA_PIXEL_HEIGHT, null);
			g.setColor(Color.RED);
			g.setFont(new Font(Program.FONT_FAMILY,Font.BOLD,Program.DISPLAY_HEIGHT/10));
			Display.drawText(g, "PAUSED", 0.5f, 0.4f, Display.CENTER_ALIGN);
			for (PauseButton b : pButs) {
				b.check();
				b.draw(g);
			}
		} else {
			g.drawImage(cameraView, 0, 0, CAMERA_PIXEL_WIDTH, CAMERA_PIXEL_HEIGHT, null);
		}
		
		g.setColor(Color8.GRAY);
		g.setStroke(new BasicStroke(cameraBorderSize*2));
		g.drawRect(0, 0, CAMERA_PIXEL_WIDTH, CAMERA_PIXEL_HEIGHT);
		
		//draw the profile bar
		List<Player> players = this.world.getPlayers();
		if (players.size() > 0) {
			Player player = (Player) this.world.getPlayers().get(0);
			int cameraHeight = CAMERA_PIXEL_HEIGHT-150-cameraBorderSize;
			int profileHeight = Program.DISPLAY_HEIGHT-cameraHeight;
			int barHeight = 150, barWidth = 340;
			float padding = 0.15f;
			int pixelPadding = (int)(padding*profileHeight);
			int ppX = cameraBorderSize+pixelPadding, ppY = cameraHeight+pixelPadding,
					ppS = (int)((1-padding*2)*profileHeight);
			g.setColor(new Color(0,0,0,175));
			g.fillRect(cameraBorderSize, cameraHeight, barWidth, barHeight);
			int[] xPoints = {barWidth+cameraBorderSize, barWidth+cameraBorderSize, barWidth+cameraBorderSize+150};
			int[] yPoints = {Program.DISPLAY_HEIGHT-barHeight-cameraBorderSize,Program.DISPLAY_HEIGHT-cameraBorderSize,Program.DISPLAY_HEIGHT-cameraBorderSize};
			g.fillPolygon(xPoints,yPoints,3);
			g.setStroke(new BasicStroke(cameraBorderSize/2));
			int topY = Program.DISPLAY_HEIGHT-barHeight-cameraBorderSize;
			g.setColor(Color.GRAY);
			g.drawLine(cameraBorderSize, topY, cameraBorderSize+barWidth, topY);
			g.drawLine(cameraBorderSize+barWidth, topY, xPoints[2], yPoints[2]);
			
			g.setColor(Color.GRAY);
			g.fillRect(ppX-2, ppY-2, ppS+4, ppS+4);
			g.drawImage(player.getProfilePicture(), ppX, ppY, ppS, ppS, null);
			String[] info = {
				"Name: "+player.getName(),
				"Money: "+player.getMoneyDisplay(),
				"Reputation: "+player.getReputation()
			};
			Color[] colors = {
				Color.LIGHT_GRAY,
				Color.GREEN,
				Color.BLUE
			};
			int y = ppY;
			g.setFont(new Font(Program.FONT_FAMILY,Font.BOLD,ppS/5));
			int si = g.getFontMetrics().getHeight();
			int add = 4;
			for (int i = 0; i < info.length; i++) {
				g.setColor(colors[i]);
				g.drawString(info[i], ppX+ppS+10, y+i*(si+add)+si);
			}
			
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
			if (!paused)
				player.getWeaponManager().draw(g);
		}
		
		g.setColor(Color.WHITE);
		g.setFont(new Font(Program.FONT_FAMILY,Font.BOLD,18));
		String s = "TPS: "+(int)tps;
		g.drawString(s, Program.DISPLAY_WIDTH-10-g.getFontMetrics().stringWidth(s), 40);
	}
}
