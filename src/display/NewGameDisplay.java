package display;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import game.GameController;
import main.Program;

public class NewGameDisplay extends Display {

	@Override
	public void draw(Graphics2D g) {
		Display.fillBackground(g, Color.DARK_GRAY);
		g.setFont(new Font("Consolas",Font.BOLD,48));
		g.setColor(Color.WHITE);
		String s = "New Game";
		g.drawString(s, Program.DISPLAY_WIDTH/2-g.getFontMetrics().stringWidth(s)/2, 60);
	}

	@Override
	public void set() {
		GameController.createNewGame("New World");
		DisplayController.setScreen(DisplayController.Screen.GAME);
	}

}
