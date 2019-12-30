package display.console;

import java.awt.Color;

public class ConsoleMessage {
	
	public static final int 
		ERROR_MESSAGE = 0,
		PLAIN_MESSAGE = 1,
		PROGRAM_MESSAGE = 2;
	
	private int type;
	private String message;
	
	public ConsoleMessage(String message, int type) {
		this.message = message;
		this.type = type;
	}
	
	public String getMessageText() {
		return getPrefix() + message;
	}
	
	public Color getMessageColor() {
		switch (type) {
		case ERROR_MESSAGE:
			return Color.red;
		case PLAIN_MESSAGE:
			return Color.white;
		case PROGRAM_MESSAGE:
			return Color.green;
		default:
			return Color.white;
		}
	}
	
	public String getPrefix() {
		switch (type) {
		case ERROR_MESSAGE:
			return "[ERROR] ";
		case PLAIN_MESSAGE:
			return "";
		case PROGRAM_MESSAGE:
			return "[PROGRAM] ";
		default:
			return "";
		}
	}
}
