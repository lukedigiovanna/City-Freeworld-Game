package world;

import entities.EntityList;

public class Region {
	private CellGrid cellGrid;
	private EntityList entities;
	private int width, height;
	
	private String id = "reg-0.0"; //this should match the folder path
	
	public Region(String folderPath) {
		
	}
	
	public Region(int width, int height) {
		this.width = width;
		this.height = height;
		cellGrid = new CellGrid(width,height);
		entities = new EntityList();
	}
	
	public float getWidth() {
		return (float)width;
	}
	
	public float getHeight() {
		return (float)height;
	}
	
	public CellGrid getGrid() {
		return cellGrid;
	}
	
	public EntityList getEntities() {
		return entities;
	}
}
