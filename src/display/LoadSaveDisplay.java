package display;

import java.awt.*;

import display.component.BackButton;
import display.component.Button;
import display.component.Component;

import game.GameController;
import main.Program;
import world.World;

import java.util.List;
import java.util.ArrayList;

public class LoadSaveDisplay extends Display {
	private Button backButton = new BackButton(0.025f,0.025f);
	
	private class PlayButton extends Button {
		public PlayButton(String worldName, int y) {
			super(worldName,Program.DISPLAY_WIDTH/2, y, Component.FORM_CENTER);
		}
		
		@Override
		public void draw(Graphics2D g) {
			g.setFont(new Font(Program.FONT_FAMILY,Font.ITALIC,36));
			this.setDimension(g.getFontMetrics().stringWidth(this.getText())+10, g.getFontMetrics().getHeight()+5);
			//set the dimensions based off the size of the font
			g.setColor(Color.BLACK);
			g.fillRoundRect(x-3, y-3, width+6, height+6, 5, 5);
			g.setColor(Color.WHITE);
			g.fillRect(x, y, width, height);
			g.setColor(Color.BLUE);
			g.drawString(this.getText(), x+width/2-g.getFontMetrics().stringWidth(this.getText())/2, y+height/2+5);
		}

		@Override
		public void onMouseDown() {
			
		}

		@Override
		public void onMouseUp() {
			GameController.loadSaveGame(this.getText());
			DisplayController.setScreen(DisplayController.Screen.GAME);
		}

		@Override
		public void onMouseOver() {
			
		}

		@Override
		public void onMouseOut() { 
			
		}
	}
	
	private List<PlayButton> buttons;
	
	@Override
	public void draw(Graphics2D g) {
		fillBackground(g, Color.DARK_GRAY);
		g.setColor(Color.WHITE);
		String s = "Select World";
		g.setFont(new Font(Program.FONT_FAMILY,Font.BOLD,48));
		g.drawString(s, Program.DISPLAY_WIDTH/2-g.getFontMetrics().stringWidth(s)/2, 60);
		
		for (Button b : buttons)
			b.draw(g);
		
		backButton.draw(g);
	}

	@Override
	public void set() {
		this.clearComponents();
		
		buttons = new ArrayList<PlayButton>();
		
		int y = Program.DISPLAY_HEIGHT/4;
		for (String s : World.getWorldsList()) {
			buttons.add(new PlayButton(s,y+=70));
		}
		
		for (PlayButton b : buttons)
			this.add(b);
		
		add(backButton);
	}

}
