package main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JPanel;

public class Mouse {
	private boolean mouseDown = false;
	private MouseEvent lastMouse = null;
	private JPanel panel;
	
	public Mouse(JPanel p) {
		this.panel = p;
		
		panel.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				mouseDown = true;
			}
			
			public void mouseReleased(MouseEvent e) {
				mouseDown = false;
			}
		});
		
		panel.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e) {
				lastMouse = e;
			}
		});
	}
	
	/**
	 * @return the x coordinate on the display (not the panel)
	 */
	public int getX() {
		return (int)((double)lastMouse.getX()/panel.getWidth()*Program.DISPLAY_WIDTH);
	}
	
	public int getY() {
		return (int)((double)lastMouse.getY()/panel.getHeight()*Program.DISPLAY_HEIGHT);
	}
	
	public boolean isMouseDown() {
		return this.mouseDown;
	}
}
