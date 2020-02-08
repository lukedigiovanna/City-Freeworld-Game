package display.console;

public class CommandParser {
	public static void parseCommand(Console console, String command) {
		String[] tokens = command.split(" "); //break up the command into tokens
		
		if (tokens[0].contentEquals("")) {
			console.err("No command entered");
			return;
		}
		
		String comName = tokens[0];
		
		boolean foundCommand = false;
		for (Command com : Command.commands) {
			if (comName.contentEquals(com.getName())) {
				String[] comTokens = new String[tokens.length-1];
				for (int i = 1; i < tokens.length; i++) 
					comTokens[i-1]=tokens[i];
				com.parse(console,comTokens);
				foundCommand = true;
				break;
			}
		}
		if (!foundCommand) {
			console.err("Unknown command "+comName);
		}
	}
}
