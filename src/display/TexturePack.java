package display;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import json.JSONArray;
import json.JSONFile;
import json.JSONObject;
import misc.ImageTools;
import misc.MathUtils;

public class TexturePack {
	public static final int TILE_PIXEL_SIZE = 16;
	
	private static final String TEXTURE_PACK_PATH = "assets/texture_packs/";
	
	public static final TexturePack DEFAULT = new TexturePack("default");
	private static TexturePack currentPack = DEFAULT;
	
	public static TexturePack current() {
		return currentPack;
	}
	
	private String path;
	private String name;
	
	private BufferedImage tileSheet;
	private JSONFile tileJson;
	
	//lets link an id to a stored animation
	private HashMap<Integer, TileTexture> tilesMap;
	
	public TexturePack(String folderPath) {
		this.path = TEXTURE_PACK_PATH+folderPath+"/";
		this.name = folderPath;
		tileSheet = ImageTools.getImage(path+"tileSheet.png");
		tileJson = new JSONFile(path+"tiles.json");
		tilesMap = new HashMap<Integer,TileTexture>();
		addTexture("grass","Grass");
		addTexture("dirt","Dirt");
		addTexture("sand","Sand");
		addTexture("water","Water");
		addTexture("planks","Wooden Planks 1");
		addTexture("planks1","Wooden Planks 2");
		addTexture("black_and_white_tile", "Black and White Tile");
		addTexture("street","Black Street");
		addTexture("side_of_street","Black Street Edge");
		addTexture("street_same_way_crossing","Black Street White Dashed Lines");
		addTexture("street_same_way_no_crossing","Black Street White Solid Lines");
		addTexture("street_two_way_crossing","Black Street Yellow Dashed Lines");
		addTexture("street_two_way_no_crossing","Black Street Yellow Solid Lines");
		addTexture("street_two_way_double_yellow","Black Street Yellow Double Solid Lines");
		addTexture("street_stop_line","Black Street Stop Line");
		addTexture("rainbow","rainbow_",6,"Rainbow");
		save();
		load();
	}
	
	private int index = 0;
	private void addTexture(String imageName, String stringID) {
		tilesMap.put(index, new TileTexture(ImageTools.getImage("assets/texture_packs/"+this.name+"/"+imageName+".png"),stringID));
		index++;
	}
	
	private void addTexture(String folderName, String prefix, int frameRate, String stringID) {
		tilesMap.put(index, new TileTexture(ImageTools.getImages(folderName, prefix),frameRate,stringID));
		index++;
	}
	
	/**
	 * Uses the tile sprite sheet and the json to develop
	 * a map which links the ID from the JSON to a list
	 * of buffered images
	 */
	private void load() {
		//make the hashmap
		tilesMap = new HashMap<Integer, TileTexture>();
		//First lets get some information from the JSON
		JSONArray tiles = (JSONArray)tileJson.get("tiles");
		for (int i = 0; i < tiles.size(); i++) {
			JSONObject tile = (JSONObject)tiles.get(i);
			JSONArray frames = (JSONArray)tile.get("frames");
			//use class type-casting because the method returns an object
			double id = (Double)tile.get("id");
			double frameRate = (Double)tile.get("frame_rate");
			String stringID = (String) tile.get("string_id");
			List<BufferedImage> images = new ArrayList<BufferedImage>();
			//now lets go through each frame
			for (int j = 0; j < frames.size(); j++) {
				JSONObject fInfo = (JSONObject)frames.get(j);
				double x = (Double)fInfo.get("x");
				double y = (Double)fInfo.get("y");
				double w = (Double)fInfo.get("w");
				double h = (Double)fInfo.get("h");
				BufferedImage frame = tileSheet.getSubimage((int)x, (int)y, (int)w, (int)h); 
				images.add(frame);
			}
			Animation tileAni = new Animation(images,(int)frameRate);
			TileTexture tileText = new TileTexture(tileAni,stringID);
			tilesMap.put((int)id, tileText);
		}
	}
	
	public int getFrameRate(int id) {
		return tilesMap.get(id).getAnimation().getFrameRate();
	}
	
	public List<BufferedImage> getTileImages(int id) {
		return tilesMap.get(id).getAnimation().getFrames();
	}
	
	public int getNumberOfTiles() {
		return tilesMap.size();
	}
	
	public TileTexture getTileTexture(int id) {
		return tilesMap.get(id);
	}
	
	/**
	 * Generates a JSON and Sprite Sheet and saves it off
	 */
	public void save() {
		//lets see how many images we need to store.. 
		int images = 0;
		Set<Integer> keys = tilesMap.keySet();
		for (int k : keys) 
			images+=tilesMap.get(k).getAnimation().getNumberOfFrames();
		//lets make it a square
		int size = (int)Math.sqrt(images)+1;
		this.tileSheet = new BufferedImage(size*TILE_PIXEL_SIZE,size*TILE_PIXEL_SIZE,BufferedImage.TYPE_INT_ARGB);
		Graphics g = tileSheet.getGraphics();
		this.tileJson.clear(); //empty out that JSON for us to make a new one
		//now write each frame to the tile sheet and add it to the JSON
		JSONArray arr = new JSONArray("");
		int x = 0, y = 0;
		for (int k : keys) {
			TileTexture text = tilesMap.get(k);
			Animation ani = tilesMap.get(k).getAnimation();
			JSONObject animation = new JSONObject("");
			animation.set("id", k);
			animation.set("string_id", text.getStringID());
			JSONArray frames = new JSONArray("");
			for (BufferedImage i : ani.getFrames()) {
				JSONObject position = new JSONObject("");
				position.set("x", x);
				position.set("y", y);
				position.set("w", TILE_PIXEL_SIZE);
				position.set("h", TILE_PIXEL_SIZE);
				
				frames.add(position);
				
				g.drawImage(i, x, y, TILE_PIXEL_SIZE, TILE_PIXEL_SIZE, null);
				
				x+=TILE_PIXEL_SIZE;
				if (x > this.tileSheet.getWidth()-1) {
					x = 0;
					y+=TILE_PIXEL_SIZE;
				}
			}
			animation.set("frame_rate", ani.getFrameRate());
			animation.set("frames",frames);
			arr.add(animation);
		}
		tileJson.set("tiles", arr);
		tileJson.set("sprite_path",this.path+"tiles.json");
		
		tileJson.save();
		try {
			File outFile = new File(path+"/tileSheet.png");
			ImageIO.write(tileSheet, "png", outFile);
		} catch (IOException e) { e.printStackTrace(); }
	}
}

