package display.component;

import java.awt.Graphics2D;

import main.Program;

public abstract class Component {
	protected int x, y, width, height;
	
	public Component(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
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
		//first check if the mouse is inside the component area
		int mx = Program.mouse.getX(), my = Program.mouse.getY();
		if (active && mx > x && mx < x + width && my > y && my < y + height) {
			if (Program.mouse.isMouseDown()) {
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
}
