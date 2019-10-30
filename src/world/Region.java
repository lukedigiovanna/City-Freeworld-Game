package world;

import entities.EntityList;

public class Region {
	private CellGrid cellGrid;
	private EntityList entities;
	private int width, height;
	
	public Region(String folderPath) {
		
	}
	
	public Region(int width, int height) {
		this.width = width;
		this.height = height;
		cellGrid = new CellGrid(width,height);
		entities = new EntityList();
	}
	
	public CellGrid getGrid() {
		return cellGrid;
	}
	
	public EntityList getEntities() {
		return entities;
	}
}
