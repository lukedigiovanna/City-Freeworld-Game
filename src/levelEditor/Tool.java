package levelEditor;

import java.awt.image.BufferedImage;

import misc.ImageTools;

public enum Tool {
	DRAW("brush", "Paint Brush","Modify specific tiles"),
	FILL("bucket", "Fill","Fill area of same tiles"),
	ERASE("eraser", "Erase","Erase a specific tile"),
	TOGGLE_GRID("grid_toggle","Tog. Grid","Toggle shown grid"),
	WALL("wall","Lay Wall","Draw wall lines"),
	PORTAL("portal","Portal","Add portals that link to regions"),
	OBJECT("object","Object","Add objects to the world such as trees/benches"),
	EXPAND("expand","Expand","Use arrows to adjust the size"),
	SHRINK("shrink","Shrink","Use arrows to adjust the size"),
	DELETE("delete","Delete","Delete objects/walls/portals"),
	SET_LIGHT("light","Local Light","Set the regional light value"),
	ROAD("road","Road","Add in roads"),
	ROAD_LINKER("road_linker","Road Link.","Link roads together"),
	ROAD_ATTRIB("road_attrib","Road Att.","Set the attributes of a road"),
	ROAD_STOP("road_stop","Road Stop","Set road intersections");
	
	BufferedImage img;
	String name;
	String tip;
	
	Tool(String imgName, String name, String tip) {
		img = ImageTools.getImage("assets/images/editor_tools/"+imgName+".png");
		this.name = name;
		this.tip = tip;
	}
}
