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
		repaintThread.start();
	}
	
	public Graphics2D getGraphics() {
		return graphics;
	}
	
	public void repaint() {
		if (graphics == null) {
			super.repaint();
			return;
		}
		Graphics g = graphics;
		Image i = ImageTools.getImage("city.jpg");
		i = ImageTools.convertTo8Bit(i);
		g.drawImage(i, 0, 0, screen.getWidth(), screen.getHeight(), null);
		g.setColor(Color.RED);
		g.fillRect(0, 0, 50, 50);
		super.repaint();
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(screen, 0, 0, this.getWidth(), this.getHeight(), null);
	}
}
