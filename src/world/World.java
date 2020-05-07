package world;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import entities.Entity;
import entities.npcs.NPC;
import entities.player.Player;
import misc.Line;
import misc.MathUtils;
import misc.Vector2;
import world.regions.Region;
import world.regions.Road;

public class World implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private static List<String> worlds; //list of world names held on the client hard drive
	
	private static final String wFP = "assets/saves/worlds.txt";
	
	/**
	 * Initializes the "worlds" list to be have each world from the worlds.txt file
	 */
	public static void loadWorldsList() {
		worlds = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(wFP));
			String line;
			while ((line = br.readLine()) != null)
				worlds.add(line); 
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Saves the current worlds list to the worlds file
	 */
	public static void saveWorldsList() {
		try {
			File file = new File(wFP);
			PrintWriter out = new PrintWriter(file);
			for (String w : worlds)
				out.println(w);
			out.close();
		} catch (Exception e) {
			
		}
	}
	
	/**
	 * Adds a world name to the client side list of worlds
	 * @param worldName
	 * @return
	 */
	public static void addWorld(String worldName) {
		if (!hasWorld(worldName)) {
			worlds.add(worldName);
			saveWorldsList();
		}
	}
	
	/**
	 * Checks if the world name has been used in the client side list
	 * of worlds.
	 * @param worldName
	 * @return
	 */
	public static boolean hasWorld(String worldName) {
		for (String world : worlds) 
			if (world.contentEquals(worldName))
				return true;
		return false;
	}
	
	public static List<String> getWorldsList() {
		return worlds;
	}
	
	private List<Region> regions;
	private int currentRegion;
	
	private float elapsedTime = 0.0f;
	
	private String worldName, saveName;
	
	private float timeOfDay = 12.0f; //up to hour 23, at 24 it resets to 0.
	private int elapsedDays = 0;
	
	public static World loadWorld(String saveName) {
		try {
			FileInputStream fis = new FileInputStream("assets/saves/"+saveName+".world");
			ObjectInputStream in = new ObjectInputStream(fis);
			World world = (World)in.readObject();
			in.close();
			//load assets
			//go through each world object in the world and load their assets
			for (Region r : world.regions) {
				System.out.println(r.getEntities().get().size());
				for (Entity e : r.getEntities().get())
					e.loadAssets();
				for (int x = 0; x < r.getWidth(); x++)
					for (int y = 0; y < r.getHeight(); y++)
						r.getGrid().get(x, y).loadAssets();
			}		
			return world;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Instantiates a world from the specified world
	 * folder name.
	 * @param name The name of the world folder
	 */
	public World(String worldName, String saveName) {
		this.worldName = worldName;
		this.saveName = saveName;
		
		regions = new ArrayList<Region>();
		//add in all the regions
		Region next;
		while ((next = Region.generateWorldRegion(this,worldName,regions.size())) != null)
			regions.add(next);
		
		addPlayer();
		
		Region reg0 = regions.get(0);
		
		for (int i = 0; i < 5; i++)
			reg0.add(new NPC(15+MathUtils.random(-4,4),15+MathUtils.random(-4,4)));
		
		
		//initialize starting region
		getCurrentRegion().update(0);
	}
	
	public Player addPlayer() {
		Region reg0 = this.getRegion(0);
		Player p = new Player(reg0.getWidth()/2,reg0.getHeight()/2);
		reg0.add(p);
		return p;
	}
	
	public List<Region> getRegions() {
		return this.regions;
	}
	
	public Region getCurrentRegion() {
		return regions.get(currentRegion);
	}
	
	public Region getRegion(int number) {
		return regions.get(number);
	}
	
	public void setCurrentRegion(Region region) {
		currentRegion = regions.indexOf(region);
	}
	
	/**
	 * Returns the a value between [0,1] that reflects
	 * the time of day.
	 * Uses a sinusoidal function that mimics the day-night light cycle
	 * Brightest at around 1:30 PM and Darkest at 1:30 AM
	 * @return
	 */
	public float getGlobalLightValue() {
		return (float) (Math.sin(this.timeOfDay * Math.PI / 12 - 2) * 0.4 + 0.6);
	}
	
	private float timeRate = 10f; //how many game minutes per real seconds
	
	public void update(float dt) {
		elapsedTime += dt;
		
		//where 1 second = 1/60 of an hour.
		float elapsedGameTime = timeRate/60 * dt;
		timeOfDay += elapsedGameTime;
		
		if (timeOfDay >= 24.0f) {
			elapsedDays++;
			timeOfDay%=24.0f;
		}
		
		getCurrentRegion().update(dt);
	}
	
	public int getElapsedDays() {
		return this.elapsedDays;
	}
	
	public List<Player> getPlayers() {
		List<Entity> entities = getCurrentRegion().getEntities().get("player");
		List<Player> players = new ArrayList<Player>();
		for (Entity e : entities)
			players.add((Player)e);
		return players;
	}
	
	public float getElapsedTime() {
		return elapsedTime;
	}
	
	public String getStringTime() {
		int hour = (int)this.timeOfDay;
		int minute = (int)(this.timeOfDay%1.0f*60);
		int hour12 = hour%12;
		if (hour12 == 0)
			hour12 = 12;
		String h = hour12+"";
		if (hour12 < 10)
			h = "0"+h;
		String m = minute+"";
		if (minute < 10)
			m = "0"+m;
		String time = h+":"+m;
		if (hour >= 12)
			time+=" PM";
		else
			time+=" AM";
		return time;
	}
	
	public void save() {
		try {
//			FileOutputStream fos = new FileOutputStream("assets/saves/"+saveName+".world");
//			ObjectOutputStream out = new ObjectOutputStream(fos);
//			
//			out.writeObject(this);
//			
//			out.close();
//			
//			World.addWorld(saveName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
