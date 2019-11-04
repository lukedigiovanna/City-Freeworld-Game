package game;

import java.awt.*;
import java.awt.event.KeyEvent;

import display.Display;
import main.Program;
import main.Settings;
import misc.Color8;
import world.*;

public class Game {
	
	public static final int REFRESH_RATE = 50; // milliseconds
	
	private Thread updateLoop;
	private FrameTimer ft;
	private World world;
	
	private float sidePadding = 0.2f;
	private int cameraWidth = (int)(Program.DISPLAY_WIDTH*(1-sidePadding)), 
			    cameraHeight = (int)(Program.DISPLAY_HEIGHT*(1-sidePadding));
	
	private boolean paused = true;
	
	public Game() {
		ft = new FrameTimer();
		world = new World();
		
		float worldViewWidth = 10.0f;
		camera = new Camera(0, 0, worldViewWidth, worldViewWidth/(cameraWidth/(float)cameraHeight));
		
		updateLoop = new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(REFRESH_RATE);
						if (Program.initialized())
							gameLoop();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		updateLoop.start();
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
	
	public void gameLoop() {
		//lets check for pausing
		if (Program.keyboard.keyPressed(KeyEvent.VK_ESCAPE))
			togglePause();
		
		//elapsed time since last loop call.. for regulating game operation speeds
		//across varying operating system performances
		float dt = ft.mark();
		
		if (this.paused)
			return; //dont run the game loop if we are paused
		
		float speed = 1.0f;
		if (Program.keyboard.keyDown(KeyEvent.VK_SHIFT))
			speed = 3.0f;
		if (Program.keyboard.keyDown(Settings.getSetting("move_right").charAt(0))) {
			camera.moveX(speed*dt);
			System.out.println("right key down");
		}
		if (Program.keyboard.keyDown(Settings.getSetting("move_left").charAt(0)))
			camera.moveX(-speed*dt);
		if (Program.keyboard.keyDown(Settings.getSetting("move_up").charAt(0)))
			camera.moveY(-speed*dt);
		if (Program.keyboard.keyDown(Settings.getSetting("move_down").charAt(0)))
			camera.moveY(speed*dt);
		
		main.Keyboard.Key[] keyList = Program.keyboard.getAllKeysDown();
		for (main.Keyboard.Key k : keyList) {
			System.out.print(k.character()+", ");
		}
		System.out.println();
	}
	
	
	/*
	 * rendering stuff
	 */
	
	public void draw(Graphics2D g) {
		camera.draw(world);
		g.drawImage(camera.getView(), 0, 0, cameraWidth, cameraHeight, null);
		g.setColor(Color8.GRAY);
		g.fillRect(0, cameraHeight, cameraWidth+20, 20);
		g.fillRect(cameraWidth, 0, 20, cameraHeight);
		g.fillRect(0, 0, 20, cameraHeight);
		g.fillRect(20, 0, cameraWidth, 20);
		if (paused) {
			g.setColor(Color.RED);
			g.setFont(new Font(Program.FONT_FAMILY,Font.BOLD,Program.DISPLAY_HEIGHT/10));
			Display.drawText(g, "PAUSED", 0.5f, 0.4f, Display.CENTER_ALIGN);
		}
	}
}
