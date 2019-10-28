package display;

import java.awt.*;

import display.component.Button;
import display.component.Checkbox;
import display.component.*;
import main.Program;
import misc.Color8;
import misc.ImageTools;

public class MainScreenDisplay extends Display {

	private Button playButton;
	
	private Checkbox boxTest;
	
	public MainScreenDisplay() {
		super();
		playButton = new MainScreenButton("Play",Program.DISPLAY_WIDTH/2,Program.DISPLAY_HEIGHT/2) {
			public void onMouseUp() {
				DisplayController.setScreen(DisplayController.Screen.GAME);
			}
		};
		add(playButton);
		
		boxTest = new Checkbox(100,100,50,50);
		add(boxTest);
	}
	
	private Image background = ImageTools.convertTo8Bit(ImageTools.getImage("gta.jpg"));
	
	@Override
	public void draw(Graphics2D g) {
		fillBackground(g,background);
		//draw the game name
		g.setColor(Color8.BLUE);
		g.setFont(new Font(Program.FONT_FAMILY,Font.BOLD,Program.DISPLAY_HEIGHT/10));
		drawText(g,Program.GAME_NAME,CustomFonts.HANDDRAWN,0.05f,0.5f,0.3f,Display.CENTER_ALIGN);
		
		playButton.draw(g);
		
		boxTest.draw(g);
	}
	
	@Override
	public void set() {
		
	}
}