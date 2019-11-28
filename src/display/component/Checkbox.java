package display.component;

import java.awt.Color;
import java.awt.Graphics2D;

public abstract class Checkbox extends Component {

	private boolean value = false;
	
	public Checkbox(int x, int y, int w, int h) {
		super(x, y, w, h, Component.FORM_LEFT);
	}

	private float padding = 0.075f;
	private int horizontalPadding = (int)(padding * height), verticalPadding = (int)(padding * width); 
	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(x, y, width, height);
		g.setColor(Color.WHITE);
		g.fillRect(x+verticalPadding, y+horizontalPadding, width-verticalPadding*2, height-horizontalPadding*2);
		g.setColor(Color.WHITE);
		if (mouseOver)
			g.setColor(Color.LIGHT_GRAY);
		if (value) 
			g.setColor(Color.BLACK);
		g.fillRect(x+verticalPadding*2, y+horizontalPadding*2, width-verticalPadding*4, height-horizontalPadding*4);
		
//		if (mouseOver) {
//			g.setColor(Color.cyan.brighter());
//			g.drawRect(x-verticalPadding, y-horizontalPadding, width + verticalPadding*2, height + horizontalPadding * 2);
//		}
	}
	
	public abstract void onEnable();
	public abstract void onDisable();
	
	public boolean selected() {
		return value;
	}

	@Override
	public void onMouseDown() {
		value = !value;
		if (value)
			onEnable();
		else
			onDisable();
	}

	@Override
	public void onMouseUp() {
		
	}

	@Override
	public void onMouseMoved(int dx, int dy) {
		
	}

	private boolean mouseOver = false;
	@Override
	public void onMouseOver() {
		mouseOver = true;
	}

	@Override
	public void onMouseOut() {
		mouseOver = false;
	}

	@Override
	public void onMouseDragged(int dx, int dy) {	
	}
	
	public void onMouseDownOut() {
		
	}
}
