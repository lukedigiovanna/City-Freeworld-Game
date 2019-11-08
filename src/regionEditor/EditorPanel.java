package regionEditor;

import javax.swing.*;

import main.Mouse;
import main.Program;
import misc.Color8;

import java.awt.*;
import java.awt.image.BufferedImage;

public class EditorPanel extends JPanel {
	public static final int DISPLAY_WIDTH = Program.DISPLAY_WIDTH, DISPLAY_HEIGHT = Program.DISPLAY_HEIGHT; 
	
	private BufferedImage image;
	
	private Mouse mouse;
	
	public EditorPanel() {
		this.setFocusable(true);
		
		mouse = new Mouse(this);
		
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
		Graphics2D g = image.createGraphics();
		ScreenController.draw(g,mouse);
		super.repaint();
	}
	
	public void update() {
		
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
	}
}
