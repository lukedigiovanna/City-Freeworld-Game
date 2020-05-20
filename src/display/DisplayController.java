package display;

import java.awt.*;

import display.console.Console;
import display.console.ConsoleMessage;
import display.ui.UIController;
import game.FrameTimer;
import main.Program;
import soundEngine.SoundManager;

/*
 * static class to handle drawing to the screen
 */
public class DisplayController {
	public static final int REPAINT_RATE = 50;
	
	public static enum Screen {
		MAIN(new MainScreenDisplay()),
		GAME(new GameDisplay()),
		NEW_GAME(new NewGameDisplay()),
		LOAD_SAVE(new LoadSaveDisplay()),
		MULTIPLAYER(new MultiplayerDisplay()),
		UPDATE_NOTES(new UpdateNotesDisplay()),
		SETTINGS(new SettingsDisplay());
		
		Display display;
		Screen(Display d) {
			display = d;
		}
	}
	
	public static Console console;
	
	private static Screen currentScreen = Screen.MAIN;
	
	private static int captures = 0;
	private static float dt = 0;
	public static void redraw(Graphics2D g) {
		if (!initialized) 
			return;
				
		currentScreen.display.draw(g);
		
		console.listen();
		console.draw(g);
		
		SoundManager.update();
		
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
		UIController.setDefault();
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
		console = new Console();
		console.log("Initialized Program",ConsoleMessage.PROGRAM_MESSAGE);
		frameTimer = new FrameTimer();
		initialized = true;
		currentScreen.display.set();
	}
}
