package game;

import java.awt.*;

import main.Program;
import mapping.*;

public class Game {
	
	public static final int REFRESH_RATE = 50; // milliseconds
	
	private Thread updateLoop;
	private FrameTimer ft;
	private World world;
	private Camera camera;
	
	private boolean paused = true;
	
	public Game() {
		ft = new FrameTimer();
		world = new World();
		
		camera = new Camera(0, 0);
		
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
	
	public void gameLoop() {
		//elapsed time since last loop call.. for regulating game operation speeds
		//across varying operating system performances
		float dt = ft.mark();
		
		if (this.paused)
			return; //dont run the game loop if we are paused
		
		if (Program.keyboard.keyDown('d')) 
			camera.moveX(1*dt);
		if (Program.keyboard.keyDown('a'))
			camera.moveX(-1*dt);
		if (Program.keyboard.keyDown('w'))
			camera.moveY(-1*dt);
		if (Program.keyboard.keyDown('s'))
			camera.moveY(1*dt);
	}
	
	public void draw(Graphics2D g) {
		camera.draw(g, world, 0, 0, (int)(Program.DISPLAY_WIDTH*0.8), (int)(Program.DISPLAY_HEIGHT*0.8));
	}
}
