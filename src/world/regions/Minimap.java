package world.regions;

import java.awt.image.BufferedImage;
import java.awt.*;

import entities.Entity;
import misc.ImageTools;

public class Minimap {
	private BufferedImage image, blanked;
	private Entity focus;
	
	private int size = 30; //the width of the map
	
	private static final int PIXEL_SIZE = 250;
	
	public Minimap(Entity focus) {
		this.focus = focus;
		
		this.image = new BufferedImage(PIXEL_SIZE,PIXEL_SIZE,BufferedImage.TYPE_INT_ARGB);
		this.blanked = new BufferedImage(PIXEL_SIZE,PIXEL_SIZE,BufferedImage.TYPE_INT_ARGB);
		Graphics g = this.blanked.getGraphics();
		g.setColor(Color.WHITE);
		g.fillOval(2,2,PIXEL_SIZE-4,PIXEL_SIZE-4);
	}
	
	private static final int BLANK_RGB = (new Color(0,0,0,0)).getRGB();
	
	private float margin = 0.05f;
	
	public void draw() {
		if (focus == null || focus.getRegion() == null) { // i.e. they were removed from the game (death or whatever)
			return; //don't draw
		}
		
		Graphics2D g = image.createGraphics();
		//fill in the background
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, PIXEL_SIZE, PIXEL_SIZE);
		float pps = PIXEL_SIZE/size; //pixels per unit
		//draw in the tiles
		float fx = focus.getX(), fy = focus.getY();
		float lx = fx - size / 2, ly = fy - size/2;
		int sx = (int)(lx - 1), sy = (int)(ly - 1);
		CellGrid grid = this.focus.getRegion().getGrid();
		for (int x = sx; x < sx + size + 2; x++) {
			for (int y = sy; y < sy + size + 2; y++) {
				Cell c = grid.get(x, y);
				if (c != null) {
					BufferedImage img = c.getImage();
					int px = (int)((x-lx)*pps), 
						py = (int)((y-ly)*pps);
					g.drawImage(img, px, py, (int)pps+1, (int)pps+1, null);
				}
			}
		}
		
		//draw the focused player as a yellow dot with an arrow in the direction of movement
		int px = (int)((fx-lx)*pps),
			py = (int)((fy-ly)*pps);
		g.setColor(Color.YELLOW);
		g.fillOval(px, py, (int)pps, (int)pps);
		
		//draw NPCs as blue white dots
		for (Entity e : focus.getRegion().getEntities().get("npc")) {
			px = (int)((e.getX() - lx)*pps);
			py = (int)((e.getY() - ly)*pps);
			g.setColor(new Color(100,100,255));
			g.fillOval(px, py, (int)pps,(int)pps);
		}
		
		//get rid of the edges
		for (int x = 0; x < PIXEL_SIZE; x++) {
			for (int y = 0; y < PIXEL_SIZE; y++) {
				Color bc = new Color(blanked.getRGB(x, y));
				if (bc.getRed() == 0) {
					image.setRGB(x, y, BLANK_RGB);
				}
			}
		}
		
		//draw the border
		int pm = (int)(margin * PIXEL_SIZE);
		g.setStroke(new BasicStroke(pm));
		g.setColor(Color.BLACK);
		g.drawOval(pm/2, pm/2, PIXEL_SIZE-pm, PIXEL_SIZE-pm);
		
		//now gray scale the image
		this.image = ImageTools.grayscale(this.image);
	}
	
	public BufferedImage getImage() {
		return this.image;
	}
}
