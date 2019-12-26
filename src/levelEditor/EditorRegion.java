package levelEditor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.util.ArrayList;

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
	 */
	
	private int width, height;
	private ArrayList<ArrayList<EditorCell>> grid; // a value of -1 indicates that no tile has been defined
	
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
		this.grid = new ArrayList<ArrayList<EditorCell>>();
		//initializes the grid with all values at -1
		for (int x = 0; x < width; x++) {
			ArrayList<EditorCell> column = new ArrayList<EditorCell>();
			for (int y = 0; y < height; y++)
				column.add(new EditorCell());
			grid.add(column);
		}
	}
	
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
	
	public void fillGrid(int x, int y, int value) {
		int prev = getGridValue(x,y);
		if (prev < -1)
			return;
		setGridValue(x,y,value);
//		if (getGridValue(x-1,y) == prev)
//			fillGrid(x-1,y,value);
		if (getGridValue(x,y-1) == prev)
			fillGrid(x,y-1,value);
	}
	
	public ArrayList<ArrayList<EditorCell>> getGrid() {
		return grid;
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
			out.write(this.width);
			out.write(this.height);
			for (int y = 0; y < this.height; y++) {
				for (int x = 0; x < this.width; x++) {
					out.write(this.getGridValue(x, y));
				}
			}
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
