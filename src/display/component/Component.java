package display.component;

import java.awt.Graphics2D;

import main.Mouse;
import main.Program;

public abstract class Component {
	public static final int FORM_CENTER = 0, FORM_LEFT = 1, FORM_RIGHT = 2;
	
	protected int x, y, width, height;
	
	private int formation = FORM_LEFT;
	
	public Component(int x, int y, int w, int h, int formation) {
		this.width = w;
		this.height = h;
		this.y = y;
		this.x = x;
		switch (formation) {
		case FORM_CENTER:
			this.x = x-width/2;
			break;
		case FORM_RIGHT:
			this.x = x-width;
		}
		this.formation = formation;
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
		setDimension(width,height);
	}
	
	public void setHeight(int height) {
		setDimension(width,height);
	}
	
	public void setDimension(int width, int height) {
		switch (formation) {
		case FORM_CENTER:
			this.x+=this.width/2;
			this.y+=this.height/2;
			break;
		case FORM_RIGHT:
			this.x+=this.width;
			this.y+=this.height;
		}
		this.width = width;
		this.height = height;
		switch (formation) {
		case FORM_CENTER:
			this.x-=this.width/2;
			this.y-=this.height/2;
			break;
		case FORM_RIGHT:
			this.x-=this.width;
			this.y-=this.height;
		}
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
}
