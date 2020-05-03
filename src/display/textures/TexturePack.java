package display.textures;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import display.Animation;
import json.JSONArray;
import json.JSONFile;
import json.JSONObject;
import misc.CSVFile;
import misc.ImageTools;

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
	
	//link an id to a texture for TILES
	private HashMap<Integer, Texture> tilesMap;
	
	//link an id to a texture for OBJECTS
	private HashMap<Integer, Texture> objectsMap;
	
	public TexturePack(String folderPath) {
		this.path = TEXTURE_PACK_PATH+folderPath+"/";
		this.name = folderPath;
		tileSheet = ImageTools.getImage(path+"tiles/tileSheet.png");
		tileJson = new JSONFile(path+"tiles/tiles.json");
		tilesMap = new HashMap<Integer, Texture>();
		objectsMap = new HashMap<Integer, Texture>();
		CSVFile tiles = new CSVFile(TEXTURE_PACK_PATH+"tiles.csv");
		tiles.removeRows(1); //remove the header
		System.out.println("Creating texture pack: "+this.name);
		for (int row = 0; row < tiles.getNumberOfRows(); row++) {
			String stringID = tiles.getString(row, 0);
			int frameRate=  tiles.getInt(row, 1);
			String displayName = tiles.getString(row, 2);
			float drivability = tiles.getFloat(row, 3);
			addTileTexture(stringID,stringID+"_",frameRate,displayName);
			Texture texture = this.tilesMap.get(tilesMap.size()-1);
			texture.setDrivability(drivability);
		}
		CSVFile objects = new CSVFile(TEXTURE_PACK_PATH+"objects.csv");
		objects.removeRows(1); //remove header
		for (int row = 0; row < objects.getNumberOfRows(); row++) {
			String stringID = objects.getString(row, 0);
			int frameRate = objects.getInt(row, 1);
			float width = objects.getFloat(row, 2), height = objects.getFloat(row, 3);
			float light = objects.getFloat(row, 4), vHeight = objects.getFloat(row, 5);
			String displayName = objects.getString(row, 6);
			if (frameRate == 0)
				addObjectTexture(stringID,width,height,displayName);
			else
				addObjectTexture(stringID,stringID+"_",frameRate,width,height,displayName);
			Texture texture = objectsMap.get(objectsMap.size()-1);
			texture.setLightEmission(light);
			texture.setVerticalHeight(vHeight);
		}
		save();
		load();
	}
	
	private void addTileTexture(String imageName, String stringID) {
		tilesMap.put(tilesMap.size(), new Texture(ImageTools.getImage("assets/texture_packs/"+this.name+"/tiles/"+imageName+".png"),stringID));
	}
	
	private void addTileTexture(String folderName, String prefix, int frameRate, String stringID) {
		if (frameRate == 0)
			addTileTexture(folderName,stringID);
		else
			tilesMap.put(tilesMap.size(), new Texture(ImageTools.getImages("assets/texture_packs/"+this.name+"/tiles/"+folderName, prefix),frameRate,stringID));
	}
	
	private void addObjectTexture(String imageName, float width, float height, String stringID) {
		objectsMap.put(objectsMap.size(), (new Texture(ImageTools.getImage("assets/texture_packs/"+this.name+"/objects/"+imageName+".png"),width,height,stringID)));
	}
	
	private void addObjectTexture(String folderName, String prefix, int frameRate, float width, float height, String stringID) {
		objectsMap.put(objectsMap.size(), (new Texture(ImageTools.getImages("assets/texture_packs/"+this.name+"/objects/"+folderName, prefix), frameRate,width,height,stringID)));
	}
	
	/**
	 * Uses the tile sprite sheet and the json to develop
	 * a map which links the ID from the JSON to a list
	 * of buffered images
	 */
	private void load() {
		//make the hashmap
		tilesMap = new HashMap<Integer, Texture>();
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
			Texture tileText = new Texture(tileAni,stringID);
			tilesMap.put((int)id, tileText);
		}
	}
	
	public int getNumberOfTiles() {
		return tilesMap.size();
	}
	
	public int getNumberOfObjects() {
		return objectsMap.size();
	}
	
	public Texture getTileTexture(int id) {
		return tilesMap.get(id);
	}
	
	public Texture getObjectTexture(int id) {
		return objectsMap.get(id);
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
			Texture text = tilesMap.get(k);
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
		tileJson.set("sprite_path",this.path+"tiles/tiles.json");
		
		tileJson.save();
		try {
			File outFile = new File(this.path+"tiles/tileSheet.png");
			ImageIO.write(tileSheet, "png", outFile);
		} catch (IOException e) { e.printStackTrace(); }
	}
}

