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
	 * DAT FILE KEY:
	 * byte 0: width
	 * byte 1: height
	 * bytes 2 to (2 + width * height): grid data
	 * bytes (2 + width * height) to (2 + width * height + 1 + portalNum * 9): portal info
	 */
	
	private int width, height;
	private ArrayList<ArrayList<EditorCell>> grid; // a value of -1 indicates that no tile has been defined
	
	private List<EditorPortal> portals;
	
	private List<EditorWall> walls;
	
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
		
		//initialize the portal list
		portals = new ArrayList<EditorPortal>();
		
		//initialize the wall list
		walls = new ArrayList<EditorWall>();
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
					this.setGridValue(x, y, in.read());
				}
			}
			
			//add in the portals next
			this.portals = new ArrayList<EditorPortal>();
			int numPortals = in.read();
			for (int i = 0; i < numPortals; i++) {
				EditorPortal p = new EditorPortal();
				p.destinationNumber = in.read();
				p.x = in.read() + in.read()/256.0f;
				p.y = in.read() + in.read()/256.0f;
				p.width = in.read() + in.read()/256.0f;
				p.height = in.read() + in.read()/256.0f;
				p.destX = in.read() + in.read()/256.0f;
				p.destY = in.read() + in.read()/256.0f;
				portals.add(p);
			}
			
			//add in the walls next
			this.walls = new ArrayList<EditorWall>();
			int numWalls = in.read();
			for (int i = 0; i < numWalls; i++) {
				EditorWall w = new EditorWall();
				w.x1 = in.read() + in.read()/256.0f;
				w.y1 = in.read() + in.read()/256.0f;
				w.x2 = in.read() + in.read()/256.0f;
				w.y2 = in.read() + in.read()/256.0f;
				walls.add(w);
			}
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
		if (x < 0 || x >= this.getWidth() || y < 0 || y >= this.getHeight())
			return -100; //way out idk.
		else 
			return grid.get(x).get(y).value;
	}
	
	public void setGridValue(int x, int y, int value) {
		this.grid.get(x).get(y).value = value;
	}
	
	public void fillGrid(int x, int y, int fillValue) {
		fillGrid(x,y,fillValue,getGridValue(x,y));
	}
	
	public void fillGrid(int x, int y, int fillValue, int previousValue) {
		if (getGridValue(x,y) != previousValue) {
			return; //end condition
		}
		else {
			setGridValue(x,y,fillValue);
			fillGrid(x-1,y,fillValue,previousValue);
			fillGrid(x+1,y,fillValue,previousValue);
			fillGrid(x,y-1,fillValue,previousValue);
			fillGrid(x,y+1,fillValue,previousValue);
		}
	}
	
	public ArrayList<ArrayList<EditorCell>> getGrid() {
		return grid;
	}
	
	public List<EditorPortal> getPortals() {
		return portals;
	}
	
	public List<EditorWall> getWalls() {
		return walls;
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
				for (int x = 0; x < this.width; x++) 
					out.write(this.getGridValue(x, y));
			
			//write the portals
			out.write(this.portals.size());
			for (EditorPortal p : portals)
				p.write(out);
			out.write(this.walls.size());
			for (EditorWall w : walls) 
				w.write(out);
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
		this.height++;
	}
	
	public void addColumnLeft() {
		ArrayList<EditorCell> newCol = new ArrayList<EditorCell>();
		for (int i = 0; i < height; i++)
			newCol.add(new EditorCell());
		grid.add(0, newCol);
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
		this.height--;
	}
	
	public void removeColumnLeft() {
		grid.remove(0);
		this.width--;
	}
	
	public void removeColumnRight() {
		grid.remove(grid.size()-1);
		this.width--;
	}
}
