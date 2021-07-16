package levelEditor.editorComponents;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores the data for a region for creating one
 */
public class EditorRegion {
	private String filePath; //path to the .DAT file that stores information on the region
	private File file; //the actual .DAT file
	private String worldName;
	private int regNum;
	
	/*
	 * DAT FILE DESCRIPTION:
	 * bytes 0 + 1: the width and height of the region
	 * the next bytes are the grid info:
	 *   the cell ID and then rotation going row by row
	 *  then the portals, walls, and objects
	 */
	
	private int width, height;
	private ArrayList<ArrayList<EditorCell>> grid; // a value of -1 indicates that no tile has been defined
	
	private static final String[] TYPES = {"portal","wall","object","road","tag","interactable"};
	private List<EditorComponent> components; //all other float position components of the game board
	
	private float localLightValue = 0.0f;
	
	/**
	 * For intializing a new region
	 * @param worldName
	 * @param number
	 * @param width
	 * @param height
	 */
	public EditorRegion(String worldName, int number, int width, int height) {
		filePath = "assets/worlds/"+worldName+"/regions/reg-"+number+".DAT";
		this.worldName = worldName;
		this.regNum = number;
		try {
			file = new File(filePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.width = width;
		this.height = height;
		
		//initializes the grid with all values at -1
		this.grid = new ArrayList<ArrayList<EditorCell>>();
		for (int x = 0; x < width; x++) {
			ArrayList<EditorCell> column = new ArrayList<EditorCell>();
			for (int y = 0; y < height; y++)
				column.add(new EditorCell());
			grid.add(column);
		}
		
		//initialize the component list
		this.components = new ArrayList<EditorComponent>();
	}
	
	/**
	 * For initializing from an existing file
	 * @param worldName
	 * @param regNum
	 */
	public EditorRegion(String worldName, int regNum) {
		filePath = "assets/worlds/"+worldName+"/regions/reg-"+regNum+".DAT";
		this.worldName = worldName;
		this.regNum = regNum;
		try {
			file = new File(filePath);
			DataInputStream in = new DataInputStream(new FileInputStream(file));
			this.width = in.read();
			this.height = in.read();
			this.grid = new ArrayList<ArrayList<EditorCell>>();
			//initializes the grid with all values at -1
			for (int x = 0; x < width; x++) {
				ArrayList<EditorCell> column = new ArrayList<EditorCell>();
				for (int y = 0; y < height; y++)
					column.add(new EditorCell());
				grid.add(column);
			}
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int value = in.read();
					int rotFlip = in.read();
					int rotation = rotFlip & 0xF;
					int flip = rotFlip >> 4;
					this.setGridValue(x, y, rotation, flip, value);
				}
			}
			
			this.components = new ArrayList<EditorComponent>();
			
			int numOfPortals = in.read();
			for (int i = 0; i < numOfPortals; i++) {
				EditorPortal p = new EditorPortal();
				p.read(in);
				this.components.add(p);
			}
			
			int numOfWalls = in.read();
			for (int i = 0; i < numOfWalls; i++) {
				EditorWall p = new EditorWall();
				p.read(in);
				this.components.add(p);
			}
			
			int numOfObjects = in.read();
			for (int i = 0; i < numOfObjects; i++) {
				EditorObject p = new EditorObject();
				p.read(in);
				this.components.add(p);
			}
			
			int numOfRoads = in.read();
			for (int i = 0; i < numOfRoads; i++) {
				EditorRoad r = new EditorRoad();
				r.read(in);
				this.components.add(r);
			}
			
			int numOfTags = in.read();
			for (int i = 0; i < numOfTags; i++) {
				EditorTag t = new EditorTag();
				t.read(in);
				this.components.add(t);
			}
			
			int numOfInteractables = in.read();
			for (int i = 0; i < numOfInteractables; i++) {
				EditorInteractable inte = new EditorInteractable();
				inte.read(in);
				this.components.add(inte);
			}
			
			//set the local light value
			int localLight = in.read();
			this.localLightValue = (float)localLight/256.0f;
			
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void printGrid() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++)
				System.out.print(this.getGridValue(x, y)+" ");
			System.out.println();
		}
	}
	
	public int getGridValue(int x, int y) {
		EditorCell cell = getGridCell(x,y);
		return cell != null ? cell.value : -100;
	}
	
	public int getRotationValue(int x, int y) {
		EditorCell cell = getGridCell(x,y);
		return cell != null ? cell.rotation : -1;
	}
	
	public EditorCell getGridCell(int x, int y) {
		if (x < 0 || x >= this.getWidth() || y < 0 || y >= this.getHeight())
			return null; //way out idk.
		else 
			return grid.get(x).get(y);
	}
	
	public void setGridValue(int x, int y, int rotation, int flip, int value) {
		if (getGridValue(x,y) >= -1) {
			EditorCell cell = this.grid.get(x).get(y);
			cell.value = value;
			cell.rotation = rotation;
			cell.flip = flip;
		}
	}
	
	public void fillRect(int x, int y, int rotation, int width, int height, int fillValue, int flip) {
		for (int gx = x; gx < x + width; gx++)
			for (int gy = y; gy < y + height; gy++)
				setGridValue(gx,gy,rotation,fillValue, flip);
	}
	
	public void fillGrid(int x, int y, int rotation, int flip, int fillValue) {
		fillGrid(x,y,rotation,flip, fillValue,getGridValue(x,y));		
	}
	
	public void fillGrid(int x, int y, int rotation, int flip, int fillValue, int previousValue) {
		if (getGridValue(x,y) != previousValue || fillValue == previousValue) {
			return; //end condition
		}
		else {
			setGridValue(x,y,rotation,flip,fillValue);
			fillGrid(x-1,y,rotation,flip,fillValue,previousValue);
			fillGrid(x+1,y,rotation,flip,fillValue,previousValue);
			fillGrid(x,y-1,rotation,flip,fillValue,previousValue);
			fillGrid(x,y+1,rotation,flip,fillValue,previousValue);
		}
	}
	
	public ArrayList<ArrayList<EditorCell>> getGrid() {
		return grid;
	}
	
	public void addComponent(EditorComponent c) {
		this.components.add(c);
	}
	
	public void removeComponent(EditorComponent c) {
		this.components.remove(c);
	}
	
	public int getNumber(String type) {
		int count = 0;
		for (EditorComponent c : this.components) 
			if (c.getString().contentEquals(type))
				count++;
		return count;
	}
	
	public List<EditorComponent> getType(String type) {
		List<EditorComponent> list = new ArrayList<EditorComponent>();
		for (EditorComponent c : this.components) {
			if (c.getString().contentEquals(type)) {
				list.add(c);
			}
		}
		return list;
	}
	
	public float getLocalLightValue() {
		return localLightValue;
	}
	
	public void setLocalLightValue(float val) {
		this.localLightValue = val;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	/**
	 * Saves the region to the file
	 */
	public void save() {
		DataOutputStream out;
		try {
			out = new DataOutputStream(new FileOutputStream(file));
			//write the region dimensions
			out.write(this.width);
			out.write(this.height);
			
			//write the grid
			for (int y = 0; y < this.height; y++) 
				for (int x = 0; x < this.width; x++) {
					EditorCell cell = this.getGridCell(x, y);
					out.write(cell.value);
					out.write(cell.rotation | cell.flip << 4);
				}
			
			
			for (String type : TYPES) {
				out.write(this.getNumber(type));
				for (EditorComponent c : this.getType(type)) {
					c.write(out);
				}
			}
			
			//write the local light
			out.write((int)(this.localLightValue * 255));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getFilePath() {
		return this.filePath;
	}
	
	public String getWorldName() {
		return this.worldName;
	}
	
	public int getRegionNumber() {
		return this.regNum;
	}
	
	public void addRowBottom() {
		for (ArrayList<EditorCell> list : grid)
			list.add(new EditorCell());
		this.height++;
	}
	
	public void addRowTop() {
		for (ArrayList<EditorCell> list : grid)
			list.add(0, new EditorCell());
		for (EditorComponent c : this.components)
			c.translate(0, 1);
		this.height++;
	}
	
	public void addColumnLeft() {
		ArrayList<EditorCell> newCol = new ArrayList<EditorCell>();
		for (int i = 0; i < height; i++)
			newCol.add(new EditorCell());
		grid.add(0, newCol);
		for (EditorComponent c : this.components)
			c.translate(1, 0);
		this.width++;
	}
	
	public void addColumnRight() {
		ArrayList<EditorCell> newCol = new ArrayList<EditorCell>();
		for (int i = 0; i < height; i++)
			newCol.add(new EditorCell());
		grid.add(newCol);
		this.width++;
	}
	
	public void removeRowBottom() {
		for (ArrayList<EditorCell> col : grid)
			col.remove(col.size()-1);
		this.height--;
	}
	
	public void removeRowTop() {
		for (ArrayList<EditorCell> col : grid)
			col.remove(0);
		for (EditorComponent c : this.components) 
			c.translate(0, -1);
		this.height--;
	}
	
	public void removeColumnLeft() {
		grid.remove(0);
		for (EditorComponent c : this.components) 
			c.translate(-1, 0);
		this.width--;
	}
	
	public void removeColumnRight() {
		grid.remove(grid.size()-1);
		this.width--;
	}
}
