package mapping;

import misc.Vector2;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Camera {
	private Vector2 position;
	private float width, height; //cell grid width and height
	private BufferedImage image;
	private int cellSize = 32;
	
	public Camera(float x, float y, float width, float height) {
		this.position = new Vector2(x, y);
		this.width = width;
		this.height = height;
		image = new BufferedImage((int)(width * cellSize), (int)(height * cellSize), BufferedImage.TYPE_INT_ARGB);
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
		Graphics g = image.getGraphics();
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
					g.drawImage(grid.get(ix, iy).getImage(), px, py, cellSize, cellSize, null);
				}
			}
		}
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
