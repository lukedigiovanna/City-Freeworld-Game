package world.regions;

import java.awt.Color;
import java.io.*;

import entities.*;
import entities.misc.*;
import entities.misc.Particle.Type;
import entities.misc.interactables.InteractableShopObject;
import main.Program;
import misc.Line;
import misc.MathUtils;
import misc.Vector2;
import soundEngine.SoundEngine;
import world.Camera;
import world.World;

public class Region implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static Region generateWorldRegion(World world, String worldName, int index) {
		Region region = new Region(world, worldName, index);
		if (region.exists)
			return region;
		else
			return null;
	}
	
	private World world;
	private CellGrid cellGrid;
	private EntityList entities;
	private Walls walls;
	private RoadMap roadMap;
	private int width, height;
	
	private SoundEngine soundEngine;
	
	//0 means its dictated fully by the time, 1 means the time plays no role in light level
	private float localLightValue = 0.0f; 
	
	private String id = "reg-0"; //this should match the folder path
	
	public Region(World world, int width, int height) {
		this.width = width;
		this.height = height;
		cellGrid = new CellGrid(this,width,height);
		entities = new EntityList(this);
		walls = new Walls();
		this.world = world;
		this.soundEngine = new SoundEngine();
		this.addBoundingWalls();
	}
	
	private boolean exists = true;
	
	public Region(World world, String worldName, int regionNumber) {
		String path = "assets/worlds/"+worldName+"/regions/reg-"+regionNumber+".DAT";
		System.out.println("Generating "+worldName+"."+regionNumber);
		this.cellGrid = new CellGrid(this);
		this.entities = new EntityList(this);
		this.walls = new Walls();
		this.roadMap = new RoadMap(this);
		try {
			DataInputStream in = new DataInputStream(new FileInputStream(path));
			this.width = in.read();
			this.height = in.read();
			cellGrid.setDimension(width, height);
			for (int y = 0; y < height; y++) 
				for (int x = 0; x < width; x++) {
					int id = in.read();
					int rotFlip = in.read();
					Cell cell = new Cell(id, rotFlip & 0xF, rotFlip >> 4, x, y);
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
				float r = in.read() + in.read()/256.0f;
				EntityObject o = new EntityObject(id,x,y,r);
				this.add(o);
			}
			int numOfRoads = in.read();
			for (int i = 0; i< numOfRoads; i++) {
				int id = in.read();
				Road road = new Road(this,id);
				int numOfPoints = in.read();
				for (int j = 0; j < numOfPoints; j++) {
					Vector2 point = new Vector2(in.read() + in.read()/256.0f, in.read() + in.read()/256.0f);
					road.addPoint(point);
				}
				int numOfLinkedRoads = in.read();
				for (int j = 0; j < numOfLinkedRoads; j++) {
					road.linkRoad(in.read());
				}
				int numOfIntersectionRoads = in.read();
				for (int j = 0; j < numOfIntersectionRoads; j++) {
					road.intersectRoad(in.read());
				}
				road.setCarRate(in.read() + in.read()/256.0f);
				road.setSpeedLimit(in.read() + in.read()/256.0f);
				this.roadMap.addRoad(road);
			}
			this.roadMap.linkRoads();
			this.roadMap.intersectRoads();
			int numOfTags = in.read();
			for (int i = 0; i < numOfTags; i++) {
				int textLength = in.read();
				String text = "";
				for (int j = 0; j < textLength; j++) {
					text += in.readChar();
				}
				float x = in.read() + in.read()/256.0f;
				float y = in.read() + in.read()/256.0f;
				Tag tag = new Tag(text,Color.WHITE,Color.BLACK,x,y);
				this.add(tag);
			}
			int numOfInteractables = in.read();
			for (int i = 0; i < numOfInteractables; i++) {
				float x = in.read() + in.read()/256.0f-0.5f;
				float y = in.read() + in.read()/256.0f-0.5f;
				int textLength = in.read();
				String text = "";
				for (int j = 0; j < textLength; j++) {
					text += in.readChar();
				}
				InteractableShopObject inte = new InteractableShopObject(text,x,y);
				this.add(inte);
			}
			
			this.localLightValue = in.read()/256.0f;
			
			in.close();
		} catch (Exception e) {
			exists = false; //the file does not exist
		}
		this.world = world;
		this.soundEngine = new SoundEngine();
	}
	
	public SoundEngine getSoundEngine() {
		return this.soundEngine;
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
	
	public RoadMap getRoadMap() {
		return this.roadMap;
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
	
	public void setLocalLightLevel(float level) {
		this.localLightValue = level;
	}
	
	//combines the local and global light values with a maximum value of 1 possible
	public float getLightLevel() {
		return MathUtils.min(this.localLightValue + world.getGlobalLightValue(), 1.0f);
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
	
	private Camera camera;
	public void setCamera(Camera camera) {
		this.camera = camera;
	}
	
	/**
	 * Gets the mouse position relative to where it is in this region
	 * @return
	 */
	public Vector2 getMousePosition() {
		if (camera == null)
			return new Vector2(0,0);
		float mx = Program.mouse.getX(), my = Program.mouse.getY();
		float xPercent = mx/Program.DISPLAY_WIDTH, yPercent = my/Program.DISPLAY_HEIGHT;
		float x = camera.getX() + camera.getWidth() * xPercent,
			  y = camera.getY() + camera.getHeight() * yPercent;
		return new Vector2(x,y);
	}
	
	/**
	 * Updates region stuff
	 * *Entity movement
	 */
	public void update(float dt) {
		entities.update(dt);
		cellGrid.update(dt);
		roadMap.update(dt);
		this.soundEngine.update();
	}
}
