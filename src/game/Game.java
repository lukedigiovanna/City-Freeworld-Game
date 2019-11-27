package game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import display.Display;
import display.DisplayController;
import entities.Entity;
import main.Program;
import main.Settings;
import misc.Color8;
import misc.ImageTools;
import misc.Line;
import misc.Vector2;
import world.*;

public class Game {
	
	public static final int REFRESH_RATE = 50; // milliseconds
	
	private Thread updateLoop;
	private float elapsedTime = 0.0f;
	private FrameTimer ft;
	private World world;
	
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
	
	private static int cameraBorderSize = 20;
	
	public static final float CAMERA_PERCENT_WIDTH = 1.0f, CAMERA_PERCENT_HEIGHT = 0.75f;
	public static final int CAMERA_PIXEL_WIDTH = (int)(CAMERA_PERCENT_WIDTH*Program.DISPLAY_WIDTH)-cameraBorderSize*2, CAMERA_PIXEL_HEIGHT = (int)(CAMERA_PERCENT_HEIGHT*Program.DISPLAY_HEIGHT);
	
	private abstract class PauseButton extends display.component.Button {

		public PauseButton(String s, int y) {
			super(s, Program.DISPLAY_WIDTH/2, y, 0, 30, display.component.Component.FORM_CENTER);
		}

		private Color c = Color.blue;
		
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
		public void onMouseOut() { c = Color.blue; }
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
		world.draw();	
		BufferedImage cameraView = world.getCamera().getView();
		//draw the border around the camera
		g.setColor(Color8.GRAY);
		g.fillRect(0, cameraBorderSize+CAMERA_PIXEL_HEIGHT, CAMERA_PIXEL_WIDTH+cameraBorderSize*2, cameraBorderSize);
		g.fillRect(cameraBorderSize+CAMERA_PIXEL_WIDTH, 0, cameraBorderSize, CAMERA_PIXEL_HEIGHT+cameraBorderSize);
		g.fillRect(0, 0, cameraBorderSize, CAMERA_PIXEL_HEIGHT+cameraBorderSize);
		g.fillRect(cameraBorderSize, 0, CAMERA_PIXEL_WIDTH, cameraBorderSize);
		if (paused) {
			//make the game gray scaled
			g.drawImage(ImageTools.colorscale(cameraView,Color.WHITE), cameraBorderSize, cameraBorderSize, CAMERA_PIXEL_WIDTH, CAMERA_PIXEL_HEIGHT, null);
			g.setColor(Color.RED);
			g.setFont(new Font(Program.FONT_FAMILY,Font.BOLD,Program.DISPLAY_HEIGHT/10));
			Display.drawText(g, "PAUSED", 0.5f, 0.4f, Display.CENTER_ALIGN);
			for (PauseButton b : pButs) {
				b.check();
				b.draw(g);
			}
		} else {
			g.drawImage(cameraView, cameraBorderSize, cameraBorderSize, CAMERA_PIXEL_WIDTH, CAMERA_PIXEL_HEIGHT, null);
		}
		//draw this other stuff about the player
		
		g.setColor(Color.WHITE);
		g.setFont(new Font(Program.FONT_FAMILY,Font.BOLD,18));
		String s = "TPS: "+(int)tps;
		g.drawString(s, Program.DISPLAY_WIDTH-10-g.getFontMetrics().stringWidth(s), 40);
	}
}
