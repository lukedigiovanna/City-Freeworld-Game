package regionEditor;

import javax.swing.*;

import misc.Color8;

import java.awt.*;
import java.awt.image.BufferedImage;

public class EditorPanel extends JPanel {
	public static final int DISPLAY_WIDTH = 1000, DISPLAY_HEIGHT = 800; 
	
	private BufferedImage image;
	
	public EditorPanel() {
		this.setFocusable(true);
		
		Thread repaintThread = new Thread( new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(50);
						repaint();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		repaintThread.start();
		
		Thread updateThread = new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(50);
						update();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		updateThread.start();
	}
	
	public void repaint() {
		if (image == null)
			image = new BufferedImage(DISPLAY_WIDTH,DISPLAY_HEIGHT,BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		g.setColor(Color8.LIGHT_GRAY);
		g.fillRect(0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT);
		//draw a border
		int padding = 15;
		g.setColor(Color8.GRAY);
		g.fillRect(0, 0, DISPLAY_WIDTH, padding);
		g.fillRect(DISPLAY_WIDTH-padding, 0, padding, DISPLAY_HEIGHT);
		g.fillRect(0, DISPLAY_HEIGHT-padding, DISPLAY_WIDTH, padding);
		g.fillRect(0, 0, padding, DISPLAY_HEIGHT);
		super.repaint();
	}
	
	public void update() {
		
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
	}
}
