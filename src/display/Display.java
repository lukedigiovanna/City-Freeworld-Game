package display;

import java.awt.*;
import java.util.List;
import java.util.*;

import display.component.*;
import display.component.Component;
import main.Program;
import misc.ImageTools;

/**
 * abstract class for displays
 *
 */
public abstract class Display {
	public abstract void draw(Graphics2D g);
	public abstract void set(); //called when the controller sets the display
	
	private List<Component> components;
	
	public Display() {
		components = new ArrayList<Component>();
	}
	
	public void add(Component component) {
		components.add(component);
	}
	
	public void checkComponents() {
		for (Component c : components) {
			c.check();
		}
	}
	
	public static void fillBackground(Graphics2D g, Color color) {
		g.setColor(color);
		g.fillRect(0, 0, Program.DISPLAY_WIDTH, Program.DISPLAY_HEIGHT);
	}
	
	public static void fillBackground(Graphics2D g, Image image) {
		g.drawImage(image, 0, 0, Program.DISPLAY_WIDTH, Program.DISPLAY_HEIGHT, null);
	}
	
	public static final int CENTER_ALIGN = 0, LEFT_ALIGN = 1, RIGHT_ALIGN = 2;
	
	/**
	 * draws text a specific way
	 * @param g Graphics reference
	 * @param str String to draw
	 * @param x x position decided by alignment
	 * @param y bottom of the text
	 * @param alignment integer value that determines alignment: LEFT, CENTER, RIGHT
	 */
	public static void drawText(Graphics g, String str, int x, int y, int alignment) {
		switch (alignment) {
		case CENTER_ALIGN:
			g.drawString(str, x-g.getFontMetrics().stringWidth(str)/2, y);
			break;
		case LEFT_ALIGN:
			g.drawString(str, x, y);
			break;
		case RIGHT_ALIGN:
			g.drawString(str, x-g.getFontMetrics().stringWidth(str), y);
			break;
		}
	}
	
	public static void drawText(Graphics g, String str, float x, float y, int alignment) {
		drawText(g,str,ptpX(x),ptpY(y),alignment);
	}
	
	public static void drawText(Graphics2D g, String str, CustomFont cf, int size, int x, int y, int alignment) {
		int startX = x;
		int spacing = 5;
		int length = str.length()*(size+spacing);
		switch (alignment) {
		case CENTER_ALIGN:
			startX-=length/2;
			break;
		case LEFT_ALIGN:
			startX = x;
			break;
		case RIGHT_ALIGN:
			startX-=length;
		}
		for (int i = 0; i < str.length(); i++) {
			g.drawImage(ImageTools.colorscale(cf.getChar(str.charAt(i)),g.getColor()), startX, y, size, size, null);
			startX+=size+spacing;
		}
	}
	
	public static void drawText(Graphics2D g, String str, CustomFont cf, float size, float x, float y, int alignment) {
		drawText(g,str,cf,ptpY(size),ptpX(x),ptpY(y),alignment);
	}
	
	/**
	 * Primes the passing graphics object to draw 
	 * @param g
	 * @param rotation
	 */
	public static void setRotation(Graphics2D g, double rotation) {
		
	}
	
	public static void clearRotation(Graphics2D g) {
		
	}
	
	public static int ptpX(float x) {
		return (int)(x*Program.DISPLAY_WIDTH);
	}
	
	public static int ptpY(float y) {
		return (int)(y*Program.DISPLAY_HEIGHT);
	}
	
}
