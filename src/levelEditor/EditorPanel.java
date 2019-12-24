package levelEditor;

import javax.swing.*;

import display.Animation;
import display.TexturePack;
import main.Mouse;
import main.Program;
import misc.Color8;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class EditorPanel extends JPanel {
	
	private BufferedImage screen;
	private List<Animation> tiles;
	private Mouse mouse;
	
	private List<MenuButton> menuButtons;
	
	public EditorPanel() {
		screen = new BufferedImage(Program.DISPLAY_WIDTH,Program.DISPLAY_HEIGHT,BufferedImage.TYPE_INT_ARGB);
		
		mouse = new Mouse(this, Program.DISPLAY_WIDTH, Program.DISPLAY_HEIGHT);
		
		menuButtons = new ArrayList<MenuButton>();
		addMenuButton("New Region",new Runnable() {
			public void run() {
				
			}
		});
		addMenuButton("Save", new Runnable() {
			public void run() {
				
			}
		});
		
		TexturePack pack = TexturePack.DEFAULT;
		tiles = new ArrayList<Animation>();
		for (int i = 0; i < pack.getNumberOfTiles(); i++)
			tiles.add(new Animation(pack.getTileImages(i),pack.getFrameRate(i)));
		
		Thread redrawThread = new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(50);
					} catch (Exception e) {
						
					}
					redraw();
				}
			}
		});
		redrawThread.start();
	}
	
	private void addMenuButton(String name, Runnable onClick) {
		int x = 220;
		for (MenuButton b : menuButtons) 
			x += b.getWidth()+15;
		MenuButton mb = new MenuButton(name, x, 15, onClick);
		menuButtons.add(mb);
	}
	
	private int curTile = 0;
	
	public void redraw() {
		Graphics2D g = screen.createGraphics();
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, Program.DISPLAY_WIDTH, Program.DISPLAY_HEIGHT);
		//left bar lists out each tile
		g.setColor(Color.RED.darker());
		g.fillRect(0, 0, 200, Program.DISPLAY_HEIGHT);
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(10));
		g.drawRect(0, 0, 200, Program.DISPLAY_HEIGHT);
		g.setColor(Color.DARK_GRAY);
		g.setFont(new Font("Arial",Font.BOLD,30));
		g.drawString("TILES", 100-g.getFontMetrics().stringWidth("TILES")/2, 50);
		int tileSize = 60;
		for (int i = 0; i < tiles.size(); i++) {
			int x = 60 - tileSize/2;
			if (i % 2 == 1)
				x += 80;
			int y = (i / 2) * (tileSize+10) + 70;
			g.drawImage(tiles.get(i).getCurrentFrame(),x,y,tileSize,tileSize,null);
			if (curTile == i) {
				g.setColor(Color.CYAN);
				g.setStroke(new BasicStroke(4));
				g.drawRect(x-5, y-5, tileSize+10, tileSize+10);
			}
			//check if mouse is clicked over it
			if (mouse.isMouseDown()) {
				if (mouse.getX() > x && mouse.getX() < x + tileSize && mouse.getY() > y && mouse.getY() < y + tileSize) {
					curTile = i;
				}
			}
		}
		for (MenuButton b : menuButtons) {
			b.check(mouse);
			b.draw(g);
		}
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(screen,0,0,getWidth(),getHeight(),null);
	}
}
