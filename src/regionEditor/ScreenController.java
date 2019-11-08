package regionEditor;

import java.awt.*;

import display.component.Button;
import display.component.Component;
import main.Mouse;
import main.Program;

public class ScreenController {
	public static enum Screen {
			MAIN;
	}
	
	private static Screen current = Screen.MAIN;
	
	public static void setScreen(Screen screen) {
		current = screen;
	}
	
	private static String ff = "Consolas";
	private static int width = EditorPanel.DISPLAY_WIDTH, height = EditorPanel.DISPLAY_HEIGHT;
	
	private abstract static class MainScreenButton extends Button {
		private Font font = new Font(ff,Font.BOLD,30);
		
		public MainScreenButton(String text, int y) {
			super(text,ScreenController.width/2,y,10,20,Component.FORM_CENTER);
		}
		
		@Override
		public void draw(Graphics2D g) {
			g.setFont(font);
			this.setWidth(g.getFontMetrics().stringWidth(this.title));
			this.setHeight(g.getFontMetrics().getHeight());
			g.setColor(Color.BLACK);
			g.drawString(title, x, y);
			g.fillRect(x, y, width, height);
		}

		@Override
		public void onMouseUp() {
			
		}

		@Override
		public void onMouseMoved(int dx, int dy) {
			
		}

		@Override
		public void onMouseOver() {
			font = new Font(ff,Font.BOLD,30);
		}

		@Override
		public void onMouseOut() {
			font = new Font(ff,Font.BOLD,26);
		}
	}
	
	private static MainScreenButton newWorldButton = new MainScreenButton("New World",height/2+height/4) {
		@Override
		public void onMouseDown() {
			
		}
	};
	
	public static void draw(Graphics2D g, Mouse mouse) {
		switch (current) {
		case MAIN:
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(0, 0, width, height);
			g.setFont(new Font(ff,Font.BOLD,38));
			g.setColor(Color.BLACK);
			String str = Program.GAME_NAME+ " Region Editor";
			int y = height/3;
			g.drawString(str, width/2-g.getFontMetrics().stringWidth(str)/2, y);
			g.setColor(Color.GRAY);
			str = "For version: "+Program.getVersionString();
			y+=g.getFont().getSize()+5;
			g.setFont(new Font(ff,Font.PLAIN,30));
			g.drawString(str, width/2-g.getFontMetrics().stringWidth(str)/2, y);
			newWorldButton.check(mouse);
			newWorldButton.draw(g);
			break;
		}
	}
}
