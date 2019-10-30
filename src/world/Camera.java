package world;

import misc.MathUtils;
import misc.Vector2;

import java.awt.*;
import java.awt.image.BufferedImage;

import entities.Entity;

public class Camera {
	private Vector2 position;
	private float width, height; //cell grid width and height
	private BufferedImage image;
	private int cellSize = 16;
	
	public Camera(float x, float y, float width, float height) {
		this.position = new Vector2(x, y);
		this.width = width;
		this.height = height;
		image = new BufferedImage((int)(width * cellSize), (int)(height * cellSize), BufferedImage.TYPE_INT_ARGB);
	}
	
	/**
	 * The value for 1 world unit converted to pixels.
	 * @return
	 */
	public int getUnitSize() {
		return cellSize;
	}
	
	public float getX() {
		return position.x;
	}
	
	public float getY() {
		return position.y;
	}
	
	/**
	 * Getter for camera view
	 * @return the camera view
	 */
	public BufferedImage getView() {
		return image;
	}
	
	/**
	 * Redraws the camera view to a BufferedImage
	 * Only draws what is in view of the camera. Ignores other cells.
	 * @param world the world to draw. includes the grid and entities
	 */
	public void draw(World world){
		refreshGraphics();
		//first wipe the old view
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, image.getWidth(), image.getHeight());
		
		Region region = world.getCurrentRegion();
		CellGrid grid = region.getGrid();
		//find the index on the grid where we need to start
		int startIndexX = (int)this.position.getX(), startIndexY = (int)this.position.getY();
		//find where we need to start on the tile drawing
		int startPX = (int)((1-(position.getX()%1.0f)) * cellSize)-cellSize,
			startPY = (int)((1-(position.getY()%1.0f)) * cellSize)-cellSize;
		//how many cells we need to get in the x, y directions
		int gridWidth = (int)width+1, gridHeight = (int)height+1;
		for (int ix = startIndexX; ix < startIndexX+gridWidth; ix++) {
			for (int iy = startIndexY; iy < startIndexY+gridHeight; iy++) {
				if (ix >= 0 && ix < grid.getWidth() && iy >= 0 && iy < grid.getHeight()) {
					int px = startPX + (ix-startIndexX) * cellSize;
					int py = startPY + (iy-startIndexY) * cellSize;
					//px = (int)((ix - this.getX())*cellSize);
					//py = (int)((iy - this.getY())*cellSize);
					g.drawImage(grid.get(ix, iy).getImage(), px, py, cellSize, cellSize, null);
					//drawImage(grid.get(ix, iy).getImage(), (float)ix, (float)iy, 1.0f, 1.0f);
				}
			}
		}
		
		for (Entity e : world.getCurrentRegion().getEntities().get()) {
			e.drawHitbox(this);
		}
	}
	
	/*
	 * Graphics functions so we can call position/dimension in world coordinates
	 * rather than in pixel coordinates
	 */
	
	private Graphics g;
	
	public void refreshGraphics() {
		g = image.getGraphics();
	}
	
	public void setColor(Color color) {
		g.setColor(color);
	}
	
	public void fillRect(float x, float y, float width, float height) {
		g.fillRect(toPX(x), toPY(y), toPW(width), toPH(height));
	}
	
	public void drawLine(float x1, float y1, float x2, float y2) {
		g.drawLine(toPX(x1), toPY(y1), toPX(x2), toPY(y2));
	}
	
	public void drawImage(Image image, float x, float y, float width, float height) {
		g.drawImage(image, toPX(x), toPY(y), toPW(width), toPH(height), null);
	}
	
	private int toPX(float x) {
		float rel = MathUtils.round(x-this.position.x,1.0f/cellSize);
		int sx = Math.round(rel * cellSize);
		return sx;
	}
	
	private int toPW(float width) {
		return Math.round(width * cellSize);
	}
	
	private int toPY(float y) {
		float rel = MathUtils.round(y-this.position.y,1.0f/cellSize);
		int sy = Math.round(rel * cellSize);
		return sy;
	}
	
	private int toPH(float height) {
		return Math.round(height * cellSize);
	}
	
	public void move(float dx, float dy) {
		position.add(new Vector2(dx,dy));
	}
	
	public void moveX(float dx) {
		move(dx,0);
	}
	
	public void moveY(float dy) {
		move(0,dy);
	}
}
