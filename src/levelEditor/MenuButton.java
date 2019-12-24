package levelEditor;

import java.awt.*;
import java.awt.image.BufferedImage;

import display.component.Button;

public class MenuButton extends Button {

	private static final Font FONT = new Font("Consolas",Font.PLAIN,18);
	private static final Graphics2D graphics = (new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB)).createGraphics();
	
	private Runnable onClick;
	
	public MenuButton(String s, int x, int y, Runnable onClick) {
		super(s);
		this.onClick = onClick;
		this.setX(x);
		this.setY(y);
		graphics.setFont(FONT);
		this.setWidth(graphics.getFontMetrics().stringWidth(s)+20);
		this.setHeight(graphics.getFontMetrics().getHeight()+6);
	}
	
	private Color color = Color.WHITE;
	
	@Override
	public void draw(Graphics2D g) {
		g.setColor(color);
		g.fillRoundRect(x, y, width, height, 6, 6);
		g.setColor(Color.BLACK);
		g.drawRoundRect(x, y, width, height, 6, 6);
		g.setFont(FONT);
		g.drawString(this.getText(), x + width/2 - g.getFontMetrics().stringWidth(this.getText())/2, y+height/2+7);
	}

	@Override
	public void onMouseDown() {
		color = Color.GRAY;
	}

	@Override
	public void onMouseUp() {
		onClick.run();
		color = Color.LIGHT_GRAY;
	}

	@Override
	public void onMouseOver() {
		color = Color.LIGHT_GRAY;
	}

	@Override
	public void onMouseOut() {
		color = Color.WHITE;
	}
}
