package phone;

import java.awt.*;

public class ContactsApp extends PhoneApp {
	public ContactsApp() {
		super(PhoneAppInfo.CONTACTS);
	}
	
	public void draw(Graphics2D g) {
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, Phone.SCREEN_WIDTH, Phone.SCREEN_HEIGHT);
	}
}
