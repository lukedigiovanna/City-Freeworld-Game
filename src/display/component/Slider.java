package display.component;

import java.awt.Color;
import java.awt.Graphics2D;

import misc.MathUtils;

public class Slider extends Component {

	private float rawVal = 0.0f;
	
	public Slider(int x, int y, int w, int h, int formation) {
		super(x, y, w, h, formation);
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.fillRect(x, y, width, height);
		int barX = x+(int)(rawVal * width);
		g.setColor(Color.BLACK);
		g.fillRect(barX-10-2, y-2, 20+4, height+4);
		g.setColor(Color.GRAY);
		g.fillRect(barX-10, y, 20, height);
	}

	@Override
	public void onMouseDown() {
		
	}

	@Override
	public void onMouseUp() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMouseMoved(int dx, int dy) {
		// TODO Auto-generated method stub
		
	}
	
	public void onMouseDragged(int dx, int dy) {
		rawVal += (float)dx / width;
		rawVal = MathUtils.clip(0, 1, rawVal);
	}

	@Override
	public void onMouseOver() {
		
	}

	@Override
	public void onMouseOut() {
		
	}
	
	public void onMouseDownOut() {
		
	}

}
