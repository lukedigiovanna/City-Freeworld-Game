package display;

import java.awt.*;
import java.awt.event.KeyEvent;

import main.Program;
import main.Settings;
import display.component.BackButton;
import display.component.Button;
import display.component.Checkbox;
import display.component.Component;

public class SettingsDisplay extends Display {
	private Button backButton = new BackButton(0.025f,0.025f);
	
	private Checkbox vsync = new Checkbox(200,200,35,35) {

		@Override
		public void onEnable() {
			Settings.setSetting("vsync_enabled", "true");
		}

		@Override
		public void onDisable() {
			Settings.setSetting("vsync_enabled", "false");
		}
	}, mute = new Checkbox(200,245,35,35) {
		public void onEnable() {
			Settings.setSetting("muted", "true");
		}
		
		public void onDisable() {
			Settings.setSetting("muted", "false");
		}
	};
	
	public SettingsDisplay() {
		super();
		add(backButton);
		vsync.setValue(false);
		if ((boolean)Settings.getSetting("vsync_enabled") == true)
			vsync.setValue(true);
		add(vsync);
		mute.setValue(false);
		if ((boolean)Settings.getSetting("muted") == true)
			mute.setValue(true);
		add(mute);
	}
	
	@Override
	public void draw(Graphics2D g) {
		if (Program.keyboard.keyPressed(KeyEvent.VK_ESCAPE))
			DisplayController.setScreen(DisplayController.Screen.MAIN);
		
		fillBackground(g, Color.BLACK);
		g.setColor(Color.GRAY);
		g.setFont(new Font(Program.FONT_FAMILY,Font.BOLD,ptpY(0.1f)));
		drawText(g,"Settings",0.5f,0.15f,Display.CENTER_ALIGN);
		
		backButton.draw(g);
		vsync.draw(g);
		mute.draw(g);
		
		g.setColor(Color.WHITE);
		g.setFont(new Font(Program.FONT_FAMILY,Font.ITALIC,ptpY(0.03f)));
		g.drawString("VSync Enabled", 245, 230);
		g.drawString("Muted", 245, 275);
	}

	@Override
	public void set() {
		
	}
}
