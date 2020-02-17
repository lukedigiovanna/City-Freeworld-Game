package display;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import display.component.Button;
import display.component.*;
import main.Program;
import misc.Color8;
import misc.ImageTools;

public class MainScreenDisplay extends Display {

	private Button[] buttons;

	//private Slider slider;
	
	public MainScreenDisplay() {
		super();
		String[] buttonNames = {"New Game","Load Save","Mutiplayer","Settings","Update Notes","Quit"};
		Runnable[] buttonActions = {
				() -> {
					DisplayController.setScreen(DisplayController.Screen.NEW_GAME);
				}, 
				() -> {
					DisplayController.setScreen(DisplayController.Screen.LOAD_SAVE);
				},
				() -> {
					DisplayController.setScreen(DisplayController.Screen.MULTIPLAYER);
				},
				() -> {
					DisplayController.setScreen(DisplayController.Screen.SETTINGS);
				},
				() -> {
					DisplayController.setScreen(DisplayController.Screen.UPDATE_NOTES);
				},
				() -> {
					System.exit(0);
				} 
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
	}
	
	private BufferedImage background = (ImageTools.getImage("gta.jpg"));

	@Override
	public void draw(Graphics2D g) {
		
		fillBackground(g,Color.DARK_GRAY);
		
		float size = 0.4f;
		g.drawImage(Program.PROGRAM_ICON, Program.DISPLAY_WIDTH/2-(int)(size/2*Program.DISPLAY_WIDTH), Program.DISPLAY_HEIGHT/2-(int)(size/2*Program.DISPLAY_HEIGHT), (int)(size*Program.DISPLAY_WIDTH), (int)(size*Program.DISPLAY_HEIGHT), null);
		
		//draw the game name
		g.setColor(Color8.LIGHT_GRAY);
		drawText(g,Program.GAME_NAME,CustomFont.PIXEL,0.05f,0.498f,0.3f,Display.CENTER_ALIGN);
		drawText(g,Program.GAME_NAME,CustomFont.PIXEL,0.05f,0.498f,0.295f,Display.CENTER_ALIGN);
		g.setColor(Color8.GRAY);
		drawText(g,Program.GAME_NAME,CustomFont.PIXEL,0.05f,0.5f,0.3f,Display.CENTER_ALIGN);
		
		for (Button b : buttons)
			b.draw(g);
		
		//slider.draw(g);
	}
	
	@Override
	public void set() {
		Program.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}