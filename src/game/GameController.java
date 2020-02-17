package game;

import entities.player.Player;

public class GameController {
	private static Game game;
	private static GameDrawer gameDrawer;
	
	public static void createNewGame(String worldName, String name) {
		game = new Game(worldName,name);
		loadGameDrawer();
	}
	
	public static void loadSaveGame(String name) {
		game = new Game(name);
		loadGameDrawer();
	}
	
	private static void loadGameDrawer() {
		Player p = game.getWorld().getPlayers().get(0);
		gameDrawer = new GameDrawer(game,p);
		game.unpause();
	}
	
	public static GameDrawer getGameDrawer() {
		return gameDrawer;
	}
	
	public static Game getGame() {
		return game;
	}
}
