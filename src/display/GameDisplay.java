package display;

import java.awt.Graphics2D;

import javax.swing.JFrame;

import entities.player.Player;
import game.Game;
import game.GameController;
import game.GameDrawer;
import main.Program;

public class GameDisplay extends Display {
	
	public GameDisplay() {
		
	}
	
	public void set() {
		Program.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
	
	@Override
	public void draw(Graphics2D g) {
		if (GameController.getGameDrawer() != null)
			GameController.getGameDrawer().draw(g);
	}

}
