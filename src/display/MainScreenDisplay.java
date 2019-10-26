package display;

import java.awt.*;

import main.Program;
import misc.Color8;
import misc.ImageTools;

public class MainScreenDisplay extends Display {

	private Image background = ImageTools.convertTo8Bit(ImageTools.getImage("gta.jpg"));
	
	float x = 0;
	
	@Override
	public void draw(Graphics2D g) {
		fillBackground(g,background);
		//draw the game name
		g.setColor(Color8.BLUE);
		g.setFont(new Font("Arial",Font.BOLD,Program.DISPLAY_HEIGHT/10));
		drawText(g,Program.GAME_NAME,CustomFonts.HANDDRAWN,0.05f,0.5f,0.3f,Display.CENTER_ALIGN);
	
		x+=0.5f;
		g.drawString("This isgggjjjikl a test", x, 100.5f);
	}
}