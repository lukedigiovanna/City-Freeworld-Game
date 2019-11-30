package display;

import java.awt.Graphics2D;

import javax.swing.JFrame;

import game.Game;
import main.Program;

public class GameDisplay extends Display {

	private Game currentGame;
	
	public GameDisplay() {
		
	}
	
	public void set() {
		currentGame = new Game();
		currentGame.unpause();
		Program.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
	
	@Override
	public void draw(Graphics2D g) {
		if (currentGame != null)
			currentGame.draw(g);
	}

}
