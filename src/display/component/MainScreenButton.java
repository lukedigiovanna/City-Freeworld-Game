package display.component;

import java.awt.*;

import main.Program;

public class MainScreenButton extends Button {
	
	private int ox;
	
	public MainScreenButton(String title, int x, int y) {
		super(title,x,y,0,(int)(Program.DISPLAY_HEIGHT/15));
		ox = x;
	}
	
	private Color color = Color.GRAY;
	
	public void draw(Graphics2D g) {
		double padding = 0.2;
		g.setFont(new Font(Program.FONT_FAMILY,Font.PLAIN,(int)(height*(1-padding))));
		int stringWidth = g.getFontMetrics().stringWidth(getText());
		width = (int)(stringWidth*(1+padding));
		x = ox-width/2;
		g.setColor(Color.BLACK);
		g.fillRect(x, y, width, height);
		g.setColor(color);
		g.fillRect(x+1, y+1, width-2, height-2);
		g.setColor(Color.WHITE);
		g.drawString(getText(), x+width/2-stringWidth/2, (int) (y+height-(padding/2)*height));
	}
	
	public void onMouseOver() {
		System.out.println("Mouse over");
		color = Color.DARK_GRAY;
	}
	
	public void onMouseDown() {
		System.out.println("Mouse down");
		color = new Color(110, 130, 130);
	}

	public void onMouseOut() {
		System.out.println("Mouse out");
		color = Color.GRAY;
	}
	
	@Override
	public void onMouseMoved(int dx, int dy) {
		
	}

	@Override
	public void onMouseUp() {
		System.out.println("Mouse up");
		color = Color.GRAY;
	}
}
