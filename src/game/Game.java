package game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.List;

import display.*;
import entities.Entity;
import entities.player.Player;
import weapons.Weapon;
import main.*;
import misc.*;
import world.*;

public class Game {
	
	private static final int TICKS_PER_SECOND = 20;
	public static final int IDEAL_REFRESH_RATE = 1000/TICKS_PER_SECOND;
	private long wait = IDEAL_REFRESH_RATE; //default
	
	private Thread updateLoop;
	private float elapsedTime = 0.0f;
	private FrameTimer ft;
	private World world;
	
	private boolean paused = true;
	
	private boolean gameActive = true; //says whether or not this game should exist
	
	public Game() {
		ft = new FrameTimer();
		world = new World(this);
		
		updateLoop = new Thread(new Runnable() {
			public void run() {
				while (gameActive) {
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
	
	/**
	 * Stops the update loop
	 */
	public void quit() {
		gameActive = false;
	}
	
	public void pause() {
		paused = true;
	}
	
	public void unpause() {
		paused = false;
	}
	
	public void togglePause() {
		if (paused)
			unpause();
		else
			pause();
	}
	
	public boolean isPaused() {
		return this.paused;
	}
	
	
	public World getWorld() {
		return this.world;
	}
	
	private float tps = 0.0f, elapsedSinceLastCapture = 0.0f;
	private int captures = 0;
	
	public float getTPS() {
		return this.tps;
	}
	
	public long getWait() {
		return this.wait;
	}

	public void gameLoop() {
		//lets check for pausing
		if (Program.keyboard.keyPressed(KeyEvent.VK_ESCAPE))
			togglePause();
		
		//elapsed time since last loop call.. for regulating game operation speeds
		//across varying operating system performances
		float dt = ft.mark();
		
		if (paused)
			return;
		
//		if (Program.keyboard.keyDown(KeyEvent.VK_CONTROL)) {
//			if (Program.keyboard.keyPressed(KeyEvent.VK_H))
//				this.world.getCamera().toggleHitboxes();
//			if (Program.keyboard.keyPressed(KeyEvent.VK_W))
//				this.world.getCamera().toggleWalls();
//		}
//		
//		if (Program.keyboard.keyDown('c'))
//			world.getCamera().zoom(0.01f);
//		if (Program.keyboard.keyDown('x'))
//			world.getCamera().zoom(-0.01f);
		
		if (Program.keyboard.keyPressed('h')) {
			for (Entity e : world.getCurrentRegion().getEntities().get())
				System.out.println(e.getVerticalHeight());
		}
		
		world.update(dt);
		
		captures++;
		elapsedSinceLastCapture+=dt;
		if (captures >= 5) {
			tps = 5.0f/elapsedSinceLastCapture;
			captures = 0;
			elapsedSinceLastCapture = 0;
		}
		
		elapsedTime+=dt;
	}
}
