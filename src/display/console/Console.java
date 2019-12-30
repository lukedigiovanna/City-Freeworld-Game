package display.console;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

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
	
	/**
	 * Waits for keyboard input to active the console.
	 * If the console is activated then it looks for keyboard
	 * inputs to write to the current message;
	 */
	public void listen() {
		if (Program.keyboard.keyPressed(KeyEvent.VK_BACK_QUOTE)) {
			this.active = !active;
			if (!active)
				this.currentMessage = "";
		}
		if (this.active) {
			Keyboard.Key key = Program.keyboard.getNextKey();
			if (key == null)
				return;
			if (key.keycode() == KeyEvent.VK_BACK_SPACE) {
				if (this.currentMessage.length() > 0)
					this.currentMessage = this.currentMessage.substring(0,this.currentMessage.length()-1);
			} else if (key.keycode() == KeyEvent.VK_ENTER) {
				this.messages.add(new ConsoleMessage(this.currentMessage,ConsoleMessage.PLAIN_MESSAGE));
				this.currentMessage = "";
			} else {
				char character = key.character();
				if (Program.getToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK) || Program.keyboard.keyDown(KeyEvent.VK_SHIFT))
					character = Character.toUpperCase(character);
				this.currentMessage+=character;
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
		g.drawRoundRect(20, height-10-textSize, width-40, textSize+10,5,5);
		g.setColor(Color.WHITE);
		String s = this.currentMessage;
		if (timer > 10)
			s+="|";
		g.setFont(new Font("Consolas",Font.PLAIN,textSize));
		g.drawString(s, 25, height-8);
		int y = height-8-textSize, x = 20;
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
	
	public void log(String message, int type) {
		log(new ConsoleMessage(message,type));
	}
	
	public void log(ConsoleMessage message) {
		this.messages.add(message);
	}
}
