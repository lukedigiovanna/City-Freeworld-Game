package display;

import java.awt.*;
import java.awt.event.KeyEvent;

import main.Program;
import display.component.*;
import display.component.Button;
import display.component.Component;

public class UpdateNotesDisplay extends Display {
	private String[] notes = {
		"Added guns to the game",
		"    Glock 21, Desert Eagle, Revolver, AK-47",
		"    Press <R> to reload",
		"    Press <UP> to shoot",
		"Implemented the gun scrolls",
		"    Player uses the selected gun",
		"    Draws the gun into the player's arms",
		"    Use the <TAB> key to open the UI",
		"    Use the L+R arrow keys to traverse the scroll",
		"Added a light-engine",
		"    Added a Day-Night cycle",
		"    Lamps/Fire emit light",
		"New objects: flowers, cans, lamps",
		"Sorts entities by vertical height now",
		"Added text particles and corpses",
		"Tiles can be rotated now",
		"    Allows new maps to be created",
		"New world with street/house",
		"Bug Fixes:",
		"    Player rotation resetting to 0 when the player rotates >= 360deg",
		"    Entities following paths without stopping on the final goal",
		"    Draws the fully grayed out game screen on the pause screen now",
		"    Game loop would run even if the game was quit",
		"        Caused bugs like random noises playing and not being able to use keys"
	};
	
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
	
	public UpdateNotesDisplay() {
		super();
		add(backButton);
	}
	
	@Override
	public void draw(Graphics2D g) {
		if (Program.keyboard.keyPressed(KeyEvent.VK_ESCAPE))
			DisplayController.setScreen(DisplayController.Screen.MAIN);
		
		fillBackground(g, Color.BLACK);
		g.setColor(Color.GRAY);
		g.setFont(new Font(Program.FONT_FAMILY,Font.BOLD,ptpY(0.1f)));
		drawText(g,"UPDATE NOTES",0.5f,0.15f,Display.CENTER_ALIGN);
		g.setColor(Color.LIGHT_GRAY);
		g.setFont(new Font(Program.FONT_FAMILY,Font.ITALIC,ptpY(0.05f)));
		drawText(g,"Version "+Program.getVersionString(),0.5f,0.20f,Display.CENTER_ALIGN);
		float size = 0.03f;
		float y = 0.27f;
		g.setColor(Color.WHITE);
		g.setFont(new Font(Program.FONT_FAMILY,Font.PLAIN,ptpY(size)));
		for (String s : notes) {
			int i = 0;
			String str = "";
			while (s.charAt(0) == ' ') {
				str+=" ";
				s = s.substring(1);
			}
			str+="* "+s;
			drawText(g,str,0.04f,y,Display.LEFT_ALIGN);
			y+=size;
		}
		
		backButton.draw(g);
	}

	@Override
	public void set() {
		
	}
}
