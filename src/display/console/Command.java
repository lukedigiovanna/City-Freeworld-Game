package display.console;

import java.util.ArrayList;
import java.util.List;

import display.DisplayController;
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
