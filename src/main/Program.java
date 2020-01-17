package main;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import display.DisplayController;
import misc.ImageTools;
import soundEngine.SoundManager;
import soundEngine.Sounds;

//static class that holds some innate information about this application
public class Program {
	private static final double ratio = 4.0f/3.0f; //WIDTH/HEIGHT
	public static final int DISPLAY_WIDTH = 1000, DISPLAY_HEIGHT = (int)(DISPLAY_WIDTH/ratio);
	
	public static final int VERSION_MAJOR = 5, VERSION_MINOR = 0, VERSION_TINY = 0;
	
	public static final String GAME_NAME = "Kings of San Anglos",
							   VERSION_PREFIX = "Indev",
							   DEVELOPMENT_PERIOD = "January 2020";
	
	public static final String FONT_FAMILY = "Consolas";
	
	public static final Image PROGRAM_ICON = ImageTools.getImage("assets/icon.png");
	
	@SuppressWarnings("unused")
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
	
	private static boolean initialized = false;
	
	public static void init() {
		Settings.initialize();
		initFrame(); //opens up the window and creates a game panel object
		mouse = new Mouse(panel,Program.DISPLAY_WIDTH,Program.DISPLAY_HEIGHT);
		keyboard = new Keyboard(panel);
		DisplayController.initialize();
		SoundManager.initialize();
		Sounds.initialize();
		
		initialized = true;
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
