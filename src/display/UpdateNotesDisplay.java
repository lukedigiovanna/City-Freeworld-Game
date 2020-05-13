package display;

import java.awt.*;
import java.awt.event.KeyEvent;

import main.Program;
import display.component.BackButton;
import display.component.Button;
import display.component.Component;

public class UpdateNotesDisplay extends Display {
	private String[] notes = {
		"Implemented a command system in the developer console",
		"    These are only accessible in developer versions not releases",
		"New NPC logic",
		"    Can only 'see' the player within a field of view",
		"    Player can mug NPCs and still their money by pressing 'E'",
		"    NPC may turn hostile and try to kill the player when mugged",
		"    NPCs drop their money, if any, when killed",
		"Some more visual updates:",
		"    Updated the player GUI, better health display and shows money/xp/heat",
		"    Pause screen now fades the gameplay",
		"    Loading screen until the scene loads",
		"    Camera zooms in on the player at the beginning of a new scene",
		"Vehicle modifcations",
		"    Cars travel down the roads now",
		"    Stop and turn at intersections",
		"    Vehicle movement now responds to the tile it drives across",
		"    Vehicles controlled by AI will no longer ram into each other",
		"        Allows the player to steal vehicles when they stop by pressing F",
		"Implemented money and XP",
		"Added end screen when the player dies",
		"Added saving/loading game saves (not currently implemented)",
		"Many bug fixes that make the game run much smoother",
		"    No camera flickering anymore - allows higher FPS",
		"    Hitboxes stay fixed to objects and function correctly",
		"Other minor tweaks and features"
	};
	
	private Button backButton = new BackButton(0.025f,0.025f);
	
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
