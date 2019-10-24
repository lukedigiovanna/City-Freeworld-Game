package main;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.*;

public class Panel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private BufferedImage screen;
	
	public Panel() {
		screen = new BufferedImage(Program.DISPLAY_WIDTH,Program.DISPLAY_HEIGHT,BufferedImage.TYPE_INT_ARGB);
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(screen, 0, 0, this.getWidth(), this.getHeight(), null);
	}
}
