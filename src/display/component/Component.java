package display.component;

import java.awt.Graphics2D;

import main.Mouse;
import main.Program;

public abstract class Component {
	public static final int FORM_CENTER = 0, FORM_LEFT = 1, FORM_RIGHT = 2;
	
	protected int x, y, width, height;
	
	public Component(int x, int y, int w, int h, int formation) {
		adjust(x,y,w,h,formation);
	}
	
	private boolean active = true;
	
	public void activate() {
		active = true;
	}
	
	public void deactivate() {
		active = false;
	}
	
	public abstract void draw(Graphics2D g);
	
	public abstract void onMouseDown();
	public abstract void onMouseUp();
	public abstract void onMouseMoved(int dx, int dy);
	public abstract void onMouseOver();
	public abstract void onMouseOut();
	
	private boolean mouseDown = false;
	private boolean mouseOver = false;
	private int lastX = 0, lastY = 0;
	/**
	 * captures some mouse info and calls methods based on that
	 */
	public void check() {
		check(Program.mouse);
	}
	
	public void check(Mouse mouse) {
		if (mouse == null)
			return;
		//first check if the mouse is inside the component area
		int mx = mouse.getX(), my = mouse.getY();
		if (active && mx > x && mx < x + width && my > y && my < y + height) {
			if (mouse.isMouseDown()) {
				if (!mouseDown) {
					onMouseDown();
				}
				mouseDown = true;
			} else {
				if (mouseDown) {
					onMouseUp();
				}
				mouseDown = false;
			}
			if (!mouseOver)
				onMouseOver();
			mouseOver = true;
		} else if (active) {
			if (mouseOver)
				onMouseOut();
			mouseOver = false;
			mouseDown = false;
		}
	}	
	public void adjust(int x, int y, int width, int height, int formation) {
		this.width = width;
		this.height = height;
		switch (formation) {
		case FORM_LEFT:
			this.x = x;
			this.y = y;
			break;
		case FORM_CENTER:
			this.x = x-width/2;
			this.y = y;
			break;
		case FORM_RIGHT:
			this.x = x - width;
			this.y = y;
			break;
		default:
			this.x = x;
			this.y = y;
		}
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
}
