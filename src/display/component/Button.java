package display.component;

import java.awt.*;

public abstract class Button extends Component {

	private String title = "button";
	
	public Button(String s, int x, int y, int w, int h) {
		super(x,y,w,h);
		title = s;
	}
	
	@Override
	public void draw(Graphics2D g) {
		//default draw method
		g.setColor(Color.BLACK);
		g.fillRect(x, y, width, height);
		g.setFont(new Font("Arial",Font.PLAIN,height));
		g.setColor(Color.WHITE);
		g.drawString(title, x+width/2-g.getFontMetrics().stringWidth(title)/2, y + height-5);
	}
	
	public String getText() {
		return title;
	}
}
