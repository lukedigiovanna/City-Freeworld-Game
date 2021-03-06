package game;

import java.awt.event.KeyEvent;

import display.*;
import display.ui.UI;
import display.ui.UICodex;
import entities.player.Player;
import main.*;
import misc.*;
import world.*;

public class Game {
	
	private static final int TICKS_PER_SECOND = 50;
	public static final int IDEAL_REFRESH_RATE = 1000/TICKS_PER_SECOND;
	private long wait = IDEAL_REFRESH_RATE; //default
	
	private Thread updateLoop;
	private float elapsedTime = 0.0f;
	private FrameTimer ft;
	private World world;
	
	private boolean paused = true;
	
	private boolean gameActive = true; //says whether or not this game should exist
	
	private Game() {
		ft = new FrameTimer();
		startUpdateLoop();
	}
	
	public Game(String worldName, String saveName) {
		this();
		world = new World(worldName, saveName);
	}
	
	public Game(String saveName) {
		this();
		world = World.loadWorld(saveName);
	}
	
	private static UI gameUI;
	public static UI getInput() {
		return gameUI;
	}
	
	public static void initialize() {
		gameUI = UICodex.get("game");
	}
	
	private void startUpdateLoop() {
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
	
	public Player getPlayer() {
		return this.world.getPlayer();
	}
	
	/**
	 * Stops the update loop
	 */
	public void quit() {
		world.close();
		gameActive = false;
	}
	
	public void pause() {
		paused = true;
		this.world.getCurrentRegion().getSoundEngine().pause();
	}
	
	public void unpause() {
		paused = false;
		this.world.getCurrentRegion().getSoundEngine().unpause();
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
		if (world == null)
			return;
		
		//lets check for pausing
		if (gameUI.keyPressed(KeyEvent.VK_ESCAPE))
			togglePause();
		
		//elapsed time since last loop call.. for regulating game operation speeds
		//across varying operating system performances
		float dt = ft.mark();
		
		if (gameOver)
			paused = false;
		if (paused) 
			return;
		
		if (this.gameOver) {
			this.gameOverTimer+=dt;
			this.world.getCurrentRegion().getSoundEngine().pause();
			if (this.gameOverTimer < 30f) {
				world.update(dt * 0.25f);
			}
		}
		else
			world.update(dt);
		
		captures++;
		elapsedSinceLastCapture+=dt;
		if (captures >= 5) {
			tps = 5.0f/elapsedSinceLastCapture;
			captures = 0;
			elapsedSinceLastCapture = 0;
		}
		
		Player player = this.getPlayer();
		if (player != null && !gameOver && (player.getHealth().isDead() || player.isCaught())) {
			if (player.getHealth().isDead())
				causeOfEnd = "You died!";
			else if (player.isCaught())
				causeOfEnd = "Busted!";
			gameOver = true; 
		}
		
		elapsedTime+=dt;
	}
	
	private boolean gameOver = false;
	private float gameOverTimer = 0.0f;
	
	public boolean isOver() {
		return gameOver;
	}
	
	public float getTimeGameOver() {
		return this.gameOverTimer;
	}
	
	private String causeOfEnd = "Not over yet :)";
	public String getCauseOfEnd() {
		return this.causeOfEnd;
	}
}
