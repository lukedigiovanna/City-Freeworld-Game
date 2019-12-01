package display;

import java.awt.*;
import java.awt.event.KeyEvent;

import main.Program;
import display.component.*;
import display.component.Button;
import display.component.Component;

public class UpdateNotesDisplay extends Display {
	private String[] notes = {
		"Implemented VSync",
		"Added player sprite/animations",
		"Fixed collision with walls",
		"Added point and shoot with bullets",
		"Implementing data files to store regions",
		"Newer, more detailed tile art"
	};
	
	private Button backButton = new Button("Back to menu",ptpX(0.025f),ptpY(0.025f),0,0,Component.FORM_LEFT) {
		@Override
		public void onMouseDown() {}

		@Override
		public void onMouseUp() {
			DisplayController.setScreen(DisplayController.Screen.MAIN);
		}

		@Override
		public void onMouseMoved(int dx, int dy) {}

		@Override
		public void onMouseOver() {
			c = Color.gray;
		}

		@Override
		public void onMouseOut() {
			c = Color.white;
		}
		
		Color c = Color.white;
		Font f = new Font(Program.FONT_FAMILY,Font.BOLD,ptpY(0.025f));
		public void draw(Graphics2D g) {
			g.setFont(f);
			setDimension(g.getFontMetrics().stringWidth(this.getText()),g.getFontMetrics().getHeight());
			g.setColor(c);
			g.drawString(getText(), x, y+getHeight());
		}
	};
	
	public UpdateNotesDisplay() {
		super();
		add(backButton);
	}
	
	@Override
	public void draw(Graphics2D g) {
		if (Program.keyboard.keyPressed(KeyEvent.VK_ESCAPE))
			DisplayController.setScreen(DisplayController.Screen.MAIN);
		
		fillBackground(g, Color.BLACK);
		g.setColor(Color.GRAY);
		g.setFont(new Font(Program.FONT_FAMILY,Font.BOLD,ptpY(0.1f)));
		drawText(g,"UPDATE NOTES",0.5f,0.15f,Display.CENTER_ALIGN);
		g.setColor(Color.LIGHT_GRAY);
		g.setFont(new Font(Program.FONT_FAMILY,Font.ITALIC,ptpY(0.05f)));
		drawText(g,"Version "+Program.getVersionString(),0.5f,0.20f,Display.CENTER_ALIGN);
		float size = 0.03f;
		float y = 0.3f;
		g.setColor(Color.WHITE);
		g.setFont(new Font(Program.FONT_FAMILY,Font.PLAIN,ptpY(size)));
		for (String s : notes) {
			int i = 0;
			String str = "";
			while (s.charAt(0) == ' ') {
				str+=" ";
				s = s.substring(1);
			}
			str+="* "+s;
			drawText(g,str,0.1f,y,Display.LEFT_ALIGN);
			y+=size;
		}
		
		backButton.draw(g);
	}

	@Override
	public void set() {
		
	}
}
