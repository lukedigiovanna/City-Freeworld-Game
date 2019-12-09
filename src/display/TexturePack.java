package display;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import json.JSONArray;
import json.JSONFile;
import json.JSONObject;
import misc.ImageTools;

public class TexturePack {
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
		generateTileMap();
	}
	
	/**
	 * Uses the tile sprite sheet and the json to develop
	 * a map which links the ID from the JSON to a list
	 * of buffered images
	 */
	private void generateTileMap() {
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
	
	public int getFrameRate(int id) {
		return tilesMap.get(id).getFrameRate();
	}
	
	public List<BufferedImage> getTileImages(int id) {
		return tilesMap.get(id).getFrames();
	}
}
