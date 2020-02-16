package display;

import java.awt.Graphics2D;

import game.GameController;

public class LoadSaveDisplay extends Display {

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void set() {
		GameController.loadSaveGame("New World");
		DisplayController.setScreen(DisplayController.Screen.GAME);
	}

}
