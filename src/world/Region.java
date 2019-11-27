package world;

import java.awt.Color;

import entities.Entity;
import entities.EntityList;
import entities.Particle;
import entities.Particle.Type;
import misc.Line;
import misc.MathUtils;

public class Region {
	private World world;
	private CellGrid cellGrid;
	private EntityList entities;
	private Walls walls;
	private int width, height;
	
	private String id = "reg-0.0"; //this should match the folder path
	
	public Region(World world, int width, int height) {
		this.width = width;
		this.height = height;
		cellGrid = new CellGrid(this,width,height);
		entities = new EntityList(this);
		walls = new Walls();
		this.world = world;
	}
	
	public void addParticles(Type type, Color color, int count, float heat, float x, float y, float width, float height) {
		Particle.add(this,type,color,count,heat,x,y,width,height);
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
	
	public void addWall(Line l) {
		this.walls.add(l);
	}
	
	public Walls getWalls() {
		return walls;
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
