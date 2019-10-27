package display;

import java.awt.Graphics2D;

import game.Game;

public class GameDisplay extends Display {

	private Game currentGame;
	
	public GameDisplay() {
		currentGame = new Game();
	}
	
	public void set() {
		currentGame.unpause();
	}
	
	@Override
	public void draw(Graphics2D g) {
		currentGame.draw(g);
	}

}
