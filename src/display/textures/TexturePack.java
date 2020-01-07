package display.textures;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.imageio.ImageIO;

import display.Animation;
import json.JSONArray;
import json.JSONFile;
import json.JSONObject;
import misc.CSVFile;
import misc.ImageTools;
import misc.MathUtils;

public class TexturePack {
	
	/**
	 * Generates the tile sheet and data from the CSV files
	 * @param args
	 */
	public static void main(String[] args) {
		CSVFile tiles = new CSVFile("assets/texture_packs/tiles.csv");
		tiles.removeRows(1); //remove the header
	}
	
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
		//add textures (these only need to be called once)
		addTileTexture("grass","Grass");
		addTileTexture("dirt","Dirt");
		addTileTexture("sand","Sand");
		addTileTexture("water","Water");
		addTileTexture("planks","Wooden Planks 1");
		addTileTexture("planks1","Wooden Planks 2");
		addTileTexture("black_and_white_tile", "Black and White Tile");
		addTileTexture("mossy_stone","Mossy Stone");
		addTileTexture("stone","Stone");
		addTileTexture("path","Path");
		addTileTexture("bricks","Bricks");
		addTileTexture("sand_sidewalk","Sand Sidewalk");
		addTileTexture("concrete_sidewalk","Concrete Sidewalk");
		addTileTexture("street","Black Street");
		addTileTexture("side_of_street","Black Street Edge");
		addTileTexture("corner_of_street","Black Street Corner");
		addTileTexture("street_same_way_crossing","Black Street White Dashed Lines");
		addTileTexture("street_same_way_no_crossing","Black Street White Solid Lines");
		addTileTexture("street_two_way_crossing","Black Street Yellow Dashed Lines");
		addTileTexture("street_two_way_no_crossing","Black Street Yellow Solid Lines");
		addTileTexture("street_two_way_double_yellow","Black Street Yellow Double Solid Lines");
		addTileTexture("street_stop_line","Black Street Stop Line");
		addTileTexture("rainbow","rainbow_",6,"Rainbow");
		addTileTexture("rainbow2","rainbow_",18,"Rainbow 2");
		addTileTexture("arrow","Arrow");
		//add objects
		addObjectTexture("fire","fire_",8,1.0f,1.0f,0.8f,1.0f,"Fire");
		addObjectTexture("wooden_bench",2.0f,1.0f,0.0f,4.0f,"Wooden Bench");
		addObjectTexture("pine_tree",1.0f,1.0f,0.0f,8.0f,"Pine Tree");
		addObjectTexture("palm_tree",1.0f,1.0f,0.0f,8.0f,"Palm Tree");
		addObjectTexture("maple_tree",1.0f,1.0f,0.0f,8.0f,"Maple Tree");
		addObjectTexture("apple_tree",1.0f,1.0f,0.0f,8.0f,"Apple Tree");
		addObjectTexture("soda_can",0.25f,0.5f,0.0f,2.0f,"Soda Can");
		addObjectTexture("red_flower",5f/16f,10f/16f,0.0f,1.0f,"Red Flower");
		addObjectTexture("yellow_flower",5f/16f,10f/16f,0.0f,1.0f,"Yellow Flower");
		addObjectTexture("blue_flower",5f/16f,10f/16f,0.0f,1.0f,"Blue Flower");
		addObjectTexture("street_lamp_0",11f/16f,11f/16f,0.8f,8.0f,"Street Lamp 0");
		save();
		load();
	}
	
	private void addTileTexture(String imageName, String stringID) {
		tilesMap.put(tilesMap.size(), new Texture(ImageTools.getImage("assets/texture_packs/"+this.name+"/tiles/"+imageName+".png"),stringID));
	}
	
	private void addTileTexture(String folderName, String prefix, int frameRate, String stringID) {
		tilesMap.put(tilesMap.size(), new Texture(ImageTools.getImages("assets/texture_packs/"+this.name+"/tiles/"+folderName, prefix),frameRate,stringID));
	}
	
	private void addObjectTexture(String imageName, float width, float height, String stringID) {
		objectsMap.put(objectsMap.size(), new Texture(ImageTools.getImage("assets/texture_packs/"+this.name+"/objects/"+imageName+".png"),width,height,stringID));
	}
	
	private void addObjectTexture(String folderName, String prefix, int frameRate, float width, float height, String stringID) {
		objectsMap.put(objectsMap.size(), new Texture(ImageTools.getImages("assets/texture_packs/"+this.name+"/objects/"+folderName, prefix), frameRate,width,height,stringID));
	}
	
	private void addObjectTexture(String imageName, float width, float height, float light, float vHeight, String stringID) {
		objectsMap.put(objectsMap.size(), (new Texture(ImageTools.getImage("assets/texture_packs/"+this.name+"/objects/"+imageName+".png"),width,height,stringID)).setLightEmission(light).setVerticalHeight(vHeight));
	}
	
	private void addObjectTexture(String folderName, String prefix, int frameRate, float width, float height, float light, float vHeight, String stringID) {
		objectsMap.put(objectsMap.size(), (new Texture(ImageTools.getImages("assets/texture_packs/"+this.name+"/objects/"+folderName, prefix), frameRate,width,height,stringID)).setLightEmission(light).setVerticalHeight(vHeight));
	}
	
	private void addObjectTexture(Texture texture) {
		objectsMap.put(objectsMap.size(), texture);
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

