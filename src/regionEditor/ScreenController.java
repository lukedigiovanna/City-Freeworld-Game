package regionEditor;

import java.awt.*;

import display.component.Button;
import display.component.Component;
import main.Mouse;
import main.Program;

public class ScreenController {
	public static enum Screen {
			MAIN,
			NEW_WORLD,
			LOAD_WORLD,
			ASSET_MAKER,
			LEVEL_EDITOR;
	}
	
	private static Screen current = Screen.MAIN;
	
	public static void setScreen(Screen screen) {
		current = screen;
	}
	
	private static String ff = "Consolas";
	private static int width = EditorPanel.DISPLAY_WIDTH, height = EditorPanel.DISPLAY_HEIGHT;
	
	private abstract static class MainScreenButton extends Button {
		private Font font = new Font(ff,Font.BOLD,26);
		
		public MainScreenButton(String text, int y) {
			super("< "+text+" >",ScreenController.width/2,y,0,0,Component.FORM_CENTER);
		}
		
		
		@Override
		public void draw(Graphics2D g) {
			g.setFont(font);
			int w = g.getFontMetrics().stringWidth(this.getText());
			this.setWidth(w);
			this.setHeight(g.getFontMetrics().getHeight());
			g.setColor(Color.YELLOW);
			g.drawString(title, x, y+this.getHeight());
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
	
	private static MainScreenButton 
		newWorldButton = new MainScreenButton("New World",height/2) {
		public void onMouseDown() {
			ScreenController.current = ScreenController.Screen.LEVEL_EDITOR;
		}
	},
		loadWorldButton = new MainScreenButton("Load World",height/2+40) {
		public void onMouseDown() {
			ScreenController.current = ScreenController.Screen.LOAD_WORLD;
		}
	},
		assetMakerButton = new MainScreenButton("Create Assets",height/2+80) {
		public void onMouseDown() {
			ScreenController.current = ScreenController.Screen.ASSET_MAKER;
		}
	},
		quitButton = new MainScreenButton("Exit",height/2+120) {
		public void onMouseDown() {
			System.exit(0);
		}
	};

	
	public static void draw(Graphics2D g, Mouse mouse) {
		String str = "";
		int y = 0;
		switch (current) {
		case MAIN:
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(0, 0, width, height);
			g.setFont(new Font(ff,Font.BOLD,38));
			g.setColor(Color.BLACK);
			str = Program.GAME_NAME+ " Region Editor";
			y = height/3;
			g.drawString(str, width/2-g.getFontMetrics().stringWidth(str)/2, y);
			g.setColor(Color.GRAY);
			str = "For version: "+Program.getVersionString();
			y+=g.getFont().getSize()+5;
			g.setFont(new Font(ff,Font.PLAIN,30));
			g.drawString(str, width/2-g.getFontMetrics().stringWidth(str)/2, y);
			newWorldButton.check(mouse);
			newWorldButton.draw(g);
			loadWorldButton.check(mouse);
			loadWorldButton.draw(g);
			assetMakerButton.check(mouse);
			assetMakerButton.draw(g);
			quitButton.check(mouse);
			quitButton.draw(g);
			break;
		case NEW_WORLD:
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(0, 0, width, height);
			g.setFont(new Font(ff,Font.BOLD,34));
			g.setColor(Color.BLACK);
			str = "New World!";
			y = height/8;
			g.drawString(str, width/2-g.getFontMetrics().stringWidth(str)/2, y);
			break;
		case LOAD_WORLD:
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(0, 0, width, height);
			g.setFont(new Font(ff,Font.BOLD,34));
			g.setColor(Color.BLACK);
			str = "Load World!";
			y = height/8;
			g.drawString(str, width/2-g.getFontMetrics().stringWidth(str)/2, y);
			break;
		case LEVEL_EDITOR:
			Editor.draw(g,width,height);
			break;
		case ASSET_MAKER:
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(0, 0, width, height);
			g.setFont(new Font(ff,Font.BOLD,34));
			g.setColor(Color.BLACK);
			str = "Asset Maker!";
			y = height/8;
			g.drawString(str, width/2-g.getFontMetrics().stringWidth(str)/2, y);
		default:
			break;
		}
	}
}
