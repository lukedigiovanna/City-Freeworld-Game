package world;

public class Region {
	private CellGrid cellGrid;
	private int width, height;
	
	public Region(String folderPath) {
		
	}
	
	public Region(int width, int height) {
		this.width = width;
		this.height = height;
		cellGrid = new CellGrid(width,height);
	}
	
	public CellGrid getGrid() {
		return cellGrid;
	}
}
