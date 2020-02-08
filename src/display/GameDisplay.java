package display;

import java.awt.Graphics2D;

import javax.swing.JFrame;

import entities.player.Player;
import game.Game;
import game.GameDrawer;
import main.Program;

public class GameDisplay extends Display {

	private Game currentGame;
	private GameDrawer gameDrawer;
	
	public GameDisplay() {
		
	}
	
	public void set() {
		currentGame = new Game();
		Player player = currentGame.getWorld().addPlayer();
		gameDrawer = new GameDrawer(currentGame, player);
		currentGame.unpause();
		Program.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
	
	@Override
	public void draw(Graphics2D g) {
		if (gameDrawer != null)
			gameDrawer.draw(g);
	}

}
