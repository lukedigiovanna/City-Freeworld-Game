package regionEditor;

import java.awt.*;
import java.io.PrintWriter;

import misc.Vector2;

public class Editor {
	private static String world = "world1"; //file name of the working world
	private static int region = 0; //references "world/reg_<region>/"
	private static String filePath = "assets/level-editor/worlds/"+world+"/reg_"+region+"/";
	
	private static int gridWidth = 30, gridHeight = 20;
	private static float regDisplaySize = 8.0f;
	private static Vector2 displayPosition = new Vector2(0.0f,0.0f);
	private static Color[][] grid;
	
	public static void draw(Graphics2D g, int width, int height) {
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, width, height);
		grid = new Color[gridWidth*8][gridHeight*8];
		for (int x = 0; x < grid.length; x++)
			for (int y = 0; y < grid[x].length; y++)
				grid[x][y] = Color.WHITE;
		writeToGridFile();
	}
	
	/**
	 * Takes the current color grid and writes it to the grid file
	 */
	public static void writeToGridFile() {
		//open up a writer to the grid file
		try {
			PrintWriter pw = new PrintWriter(filePath+"grid.txt","UTF-8");
			for (int y = 0; y < grid[0].length; y++) {
				for (int x = 0; x < grid.length; x++) {
					Color c = grid[x][y];
					pw.print(c.getRed()+" "+c.getGreen()+" "+c.getBlue()+" "+c.getAlpha()+", ");
				}
				pw.println();
			}
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void readFromGridFile() {
		
	}
}
