package display.component;

import java.awt.*;

public class Button extends Component {

	private String title = "button";
	
	public Button(String s, int x, int y, int w, int h) {
		super(x,y,w,h);
		title = s;
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(x, y, width, height);
		g.setFont(new Font("Arial",Font.PLAIN,height));
		g.setColor(Color.WHITE);
		g.drawString(title, x+width/2-g.getFontMetrics().stringWidth(title)/2, y + height-5);
	}

	@Override
	public void onMouseDown() {
		
	}

	@Override
	public void onMouseUp() {
		
	}

	@Override
	public void onMouseMoved(int dx, int dy) {
		
	}

	@Override
	public void onMouseOver() {
		
	}
}
