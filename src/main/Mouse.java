package main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Mouse {
	private boolean mouseDown = false, leftDown = false, middleDown = false, rightDown = false;
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
				if (SwingUtilities.isLeftMouseButton(e))
					leftDown = true;
				if (SwingUtilities.isMiddleMouseButton(e))
					middleDown = true;
				if (SwingUtilities.isRightMouseButton(e))
					rightDown = true;
			}
			
			public void mouseReleased(MouseEvent e) {
				mouseDown = false;
				if (SwingUtilities.isLeftMouseButton(e))
					leftDown = false;
				if (SwingUtilities.isMiddleMouseButton(e))
					middleDown = false;
				if (SwingUtilities.isRightMouseButton(e))
					rightDown = false;
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
	
	public static final int LEFT_BUTTON = 0, MIDDLE_BUTTON = 1, RIGHT_BUTTON = 2;
	
	public boolean isMouseDown(int button) {
		switch (button) {
		case LEFT_BUTTON:
			return leftDown;
		case MIDDLE_BUTTON:
			return middleDown;
		case RIGHT_BUTTON:
			return rightDown;
		}
		return false;
	}
}
