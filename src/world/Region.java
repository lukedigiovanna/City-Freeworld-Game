package world;

import entities.Entity;
import entities.EntityList;

public class Region {
	private World world;
	private CellGrid cellGrid;
	private EntityList entities;
	private int width, height;
	
	private String id = "reg-0.0"; //this should match the folder path
	
	public Region(String folderPath) {
		
	}
	
	public Region(World world, int width, int height) {
		this.width = width;
		this.height = height;
		cellGrid = new CellGrid(this,width,height);
		entities = new EntityList(this);
		this.world = world;
	}
	
	public World getWorld() {
		return world;
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
	
	public void add(Entity e) {
		entities.add(e);
		e.setRegion(this);
	}
	
	public void remove(Entity e) {
		entities.remove(e);
		e.setRegion(null);
	}
	
	/**
	 * Updates region stuff
	 * *Entity movement
	 */
	public void update(float dt) {
		entities.update(dt);
		cellGrid.update(dt);
	}
}
