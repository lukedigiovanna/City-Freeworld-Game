package tileEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import main.*;

/**
 * 
 */
public class TileEditor extends JPanel  {
	public final static int DISPLAY_WIDTH = 600, DISPLAY_HEIGHT = 600;
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Tile Editor");
		frame.setSize(600,600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new TileEditor());
		frame.setVisible(true);
	}
	
	private Mouse mouse;
	
	private BufferedImage screen;
	
	public TileEditor() {
		screen = new BufferedImage(DISPLAY_WIDTH,DISPLAY_HEIGHT,BufferedImage.TYPE_INT_ARGB);
		mouse = new Mouse(this,DISPLAY_WIDTH,DISPLAY_HEIGHT);
		
		Thread repaintThread = new Thread(new Runnable() {
			public void run() {
				while (true) { //while the program is running
					try {
						Thread.sleep(50);
					} catch (Exception e) {}
					draw();
					repaint();
				}
			}
		});
		repaintThread.start();
	}
	
	public void draw() {
		Graphics2D g = screen.createGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT);
	}
	
	public void paintComponent(Graphics g) {
		//draw the screen image
		g.drawImage(screen, 0, 0, getWidth(), getHeight(), null);
	}
	
}
