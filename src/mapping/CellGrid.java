package mapping;

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
		checkBounds(x, y);
		return grid[x][y];
	}
	
	public void set(int x, int y, Cell cell) {
		checkBounds(x, y);
		grid[x][y] = cell;
	}
	
	public boolean checkBounds(int x, int y) {
		if (x < 0 || y < 0 || x > grid.length-1 || y > grid.length-1) 
			throw new IndexOutOfBoundsException("coordinates out of array bounds");
		return true;
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
