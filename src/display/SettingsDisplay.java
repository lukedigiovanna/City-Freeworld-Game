package display;

import java.awt.*;
import java.awt.event.KeyEvent;

import main.Program;
import main.Settings;
import display.component.*;
import display.component.Button;
import display.component.Checkbox;
import display.component.Component;

public class SettingsDisplay extends Display {
	private Button backButton = new Button("Back to menu",ptpX(0.025f),ptpY(0.025f),0,0,Component.FORM_LEFT) {
		@Override
		public void onMouseDown() {}

		@Override
		public void onMouseUp() {
			DisplayController.setScreen(DisplayController.Screen.MAIN);
		}

		@Override
		public void onMouseMoved(int dx, int dy) {}

		@Override
		public void onMouseOver() {
			c = Color.gray;
		}

		@Override
		public void onMouseOut() {
			c = Color.white;
		}
		
		Color c = Color.white;
		Font f = new Font(Program.FONT_FAMILY,Font.BOLD,ptpY(0.025f));
		public void draw(Graphics2D g) {
			g.setFont(f);
			setDimension(g.getFontMetrics().stringWidth(this.getText()),g.getFontMetrics().getHeight());
			g.setColor(c);
			g.drawString(getText(), x, y+getHeight());
		}
	};
	
	private Checkbox vsync = new Checkbox(200,200,35,35) {

		@Override
		public void onEnable() {
			Settings.setSetting("vsync_enabled", "true");
		}

		@Override
		public void onDisable() {
			Settings.setSetting("vsync_enabled", "false");
		}
	};
	
	public SettingsDisplay() {
		super();
		add(backButton);
		vsync.setValue(false);
		if (Settings.getSetting("vsync_enabled").contentEquals("true"))
			vsync.setValue(true);
		add(vsync);
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
	}

	@Override
	public void set() {
		
	}
}
