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
	
	private BufferedImage tileSheet;
	private JSONFile tileJson;
	
	//lets link an id to a stored animation
	private HashMap<Integer, Animation> tilesMap;
	
	public TexturePack(String folderPath) {
		this.path = TEXTURE_PACK_PATH+folderPath+"/";
		tileSheet = ImageTools.getImage(path+"tileSheet.png");
		tileJson = new JSONFile(path+"tiles.json");
		//load();
		tilesMap = new HashMap<Integer,Animation>();
		tilesMap.put(0, new Animation(ImageTools.getImage("grass.png")));
		tilesMap.put(1, new Animation(ImageTools.getImage("water.png")));
		tilesMap.put(2, new Animation(ImageTools.getImages("rainbow", "rainbow_"),5));
		tilesMap.put(3, new Animation(ImageTools.getImage("planks.png")));
		tilesMap.put(4, new Animation(ImageTools.getImage("sand.png")));
		tilesMap.put(5, new Animation(ImageTools.getImage("dirt.png")));
		tilesMap.put(6, new Animation(ImageTools.getImage("bricks.png")));
		tilesMap.put(7, new Animation(ImageTools.getImage("cobblestone.png")));
		save();
		load();
	}
	
	/**
	 * Uses the tile sprite sheet and the json to develop
	 * a map which links the ID from the JSON to a list
	 * of buffered images
	 */
	private void load() {
		//make the hashmap
		tilesMap = new HashMap<Integer, Animation>();
		//First lets get some information from the JSON
		JSONArray tiles = (JSONArray)tileJson.get("tiles");
		for (int i = 0; i < tiles.size(); i++) {
			JSONObject tile = (JSONObject)tiles.get(i);
			JSONArray frames = (JSONArray)tile.get("frames");
			//use class type-casting because the method returns an object
			double id = (Double)tile.get("id");
			double frameRate = (Double)tile.get("frame_rate");
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
			tilesMap.put((int)id, tileAni);
		}
	}
	
	public void addTile(Animation ani) {
		tilesMap.put(this.getNumberOfTiles(), ani);
	}
	
	public int getFrameRate(int id) {
		return tilesMap.get(id).getFrameRate();
	}
	
	public List<BufferedImage> getTileImages(int id) {
		return tilesMap.get(id).getFrames();
	}
	
	public int getNumberOfTiles() {
		return tilesMap.size();
	}
	
	/**
	 * Generates a JSON and Sprite Sheet and saves it off
	 */
	public void save() {
		//lets see how many images we need to store.. 
		int images = 0;
		Set<Integer> keys = tilesMap.keySet();
		for (int k : keys) 
			images+=tilesMap.get(k).getNumberOfFrames();
		//lets make it a square
		int size = (int)Math.sqrt(images)+1;
		this.tileSheet = new BufferedImage(size*TILE_PIXEL_SIZE,size*TILE_PIXEL_SIZE,BufferedImage.TYPE_INT_ARGB);
		Graphics g = tileSheet.getGraphics();
		this.tileJson.clear(); //empty out that JSON for us to make a new one
		//now write each frame to the tile sheet and add it to the JSON
		JSONArray arr = new JSONArray("");
		int x = 0, y = 0;
		for (int k : keys) {
			Animation ani = tilesMap.get(k);
			JSONObject animation = new JSONObject("");
			animation.set("id", k);
			animation.set("string_id", "null");
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

