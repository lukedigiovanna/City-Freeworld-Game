package main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JPanel;

public class Mouse {
	private boolean mouseDown = false;
	private MouseEvent lastMouse = null;
	private int screenWidth, screenHeight;
	private JPanel panel;
	
	public Mouse(JPanel p, int sw, int sh) {
		this.panel = p;
		this.screenWidth = sw;
		this.screenHeight = sh;
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
			
			public void mouseDragged(MouseEvent e) {
				lastMouse = e;
			}
		});
		
	}
	
	/**
	 * @return the x coordinate on the display (not the panel)
	 */
	public int getX() {
		if (lastMouse == null)
			return 0;
		return (int)((double)lastMouse.getX()/panel.getWidth()*screenWidth);
	}
	
	public int getY() {
		if (lastMouse == null)
			return 0;
		return (int)((double)lastMouse.getY()/panel.getHeight()*screenHeight);
	}
	
	public boolean isMouseDown() {
		return this.mouseDown;
	}
}
