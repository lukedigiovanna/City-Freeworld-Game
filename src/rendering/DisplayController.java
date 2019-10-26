package rendering;

import java.awt.*;

import game.FrameTimer;
import main.Program;
import misc.Color8;
import misc.ImageTools;
import misc.MathUtils;

/*
 * static class to handle drawing to the screen
 */
public class DisplayController {
	public static final int REPAINT_RATE = 50;
	
	public static enum Screen {
		MAIN,
		GAME;
		
		
		Screen() {
			
		}
	}
	
	private static Screen currentScreen = Screen.MAIN;
	
	private static Image cityBackground = ImageTools.convertTo8Bit(ImageTools.getImage("gta.jpg"));
	
	public static void redraw(Graphics g) {
		if (!initialized) 
			return;
		
		switch (currentScreen) {
		case MAIN:
			fillBackground(g, cityBackground);
			break;
		case GAME:
			break;
		default:
			fillBackground(g, Color8.GRAY);
		}
		
		//get the time it took to draw this frame
		float dt = frameTimer.mark();
		curFps = 1.0f/dt;
		
		System.out.println(curFps);
		
		//now draw the fps if that is enabled
		if (showFps) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial",Font.BOLD,18));
			String s = "FPS: "+(int)curFps;
			g.drawString(s, Program.DISPLAY_WIDTH-10-g.getFontMetrics().stringWidth(s), 20);
		}
		
	}
	
	public static void fillBackground(Graphics g, Color color) {
		g.setColor(color);
		g.fillRect(0, 0, Program.DISPLAY_WIDTH, Program.DISPLAY_HEIGHT);
	}
	
	public static void fillBackground(Graphics g, Image image) {
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
	
	private static float curFps = 0;
	private static boolean showFps = true;
	private static FrameTimer frameTimer;
	private static boolean initialized = false;
	
	public static void initialize() {
		frameTimer = new FrameTimer();
		initialized = true;
	}
}
