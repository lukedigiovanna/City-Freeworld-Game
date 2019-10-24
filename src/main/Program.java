package main;

import java.awt.Dimension;
import java.awt.Toolkit;

//static class that holds some innate information about this application
public class Program {
	public static final int DISPLAY_WIDTH = 780, DISPLAY_HEIGHT = 585;
	
	public static final int VERSION_MAJOR = 0, VERSION_MINOR = 0, VERSION_TINY = 0;
	
	public static final String GAME_NAME = "Kings of San Anglos",
							   VERSION_PREFIX = "Indev",
							   DEVELOPMENT_PERIOD = "October 2019";
	
	@SuppressWarnings("unused")
	public static String getVersionString() {
		String s = VERSION_PREFIX + " " + VERSION_MAJOR + "." + VERSION_MINOR;
		if (VERSION_TINY > 0)
			s += "."+VERSION_TINY;
		return s;
	}
	
	private static Toolkit toolkit = Toolkit.getDefaultToolkit();
	
	private static final Dimension screen_size = toolkit.getScreenSize();
	
	public static final int SCREEN_WIDTH = screen_size.width, SCREEN_HEIGHT = screen_size.height;

	public static Panel panel;
	public static Frame frame;
	
	public static void initFrame() {
		panel = new Panel();
		frame = new Frame(GAME_NAME + " " + getVersionString() + " | "+DEVELOPMENT_PERIOD,panel);
	}
}
