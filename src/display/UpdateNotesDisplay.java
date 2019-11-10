package display;

import java.awt.*;
import java.awt.event.KeyEvent;

import main.Program;

public class UpdateNotesDisplay extends Display {
	private String[] notes = {
		"Low-level utility implementations",
		"   Math, graphics, displays, image tools, mouse, keyboard",
		"World structure somewhat developed",
		"   Regions, entity lists, cell grid",
		"Hitboxes constructed with lines",
		"   Rotation implemented",
		"   Collision implemented",
		"Displays",
		"   Multiple display screens: Main, Game, Update Notes",
		"   Buttons, checkboxes",
		"   Custom fonts",
		"Bug fixes to lessen the visual glitches"
	};
	
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
		float y = 0.3f;
		g.setColor(Color.WHITE);
		g.setFont(new Font(Program.FONT_FAMILY,Font.PLAIN,ptpY(size)));
		for (String s : notes) {
			int i = 0;
			String str = "";
			while (s.charAt(i++) == ' ') {
				str+=" ";
				s = s.substring(1);
			}
			str+="* "+s;
			drawText(g,str,0.1f,y,Display.LEFT_ALIGN);
			y+=size;
		}
	}

	@Override
	public void set() {
		
	}
}
