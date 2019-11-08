package display;

import java.awt.*;
import java.awt.image.BufferedImage;

import display.component.Button;
import display.component.Checkbox;
import display.component.*;
import main.Program;
import misc.Color8;
import misc.ImageTools;

public class MainScreenDisplay extends Display {

	private Button[] buttons;
	
	private Checkbox boxTest;
	
	public MainScreenDisplay() {
		super();
		String[] buttonNames = {"Play","quork","quit"};
		Runnable[] buttonActions = {
				new Runnable() { public void run() {
					DisplayController.setScreen(DisplayController.Screen.GAME);
				} }, 
				new Runnable() { public void run() {
					
				} },
				new Runnable() { public void run() {
					System.exit(0);
				} }
		};
		buttons = new Button[buttonNames.length];
		for (int i = 0; i < buttonNames.length; i++) {
			Runnable act = buttonActions[i];
			buttons[i] = new MainScreenButton(buttonNames[i],Program.DISPLAY_WIDTH/2,Program.DISPLAY_HEIGHT/2+50*i) {
				public void onMouseDown() {
					act.run();
				}
			};
		}
		
		for (Button b : buttons)
			add(b);
		
		boxTest = new Checkbox(100,100,50,50);
		add(boxTest);
	}
	
	private BufferedImage background = ImageTools.invert(ImageTools.getBufferedImage("jungle.png"));
	
	@Override
	public void draw(Graphics2D g) {
		fillBackground(g,background);
		//draw the game name
		g.setColor(Color8.BLUE);
		g.setFont(new Font(Program.FONT_FAMILY,Font.BOLD,Program.DISPLAY_HEIGHT/10));
		drawText(g,Program.GAME_NAME,CustomFonts.HANDDRAWN,0.05f,0.5f,0.3f,Display.CENTER_ALIGN);
		
		for (Button b : buttons)
			b.draw(g);
		
		boxTest.draw(g);
	}
	
	@Override
	public void set() {
		
	}
}