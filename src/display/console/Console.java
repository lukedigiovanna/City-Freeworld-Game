package display.console;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import display.ui.UI;
import display.ui.UICodex;
import display.ui.UIController;
import main.Keyboard;
import main.Program;

public class Console {
	
	private List<ConsoleMessage> messages;
	
	private String currentMessage;
	
	/**
	 * If true, then the window is shown in the top left of the screen
	 */
	private boolean active = false;
	
	private int width, height;
	
	public Console() {
		width = (int)(0.5 * Program.DISPLAY_WIDTH);
		height = (int)(0.5 * Program.DISPLAY_HEIGHT);
		messages = new ArrayList<ConsoleMessage>();
		currentMessage = "";
	}
	
	private static int[] untypableKeys = {KeyEvent.VK_SHIFT,KeyEvent.VK_CAPS_LOCK,KeyEvent.VK_CONTROL,KeyEvent.VK_ALT,KeyEvent.VK_TAB};
	/**
	 * Waits for keyboard input to active the console.
	 * If the console is activated then it looks for keyboard
	 * inputs to write to the current message;
	 */
	public void listen() {
		if (Program.keyboard.keyPressed(KeyEvent.VK_BACK_QUOTE)) { //allows the console to be opened from any place in the application
			this.active = !active;
			if (!active) {
				this.currentMessage = "";
				UIController.setDefault();
			} else {
				UIController.setActiveUI("console");
			}
		}
		if (this.active) {
			UI input = UICodex.get("console");
			Keyboard.Key key = input.getNextKey();
			if (key == null)
				return;
			else if (key.keycode() == KeyEvent.VK_ESCAPE) { 
				this.active = false;
			} else if (key.keycode() == KeyEvent.VK_BACK_SPACE) {
				if (this.currentMessage.length() > 0)
					this.currentMessage = this.currentMessage.substring(0,this.currentMessage.length()-1);
			} else if (key.keycode() == KeyEvent.VK_ENTER) {
				this.parse(this.currentMessage);
				this.currentMessage = "";
			} else {
				//check to make sure the key is an actual key
				boolean type = true;
				for (int untypable : untypableKeys)
					if (key.keycode() == untypable) 
						type = false;
				//then add it to the line
				if (type) {
					char character = key.character();
					if (Program.getToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK) || Program.keyboard.keyDown(KeyEvent.VK_SHIFT))
						character = Character.toUpperCase(character);
					this.currentMessage+=character;
				}
			}
		}
	}
	
	private int textSize = 15;
	
	private int timer = 0;
	
	/**
	 * Draws the console in the top left of the screen if it is active
	 * @param g
	 */
	public void draw(Graphics2D g) {
		if (!this.active)
			return;
		timer++;
		if (timer > 20)
			timer = 0;
		g.setColor(new Color(125,125,125,180));
		g.fillRect(0, 0, width, height);
		g.setColor(Color.GRAY);
		g.setStroke(new BasicStroke(5));
		g.drawRoundRect(20, height-15-textSize, width-40, textSize+10,5,5);
		g.setColor(Color.WHITE);
		String s = this.currentMessage;
		if (timer > 10)
			s+="|";
		g.setFont(new Font("Consolas",Font.PLAIN,textSize));
		g.drawString(s, 25, height-13);
		int y = height-13-textSize, x = 20;
		for (int i = messages.size()-1; i >= 0; i--) {
			y -= textSize + 2;
			g.setColor(messages.get(i).getMessageColor());
			g.drawString(messages.get(i).getMessageText(), x, y);
		}
		
		g.setColor(Color.BLACK);
		g.setFont(new Font("Consolas",Font.BOLD,textSize));
		s = "Developer Console";
		g.drawString(s, width-g.getFontMetrics().stringWidth(s)-5, 15);
	}
	
	/**
	 * If the input is preceded by a forward slash '/' then parse it as a command
	 * Else then just add the message to the console
	 * @param input
	 */
	private void parse(String input) {
		if (input.length() == 0)
			return;
		else if (input.charAt(0) == '/') 
			parseCommand(input.substring(1));
		else
			this.log(input);
	}
	
	/**
	 * Sends the message to the command parser to parse the command
	 * @param command
	 */
	private void parseCommand(String command) {
		CommandParser.parseCommand(this,command);
	}
	
	public void log(String message, int type) {
		this.log(new ConsoleMessage(message,type));
	}
	
	public void log(ConsoleMessage message) {
		this.messages.add(message);
	}
	
	/**
	 * Logs it as a plain message if no type is specified
	 * @param message
	 */
	public void log(String message) {
		this.log(message,ConsoleMessage.PLAIN_MESSAGE);
	}
	
	/**
	 * Logs it as an error
	 * @param message
	 */
	public void err(String message) {
		this.log(message,ConsoleMessage.ERROR_MESSAGE);
	}
}
