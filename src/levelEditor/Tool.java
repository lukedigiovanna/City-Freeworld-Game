package levelEditor;

import java.awt.image.BufferedImage;

import misc.ImageTools;

public enum Tool {
	DRAW("brush", "Paint Brush"),
	FILL("bucket", "Fill"),
	ERASE("eraser", "Erase"),
	TOGGLE_GRID("grid_toggle","Tog. Grid");
	
	BufferedImage img;
	String name;
	
	Tool(String imgName, String name) {
		img = ImageTools.getImage("assets/images/editor_tools/"+imgName+".png");
		this.name = name;
	}
}
