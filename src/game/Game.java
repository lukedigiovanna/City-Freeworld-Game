package game;

import java.awt.*;
import java.awt.event.KeyEvent;

import display.Display;
import display.DisplayController;
import entities.Entity;
import main.Program;
import main.Settings;
import misc.Color8;
import misc.Line;
import misc.Vector2;
import world.*;

public class Game {
	
	public static final int REFRESH_RATE = 50; // milliseconds
	
	private Thread updateLoop;
	private float elapsedTime = 0.0f;
	private FrameTimer ft;
	private World world;
	
	private float sidePadding = 0.25f;
	private int cameraWidth = (int)(Program.DISPLAY_WIDTH*(1-sidePadding)), 
			    cameraHeight = (int)(Program.DISPLAY_HEIGHT*(1-sidePadding));
	
	private boolean paused = true;
	
	public Game() {
		ft = new FrameTimer();
		world = new World();
		
		updateLoop = new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(REFRESH_RATE);
						if (Program.initialized() && DisplayController.getCurrentScreen() == DisplayController.Screen.GAME)
							gameLoop();
						else 
							ft.mark(); //keep the frame timer going so we dont add time that we weren't on the game screen
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
	
	private float tps = 0.0f, elapsedSinceLastCapture = 0.0f;
	private int captures = 0;
	
	public void gameLoop() {
		//lets check for pausing
		if (Program.keyboard.keyPressed(KeyEvent.VK_ESCAPE))
			togglePause();
		
		//elapsed time since last loop call.. for regulating game operation speeds
		//across varying operating system performances
		float dt = ft.mark();
		
		if (this.paused) {
			//check to return to the main menu
			if (Program.keyboard.keyPressed('q'))
				DisplayController.setScreen(DisplayController.Screen.MAIN);
			return; //dont run the game loop if we are paused
		}
		world.update(dt);
		
		if (Program.keyboard.keyDown(' '))
			world.getCamera().zoom(0.01f);
		if (Program.keyboard.keyDown('c'))
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
	
	private int cameraBorderSize = 20;
	
	private Line test = new Line(new Vector2(3,3), new Vector2(4,3));
	
	public void draw(Graphics2D g) {
		world.draw();	
		g.drawImage(world.getCamera().getView(), cameraBorderSize, cameraBorderSize, cameraWidth, cameraHeight, null);
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
		g.setColor(Color.WHITE);
		g.setFont(new Font(Program.FONT_FAMILY,Font.BOLD,18));
		String s = "TPS: "+(int)tps;
		g.drawString(s, Program.DISPLAY_WIDTH-10-g.getFontMetrics().stringWidth(s), 40);
	}
}
