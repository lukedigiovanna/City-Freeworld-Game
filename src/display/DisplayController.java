package display;

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
		MAIN(new MainScreenDisplay()),
		GAME(new GameDisplay()),
		UPDATE_NOTES(new UpdateNotesDisplay());
		
		Display display;
		Screen(Display d) {
			display = d;
		}
	}
	
	private static Screen currentScreen = Screen.MAIN;
	
	private static int captures = 0;
	private static float dt = 0;
	public static void redraw(Graphics2D g) {
		if (!initialized) 
			return;
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, Program.DISPLAY_WIDTH, Program.DISPLAY_HEIGHT);
		
		currentScreen.display.draw(g);
		
		//get the time it took to draw this frame
		dt += frameTimer.mark();
		if (captures > 5) {
			curFps = 1.0f/(dt/captures);
			captures = 1;
			dt = 0;
		} else {
			captures++;
		}
		
		//now draw the fps if that is enabled
		if (showFps) {
			g.setColor(Color.WHITE);
			g.setFont(new Font(Program.FONT_FAMILY,Font.BOLD,18));
			String s = "FPS: "+(int)curFps;
			g.drawString(s, Program.DISPLAY_WIDTH-10-g.getFontMetrics().stringWidth(s), 20);
		}
		
	}
	
	public static void setScreen(Screen screen) {
		currentScreen = screen;
		currentScreen.display.set();
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
	private static Thread compCheckThread;
	
	public static void initialize() {
		compCheckThread = new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(50);
						currentScreen.display.checkComponents();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		compCheckThread.start();
		frameTimer = new FrameTimer();
		initialized = true;
		currentScreen.display.set();
	}
}
