package phone;

import java.awt.*;

import display.CustomFont;
import main.Program;
import display.Display;

public class InternetApp extends PhoneApp {
	public InternetApp() {
		super(PhoneAppInfo.INTERNET);
	}
	
	public void draw(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, Phone.SCREEN_WIDTH, Phone.SCREEN_HEIGHT);
		g.setColor(Color.BLACK);
		g.setFont(new Font(Program.FONT_FAMILY, 10, Font.PLAIN));
		g.drawString("Hi", 5,5);
		Display.drawText(g, "internet", CustomFont.PIXEL, 5, Phone.SCREEN_WIDTH / 2, 10, Display.CENTER_ALIGN);
	}
}
