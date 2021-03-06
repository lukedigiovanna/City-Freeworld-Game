package main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Mouse {
	private boolean mouseDown = false, leftDown = false, middleDown = false, rightDown = false;
	private MouseEvent lastMouse = null;
	private int screenWidth, screenHeight;
	private JPanel panel;
	
	private int wheelRotation = 0;
	private long lastScrollTime = 0;
	
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
		
		panel.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				wheelRotation = e.getWheelRotation();
				lastScrollTime = System.currentTimeMillis();
			}
		});
		
		Thread mouseControlThread = new Thread(new Runnable() {
			public void run() {
				while (true) {
					if (System.currentTimeMillis() - lastScrollTime > 100)
						wheelRotation = 0;
					try {
						Thread.sleep(100);
					} catch (Exception e) {
						
					}
				}
			}
		});
		mouseControlThread.start();
	}
	
	public int getWheelRotation() {
		int ret = wheelRotation;
		wheelRotation = 0;
		return ret;
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
	
	/**
	 * Continuously returns true
	 * @return
	 */
	public boolean isMouseDown() {
		return this.mouseDown;
	}
	
	/** 
	 * Only returns true once
	 * @retrun 
	 */
	public boolean isMousePressed() {
		if (isMouseDown()) {
			setIsMouseDown(false);
			return true;
		} else 
			return false;
	}
	
	public boolean isMousePressed(int button) {
		if (isMouseDown(button)) {
			setIsMouseDown(button,false);
			return true;
		} else
			return false;
	}
	
	public void setIsMouseDown(boolean boo) {
		this.mouseDown = boo;
	}
	
	public void setIsMouseDown(int button, boolean boo) {
		switch (button) {
		case LEFT_BUTTON:
			leftDown = boo;
			break;
		case MIDDLE_BUTTON:
			middleDown = boo;
			break;
		case RIGHT_BUTTON:
			rightDown = boo;
			break;
		}
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
