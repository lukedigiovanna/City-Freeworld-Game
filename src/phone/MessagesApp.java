package phone;

import java.awt.*;

import main.Program;

public class MessagesApp extends PhoneApp {
	public MessagesApp() {
		super(PhoneAppInfo.MESSAGES);
	}
	
	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(0, 0, Phone.SCREEN_WIDTH, Phone.SCREEN_HEIGHT);
		g.setColor(Color.BLUE);
		int size = 20;
		g.fillOval((Phone.SCREEN_WIDTH - size) / 2, (Phone.SCREEN_HEIGHT - size) / 2, size, size);
		g.setColor(Color.BLACK);
		g.setFont(new Font(Program.FONT_FAMILY, 12, Font.BOLD));
//		g.drawString("MESSAGES", Phone.SCREEN_WIDTH / 2 - g.getFontMetrics().stringWidth("MESSAGES") / 2, Phone.SCREEN_HEIGHT / 2);
		g.drawString("hi", 10, 10);
	}
}
