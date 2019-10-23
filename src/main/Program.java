package main;

import java.awt.Dimension;
import java.awt.Toolkit;

//static class that holds some innate information about this application
public class Program {
	public static final int DISPLAY_WIDTH = 780, DISPLAY_HEIGHT = 585;
	
	public static final int VERSION_MAJOR = 0, VERSION_MINOR = 0, VERSION_TINY = 1;
	
	public static final String GAME_NAME = "ddg (tbd)",
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
}
