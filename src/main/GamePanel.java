package main;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.*;

import display.DisplayController;
import misc.Color8;
import misc.ImageTools;

public class GamePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private BufferedImage screen;
	
	public GamePanel() {
		this.setFocusable(true);
		screen = new BufferedImage(Program.DISPLAY_WIDTH,Program.DISPLAY_HEIGHT,BufferedImage.TYPE_INT_ARGB_PRE);
	
		Thread repaintThread = new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000/Integer.parseInt(Settings.getSetting("max_fps")));
						repaint();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		repaintThread.start();
	}
	
	public void repaint() {
		if (screen != null) {
			Graphics2D gf = screen.createGraphics();
			DisplayController.redraw(gf);
		}
		super.repaint();
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(screen, 0, 0, this.getWidth(), this.getHeight(), null);
	}
}
