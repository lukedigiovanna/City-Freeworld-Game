package display.component;

import java.awt.*;

import main.Program;

public class MainScreenButton extends Button {
	
	public MainScreenButton(String title, int x, int y) {
		super(title,x,y,0,(int)(Program.DISPLAY_HEIGHT/15),Component.FORM_CENTER);
	}
	
	private float fontSize = 0.05f;
	
	public void draw(Graphics2D g) {
		int fs = (int)(fontSize*Program.DISPLAY_HEIGHT);
		g.setFont(new Font(Program.FONT_FAMILY,Font.BOLD,fs));
		String str = "< "+this.getText()+" >";
		int width = g.getFontMetrics().stringWidth(str);
		this.setDimension(width,g.getFontMetrics().getHeight());
		g.setColor(new Color(0,0,200,125));
		g.drawString(str, x-1, y+this.getHeight()-1);
		g.setColor(Color.BLACK);
		g.drawString(str, x, y+this.getHeight());
	}
	
	public void onMouseOver() {
		fontSize = 0.06f;
	}
	
	public void onMouseDown() {
	}

	public void onMouseOut() {
		fontSize = 0.05f;
	}

	@Override
	public void onMouseUp() {
	}

	@Override
	public void onMouseDragged(int dx, int dy) {
		System.out.println(dx+","+dy);
	}
}
