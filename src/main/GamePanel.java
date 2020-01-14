package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Collections;

import javax.swing.*;

import display.DisplayController;
import misc.Color8;
import misc.ImageTools;
import misc.MathUtils;

public class GamePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private BufferedImage screen;
	
	public GamePanel() {
		this.setFocusable(true);
		this.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, Collections.emptySet());
		
		screen = new BufferedImage(Program.DISPLAY_WIDTH,Program.DISPLAY_HEIGHT,BufferedImage.TYPE_INT_ARGB_PRE);
	
		Thread repaintThread = new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						long first = System.currentTimeMillis();
						int targetFPS = Integer.parseInt(Settings.getSetting("max_fps"));
						int targetRefresh = 1000/targetFPS;
						repaint();
						long elapsed = System.currentTimeMillis()-first;
						long wait = targetRefresh;
						if (Settings.getSetting("vsync_enabled").contentEquals("true"))
							wait = (long)MathUtils.floor(10, targetRefresh-elapsed-1);
						Thread.sleep(wait);
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
