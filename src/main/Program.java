package main;

import java.awt.*;

import display.DisplayController;
import display.console.Command;
import display.textures.TexturePack;
import display.ui.UIController;
import game.Game;
import misc.ImageTools;
import shops.Shop;
import soundEngine.Sounds;
import world.World;

//static class that holds some innate information about this application
public class Program {
	private static final double ratio = 4.0f/3.0f; //WIDTH/HEIGHT
	public static final int DISPLAY_WIDTH = 1080, DISPLAY_HEIGHT = (int)(DISPLAY_WIDTH/ratio);
	
	public static final int VERSION_MAJOR = 6, VERSION_MINOR = 0, VERSION_TINY = 1;
	
	public static final String GAME_NAME = "Kings of San Anglos",
							   VERSION_PREFIX = "Indev",
							   DEVELOPMENT_PERIOD = "May 2020";
	
	public static final String FONT_FAMILY = "Consolas";
	
	public static final Image PROGRAM_ICON = ImageTools.getImage("assets/icon.png");
	
	public static String getVersionString() {
		String s = VERSION_PREFIX + " " + VERSION_MAJOR + "." + VERSION_MINOR;
		if (VERSION_TINY > 0)
			s += "."+VERSION_TINY;
		return s;
	}
	
	private static Toolkit toolkit = Toolkit.getDefaultToolkit();
	
	public static Toolkit getToolkit() {
		return toolkit;
	}
	
	private static final Dimension screen_size = toolkit.getScreenSize();
	
	public static final int SCREEN_WIDTH = screen_size.width, SCREEN_HEIGHT = screen_size.height;
	
	public static GamePanel panel;
	public static Frame frame;
	
	public static Mouse mouse;
	public static Keyboard keyboard;
	
	private static long startingUsedMemory;
	
	private static boolean initialized = false;
	
	public static void init() {
		TexturePack.initialize();
		Command.initialize();
		Settings.initialize();
		UIController.initialize();
		Game.initialize();
		Shop.initialize();
		
		initFrame(); //opens up the window and creates a game panel object
		mouse = new Mouse(panel,Program.DISPLAY_WIDTH,Program.DISPLAY_HEIGHT);
		keyboard = new Keyboard(panel);
		DisplayController.initialize();
		Sounds.initialize();
		
		World.loadWorldsList();
		
		Runtime rt = Runtime.getRuntime();
		startingUsedMemory = rt.totalMemory()-rt.freeMemory();
		
		initialized = true;
	}
	
	/**
	 * Calculates the total used memory and returns
	 * the value in MB (binary)
	 * @return
	 */
	public static long getUsedMemory() {
		Runtime rt = Runtime.getRuntime();
		long currentUsedMemory = rt.totalMemory()-rt.freeMemory();
		return currentUsedMemory/(1024L*1024L);
	}
	
	private static void initFrame() {
		panel = new GamePanel();
		frame = new Frame(GAME_NAME + " " + getVersionString() + " | "+DEVELOPMENT_PERIOD,panel);
	}
	
	public static boolean initialized() {
		return initialized;
	}
	
	public static void exit() {
		System.exit(0);
	}
}
