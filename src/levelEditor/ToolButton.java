package levelEditor;

import java.awt.*;

import display.component.Button;

public class ToolButton extends Button {
	private Tool tool;
	
	private EditorPanel panel;
	
	private boolean enabled = true;
	
	public ToolButton(Tool tool, int x, int y, EditorPanel panel) {
		super("",x,y,50,50);
		this.tool = tool;
		this.panel = panel;
	}

	private Color color = Color.DARK_GRAY;
	
	private int timer = 0;
	
	public void enable() {
		this.enabled = true;
	}
	
	public void disable() {
		this.enabled = false;
	}
	
	public boolean isEnabled() {
		return this.enabled;
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.setColor(color);
		if (panel.getTool() == tool) 
			g.setColor(Color.CYAN);
		g.fillRoundRect(x, y, width, height,5,5);
		if (enabled)
			g.setColor(Color.GRAY);
		else
			g.setColor(Color.DARK_GRAY);
		g.fillRect(x+4,y+4,width-8,height-8);
		g.drawImage(tool.img, x+4, y+4, width-8, height-8, null);
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial",Font.BOLD,12));
		g.drawString(tool.name, x+width/2-g.getFontMetrics().stringWidth(tool.name)/2, y+height+20);
		if (timer > 15) {
			g.setColor(Color.DARK_GRAY);
			g.drawString(tool.tip, x+width/2-g.getFontMetrics().stringWidth(tool.tip)/2, y-10);
		}
		if (this.isMouseOver()) {
			timer++;	
		}
	}
	
	@Override
	public void onMouseDown() {
		color = Color.WHITE;
	}

	@Override
	public void onMouseUp() {
		color = Color.WHITE;
		panel.setTool(this.tool);
	}

	@Override
	public void onMouseOver() {
		color = Color.GRAY;
	}

	@Override
	public void onMouseOut() {
		color = Color.DARK_GRAY;
		timer = 0;
	}
}
