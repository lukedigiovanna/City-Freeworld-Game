package mapping;

import misc.Vector2;

import java.awt.*;

public class Camera {
	private Vector2 position;
	private int cellSize = 48; //pixels... preferably a factor of 8 to maintain aspect ratio
	
	public Camera(float x, float y) {
		this.position = new Vector2(x, y);
	}
	
	public void draw(Graphics2D g, World world, int px, int py, int width, int height){
		Region region = world.getCurrentRegion();
		int indexWidth = width/cellSize+1, indexHeight = height/cellSize+1;
		int startPX = (int)(1-(position.getX()-(int)position.getX()))*cellSize, startPY = (int)(1-(position.getY()-(int)position.getY()))*cellSize;
		CellGrid grid = region.getGrid();
		for (int ix = (int)position.getX(); ix < (int)position.getX()+indexWidth; ix++) {
			for (int iy = (int)position.getY(); iy < (int)position.getY()+indexHeight; iy++) {
				if (ix >= 0 && ix < grid.getWidth() && iy >= 0 && iy < grid.getHeight()) {
					//then draw it
					g.drawImage(grid.get(ix, iy).getImage(), px + (ix-(int)position.getX()) * cellSize + startPX, py + (iy-(int)position.getY()) * cellSize + startPY, cellSize, cellSize, null);
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
