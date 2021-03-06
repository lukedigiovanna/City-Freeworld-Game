package display.component;

import java.awt.*;

public abstract class Button extends Component {

	protected String title = "button";
	
	public Button(String s, int x, int y, int w, int h, int formation) {
		super(x,y,w,h,formation);
		title = s;
	}
	
	public Button(String s, int x, int y, int w, int h) {
		this(s,x,y,w,h,Component.FORM_LEFT);
	}
	
	public Button(String s, int x, int y, int formation) {
		this(s,x,y,0,0,formation);
	}
	
	public Button(String s, int x, int y) {
		this(s,x,y,0,0,Component.FORM_LEFT);
	}
	
	public Button(String s) {
		this(s,0,0,0,0);
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
	
	//these methods likely wont be implemented in button subclasses, so define them here as nothing
	
	public void onMouseMoved(int dx, int dy) {
		
	}
	
	public void onMouseDragged(int dx, int dy) {
		
	}
	
	public void onMouseDownOut() {
		
	}
}
