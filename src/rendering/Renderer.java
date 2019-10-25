package rendering;

import java.awt.*;

import game.FrameTimer;
import main.Program;
import misc.Color8;

/*
 * static class to handle drawing to the screen
 */
public class Renderer {
	public static final int REPAINT_RATE = 50;
	
	public static enum Screen {
		MAIN,
		GAME;
		
		
		Screen() {
			
		}
	}
	
	private static Screen currentScreen = Screen.MAIN;
	
	public static void redraw() {
		//if (g == null) { //if for whatever reason the graphics didn't initialize
			g = Program.panel.getGraphics(); //capture the graphics from the panel
		//}
		
		switch (currentScreen) {
		case MAIN:
			fillBackground(Color8.BLUE);
			break;
		case GAME:
			break;
		default:
			fillBackground(Color8.GRAY);
		}
		
		//get the time it took to draw this frame
		float dt = frameTimer.mark();
		System.out.println(dt);
		curFps = 1.0f/dt;
		
		//now draw the fps if that is enabled
		if (showFps) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial",Font.BOLD,18));
			String s = "FPS: "+curFps;
			g.drawString(s, Program.DISPLAY_WIDTH-10-g.getFontMetrics().stringWidth(s), 20);
		}
		
		Program.panel.repaint();
	}
	
	public static void fillBackground(Color color) {
		g.setColor(color);
		g.fillRect(0, 0, Program.DISPLAY_WIDTH, Program.DISPLAY_HEIGHT);
	}
	
	public static void fillBackground(Image image) {
		g.drawImage(image, 0, 0, Program.DISPLAY_WIDTH, Program.DISPLAY_HEIGHT, null);
	} 
	
	public static void setScreen(Screen screen) {
		currentScreen = screen;
	}
	
	public static Screen getCurrentScreen() {
		return currentScreen;
	}
	
	public static float getFps() {
		return curFps;
	}
	
	public static void toggleShowFPS() {
		showFps = !showFps;
	}
	
	private static Thread repaintThread;
	private static float curFps = 0;
	private static boolean showFps = true;
	private static FrameTimer frameTimer;
	private static Graphics2D g;
	
	public static void initialize() {
		repaintThread = new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(REPAINT_RATE);
						redraw();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		repaintThread.start();
		g = Program.panel.getGraphics();
		frameTimer = new FrameTimer();
	}
}
