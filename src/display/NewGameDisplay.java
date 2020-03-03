package display;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import javax.swing.JOptionPane;

import display.component.BackButton;
import display.component.Button;
import game.GameController;
import main.Program;
import world.World;

public class NewGameDisplay extends Display {
	private Button backButton = new BackButton(0.025f,0.025f);
	
	public NewGameDisplay() {
		super();
		add(backButton);
	}
	
	@Override
	public void draw(Graphics2D g) {
		Display.fillBackground(g, Color.DARK_GRAY);
		g.setFont(new Font("Consolas",Font.BOLD,48));
		g.setColor(Color.WHITE);
		String s = "New Game";
		g.drawString(s, Program.DISPLAY_WIDTH/2-g.getFontMetrics().stringWidth(s)/2, 60);
		backButton.draw(g);
	}

	@Override
	public void set() {
		String worldName = JOptionPane.showInputDialog(Program.panel,"Enter a world name");
		String original = worldName;
		int tries = 0;
		while (World.hasWorld(worldName)) {
			worldName = original + " ("+tries+")";
			tries++;
		}
		GameController.createNewGame("realworld", worldName);
		DisplayController.setScreen(DisplayController.Screen.GAME);
	}
		
}
