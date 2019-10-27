package display;

import java.awt.*;

import display.component.Button;

import main.Program;
import misc.Color8;
import misc.ImageTools;

public class MainScreenDisplay extends Display {

	private Button playButton;
	
	public MainScreenDisplay() {
		super();
		playButton = new Button("Play",50,50,100,50) {
			public void onMouseDown() {
				//DisplayController.setScreen(DisplayController.Screen.GAME);
			}
			
			public void onMouseOver() {
				
			}
		};
		add(playButton);
	}
	
	private Image background = ImageTools.convertTo8Bit(ImageTools.getImage("gta.jpg"));
	
	@Override
	public void draw(Graphics2D g) {
		fillBackground(g,background);
		//draw the game name
		g.setColor(Color8.BLUE);
		g.setFont(new Font("Arial",Font.BOLD,Program.DISPLAY_HEIGHT/10));
		drawText(g,Program.GAME_NAME,CustomFonts.HANDDRAWN,0.05f,0.5f,0.3f,Display.CENTER_ALIGN);
		
		playButton.draw(g);
	}
}