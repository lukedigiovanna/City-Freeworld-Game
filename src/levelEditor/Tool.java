package levelEditor;

import java.awt.image.BufferedImage;

import misc.ImageTools;

public enum Tool {
	DRAW("brush", "Paint Brush","Modify specific tiles"),
	FILL("bucket", "Fill","Fill area of same tiles"),
	ERASE("eraser", "Erase","Erase a specific tile"),
	TOGGLE_GRID("grid_toggle","Tog. Grid","Toggle shown grid"),
	WALL("wall","Lay Wall","Draw wall lines"),
	EXPAND("expand","Expand","Use arrows to adjust the size"),
	SHRINK("shrink","Shrink","Use arrows to adjust the size");
	
	BufferedImage img;
	String name;
	String tip;
	
	Tool(String imgName, String name, String tip) {
		img = ImageTools.getImage("assets/images/editor_tools/"+imgName+".png");
		this.name = name;
		this.tip = tip;
	}
}
