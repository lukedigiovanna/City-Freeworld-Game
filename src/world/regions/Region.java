package world.regions;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import display.textures.TexturePack;
import entities.Entity;
import entities.EntityList;
import entities.misc.EntityObject;
import entities.misc.Particle;
import entities.misc.Portal;
import entities.misc.Particle.Type;
import misc.Line;
import misc.MathUtils;
import misc.Vector2;
import world.World;

public class Region {
	private World world;
	private CellGrid cellGrid;
	private EntityList entities;
	private Walls walls;
	private RoadMap roadMap;
	private int width, height;
	
	private List<Line> rigidLines;
	
	private String id = "reg-0"; //this should match the folder path
	
	public Region(World world, int width, int height) {
		this.width = width;
		this.height = height;
		cellGrid = new CellGrid(this,width,height);
		entities = new EntityList(this);
		walls = new Walls();
		this.rigidLines = new ArrayList<Line>();
		this.world = world;
		this.addBoundingWalls();
	}
	
	public Region(World world, String worldName, int regionNumber) {
		String path = "assets/worlds/"+worldName+"/regions/reg-"+regionNumber+".DAT";
		this.cellGrid = new CellGrid(this);
		this.entities = new EntityList(this);
		this.walls = new Walls();
		this.rigidLines = new ArrayList<Line>();
		try {
			DataInputStream in = new DataInputStream(new FileInputStream(path));
			this.width = in.read();
			this.height = in.read();
			cellGrid.setDimension(width, height);
			for (int y = 0; y < height; y++) 
				for (int x = 0; x < width; x++) {
					Cell cell = new Cell(in.read(), in.read(), x, y);
					cellGrid.set(x, y, cell);
				}
			int numOfPortals = in.read();
			for (int i = 0; i < numOfPortals; i++) {
				int destRegion = in.read();
				float x = in.read() + in.read()/256.0f;
				float y = in.read() + in.read()/256.0f;
				float width = in.read() + in.read()/256.0f;
				float height = in.read() + in.read()/256.0f;
				float destX = in.read() + in.read()/256.0f;
				float destY = in.read() + in.read()/256.0f;
				System.out.println("d: "+destRegion+" x: "+x+" y: "+y+" w: "+width+" h: "+height+" dx: "+destX+" dy: "+destY);
				
				Portal p = new Portal(new Portal.Destination(destRegion, destX, destY), x, y, width, height);
				this.add(p);
			}
			int numOfWalls = in.read();
			for (int i = 0; i < numOfWalls; i++) {
				float x1 = in.read() + in.read()/256.0f;
				float y1 = in.read() + in.read()/256.0f;
				float x2 = in.read() + in.read()/256.0f;
				float y2 = in.read() + in.read()/256.0f;
				Line w = new Line(new Vector2(x1,y1), new Vector2(x2,y2));
				this.addWall(w);
			}
			int numOfObjects = in.read();
			for (int i = 0; i < numOfObjects; i++) {
				int id = in.read();
				float x = in.read() + in.read()/256.0f;
				float y = in.read() + in.read()/256.0f;
				EntityObject o = new EntityObject(id,x,y);
				this.add(o);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.world = world;
		this.addBoundingWalls();
	}
	
	/**
	 * Adds walls that surround the border of the region so entities cant escape.
	 */
	private void addBoundingWalls() {
		addWall(new Line(new Vector2(0,0),new Vector2(getWidth(),0)));
		addWall(new Line(new Vector2(getWidth(),0), new Vector2(getWidth(),getHeight())));
		addWall(new Line(new Vector2(getWidth(),getHeight()),new Vector2(0,getHeight())));
		addWall(new Line(new Vector2(0,getHeight()),new Vector2(0,0)));
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
		e.updateLightValue();
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
