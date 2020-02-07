package world.regions;

import java.io.*;

import display.textures.TexturePack;

public class CellGrid {
	//stored such that the x value is the first element and the y value is the second element
	private Cell[][] grid;
	private int width, height;
	private Region region;
	
	public CellGrid(Region region, int width, int height) {
		grid = new Cell[width][height];
		this.width = width;
		this.height = height;
		this.region = region;
	}
	
	/**
	 * Constructs a cellgrid by reading from a grid DAT file.
	 * @param region
	 * @param dataPath
	 */
	public CellGrid(Region region) {
		this.region = region;
	}
	
	public Cell get(int x, int y) {
		if (checkBounds(x, y))
			return grid[x][y];
		else
			return null;
	}
	
	public void setDimension(int width, int height) {
		this.width = width;
		this.height = height;
		this.grid = new Cell[width][height];
	}
	
	public void set(int x, int y, Cell cell) {
		if (checkBounds(x,y)) {
			grid[x][y] = cell;
			cell.setRegion(region);
		}
	}
	
	public boolean checkBounds(int x, int y) {
		if (x < 0 || y < 0 || x > grid.length-1 || y > grid[0].length-1) 
			return false;
		return true;
	}
	
	public void update(float dt) {
		for (int x = 0; x < getWidth(); x++)
			for (int y = 0; y < getHeight(); y++) {
				Cell cell = get(x,y);
				if (cell != null) {
					cell.update(dt);
					cell.generalUpdate(dt);
				}
			}
				
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
}
