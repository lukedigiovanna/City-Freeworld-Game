package world;

public class CellGrid {
	//stored such that the x value is the first element and the y value is the second element
	private Cell[][] grid;
	private int width, height;
	
	public CellGrid(int width, int height) {
		grid = new Cell[width][height];
		this.width = width;
		this.height = height;
	}
	
	public Cell get(int x, int y) {
		if (checkBounds(x, y))
			return grid[x][y];
		else
			return null;
	}
	
	public void set(int x, int y, Cell cell) {
		if (checkBounds(x,y))
			grid[x][y] = cell;
	}
	
	public boolean checkBounds(int x, int y) {
		if (x < 0 || y < 0 || x > grid.length-1 || y > grid[0].length-1) 
			return false;
		return true;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public void addColumnRight() {
		
	}
	
	public void addColumnLeft() {
		
	}
	
	public void addRowTop() {
		
	}
	
	public void addRowBottom() {
		
	}
}
