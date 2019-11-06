package game;

import java.awt.*;
import java.awt.event.KeyEvent;

import display.Display;
import main.Program;
import main.Settings;
import misc.Color8;
import misc.Line;
import misc.Vector2;
import world.*;

public class Game {
	
	public static final int REFRESH_RATE = 50; // milliseconds
	
	private Thread updateLoop;
	private FrameTimer ft;
	private World world;
	private Camera camera;
	
	private float sidePadding = 0.25f;
	private int cameraWidth = (int)(Program.DISPLAY_WIDTH*(1-sidePadding)), 
			    cameraHeight = (int)(Program.DISPLAY_HEIGHT*(1-sidePadding));
	
	private boolean paused = true;
	
	public Game() {
		ft = new FrameTimer();
		world = new World();
		
		float worldViewWidth = 10.0f;
		camera = new Camera(0, 0, worldViewWidth, worldViewWidth/(cameraWidth/(float)cameraHeight));
		camera.linkToRegion(world.getCurrentRegion());
		
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
		
		world.updateCurrentRegion(dt);
		
		if (Program.keyboard.keyDown(' '))
			test.rotate(0.1f);
		if (Program.keyboard.keyDown('c'))
			test.rotateAbout(new Vector2(3,3), (float)(Math.PI*2*0.1*dt));
	}
	
	
	/*
	 * rendering stuff
	 */
	
	private int cameraBorderSize = 20;
	
	private Line test = new Line(new Vector2(3,3), new Vector2(6,4));
	
	public void draw(Graphics2D g) {
		camera.draw();	
		camera.setColor(Color.YELLOW);
		Vector2[] ep = test.getEndpoints();
		camera.drawLine(ep[0].x,ep[0].y,ep[1].x,ep[1].y);
		g.drawImage(camera.getView(), cameraBorderSize, cameraBorderSize, cameraWidth, cameraHeight, null);
		//draw the border around the camera
		g.setColor(Color8.GRAY);
		g.fillRect(0, cameraBorderSize+cameraHeight, cameraWidth+cameraBorderSize*2, cameraBorderSize);
		g.fillRect(cameraBorderSize+cameraWidth, 0, cameraBorderSize, cameraHeight+cameraBorderSize);
		g.fillRect(0, 0, cameraBorderSize, cameraHeight+cameraBorderSize);
		g.fillRect(cameraBorderSize, 0, cameraWidth, cameraBorderSize);
		if (paused) {
			g.setColor(Color.RED);
			g.setFont(new Font(Program.FONT_FAMILY,Font.BOLD,Program.DISPLAY_HEIGHT/10));
			Display.drawText(g, "PAUSED", 0.5f, 0.4f, Display.CENTER_ALIGN);
		}
	}
}
