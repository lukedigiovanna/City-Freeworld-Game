package display.console;

import java.util.ArrayList;
import java.util.List;

import display.DisplayController;
import game.GameController;
import main.Program;

public abstract class Command {
	public static List<Command> commands = new ArrayList<Command>();
	
	//adds the commands to the list
	public static void initialize() {
		new Command("help","lists out each command") {
			public void parse(Console console, String ... tokens) {
				for (Command c : commands) {
					console.log(c.getName() + " - " +c.description);
				}
			}
		};
		
		new Command("goto","sets the display to a specified screen") {
			public void parse(Console console, String ... tokens) {
				if (tokens.length == 0) {
					console.err("Specify a screen to go to");
				} else {
					switch (tokens[0]) {
					case "main":
						DisplayController.setScreen(DisplayController.Screen.MAIN);
						break;
					case "settings":
						DisplayController.setScreen(DisplayController.Screen.SETTINGS);
						break;
					case "new_game":
						DisplayController.setScreen(DisplayController.Screen.NEW_GAME);
						break;
					case "game":
						DisplayController.setScreen(DisplayController.Screen.GAME);
						break;
					case "update_notes":
						DisplayController.setScreen(DisplayController.Screen.UPDATE_NOTES);
						break;
					case "quit":
						Program.exit();
						break;
					}
				}
			}
		};
		
		new Command("tp","teleports the player to a specified coordinate") {
			public void parse(Console console, String ... tokens) {
				if (tokens.length < 2)
					console.err("Must enter two values: x, y");
				else if (tokens.length > 2)
					console.err("Too many arguments for this command");
				else {
					try {
						Float x = Float.parseFloat(tokens[0]);
						Float y = Float.parseFloat(tokens[1]);
						GameController.getGame().getWorld().getPlayer().forcePosition(x, y);
					} catch (NullPointerException e) {
						console.err("Only use this command in game");
					} catch (NumberFormatException e) {
						console.err("Only enter float values");
					}
				}
			}
		};
		
		new Command("health", "use add to heal and remove to hurt the player") {
			public void parse(Console console, String ... tokens) {
				if (tokens.length < 1)
					console.err("Enter add or remove");
				else if (tokens.length < 2)
					console.err("Enter a value to "+tokens[1]);
				else if (tokens.length > 2)
					console.err("Too many arguments for this command");
				else {
					try {
						Float val = Float.parseFloat(tokens[1]);
						if (tokens[0].contentEquals("add"))
							GameController.getGame().getWorld().getPlayer().getHealth().heal(val);
						else if (tokens[0].contentEquals("remove"))
							GameController.getGame().getWorld().getPlayer().getHealth().hurt(val);
					} catch (NullPointerException e) {
						console.err("Only use this command in game");
					} catch (NumberFormatException e) {
						console.err("Only enter float values");
					}
				}
			}
		};
	}
	
	private String name;
	private String description;
	
	public Command(String name, String description) {
		this.name = name;
		this.description = description;
		commands.add(this);
	}
	
	public String getName() {
		return this.name;
	}
	
	public abstract void parse(Console console, String ... tokens);
}
