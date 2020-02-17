package display;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import display.component.BackButton;
import display.component.Button;
import main.Program;

public class MultiplayerDisplay extends Display {
	private Button back = new BackButton(0.025f,0.025f);
	
	public MultiplayerDisplay() {
		add(back);
	}
	
	@Override
	public void draw(Graphics2D g) {
		fillBackground(g, Color.DARK_GRAY);
		g.setFont(new Font(Program.FONT_FAMILY,Font.BOLD,60));
		g.setColor(Color.WHITE);
		String s = "Multiplayer Coming Soon!";
		g.drawString(s, Program.DISPLAY_WIDTH/2-g.getFontMetrics().stringWidth(s)/2, Program.DISPLAY_HEIGHT/2-100);
		back.draw(g);
	}

	@Override
	public void set() {
		
	}

}
