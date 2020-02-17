package display.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import display.Display;
import display.DisplayController;
import main.Program;

public class BackButton extends Button {
	public BackButton(float x, float y) {
		super("Back to menu",Display.ptpX(x),Display.ptpY(y),0,0,Component.FORM_LEFT); 
	
	}
	@Override
	public void onMouseDown() {}
	
	@Override
	public void onMouseUp() {
		DisplayController.setScreen(DisplayController.Screen.MAIN);
	}
	
	@Override
	public void onMouseMoved(int dx, int dy) {}
	
	@Override
	public void onMouseOver() {
		c = Color.gray;
	}
	
	@Override
	public void onMouseOut() {
		c = Color.white;
	}
	
	Color c = Color.white;
	Font f = new Font(Program.FONT_FAMILY,Font.BOLD,Display.ptpY(0.025f));
	public void draw(Graphics2D g) {
		g.setFont(f);
		setDimension(g.getFontMetrics().stringWidth(this.getText()),g.getFontMetrics().getHeight());
		g.setColor(c);
		g.drawString(getText(), x, y+getHeight());
	}
}
