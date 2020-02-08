package world.regions;

import java.awt.image.BufferedImage;
import java.awt.*;

import entities.Entity;

public class Minimap {
	private BufferedImage image;
	private Entity focus;
	
	private int size = 30; //the width of the map
	
	private static final int PIXEL_SIZE = 200;
	
	public Minimap(Entity focus) {
		this.focus = focus;
		
		this.image = new BufferedImage(PIXEL_SIZE,PIXEL_SIZE,BufferedImage.TYPE_INT_ARGB);
	}
	
	private float margin = 0.05f;
	
	public void draw() {
		Graphics2D g = image.createGraphics();
		//fill in the background
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, PIXEL_SIZE, PIXEL_SIZE);
		//draw in the tiles
		float fx = focus.getX(), fy = focus.getY();
		float lx = fx - size / 2, ly = fy - size/2;
		int sx = (int)(lx - 1), sy = (int)(ly - 1);
		int unitSize = (int)(1.0f/size*PIXEL_SIZE);
		CellGrid grid = this.focus.getRegion().getGrid();
		for (int x = sx; x < sx + size + 2; x++) {
			for (int y = sy; y < sy + size + 2; y++) {
				Cell c = grid.get(x, y);
				if (c != null) {
					BufferedImage img = c.getImage();
					int px = (int)((x-lx)/size*PIXEL_SIZE), 
						py = (int)((y-ly)/size*PIXEL_SIZE);
					g.drawImage(img, px, py, unitSize+1, unitSize+1, null);
				}
			}
		}
		
		//draw the focused player as a yellow dot with an arrow in the direction of movement
		int px = (int)((fx-lx)/size*PIXEL_SIZE),
			py = (int)((fy-ly)/size*PIXEL_SIZE);
		g.setColor(Color.YELLOW);
		g.fillOval(px, py, unitSize, unitSize);
		
		//draw the border
		int pm = (int)(margin * PIXEL_SIZE);
		g.setStroke(new BasicStroke(pm));
		g.setColor(Color.BLACK);
		g.drawRect(pm/2, pm/2, PIXEL_SIZE-pm, PIXEL_SIZE-pm);
	}
	
	public BufferedImage getImage() {
		return this.image;
	}
}
