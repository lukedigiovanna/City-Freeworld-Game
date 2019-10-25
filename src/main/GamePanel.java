package main;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.*;

import misc.Color8;
import misc.ImageTools;

public class GamePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private BufferedImage screen;
	private Graphics2D graphics;
	
	public GamePanel() {
		this.setFocusable(true);
		screen = new BufferedImage(Program.DISPLAY_WIDTH,Program.DISPLAY_HEIGHT,BufferedImage.TYPE_INT_ARGB);
		graphics = screen.createGraphics();
		
		Thread repaintThread = new Thread(new Runnable() {
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
		//repaintThread.start();
	}
	
	public Graphics2D getGraphics() {
		return screen.createGraphics();
	}
	
	public void paintComponent(Graphics g) {
		graphics.setColor(Color.RED);
		graphics.fillRect(0, 0, 50, 50);
		g.drawImage(screen, 0, 0, this.getWidth(), this.getHeight(), null);
	}
}
