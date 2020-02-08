package entities;

import java.util.*;

import entities.player.*;
import world.regions.Region;

public class EntityList {
	//for now lets just hold an array list of entities
	
	private List<Entity> list;
	private List<Entity> toRemove, toAdd;
	private Region region;
	
	public EntityList(Region region) {
		list = new ArrayList<Entity>();
		toRemove = new ArrayList<Entity>();
		toAdd = new ArrayList<Entity>();
		this.region = region;
	}
	
	public void update(float dt) {
		for (Entity e : list) {
			if (e == null || e.isDestroyed()) { //extra check in case something happened
				toRemove.add(e);
				continue;
			}
			e.update(dt);
			e.generalUpdate(dt);
		}
		
		//get rid of entities that we want to get rid of
		for (Entity e : toRemove)
			list.remove(e);
		toRemove.clear();
		
		//add entities that we want to add in order of their vertical height
		for (Entity e : toAdd)  {
			float h = e.getVerticalHeight();
			int index = 0;
			if (list.size() == 0) {
				list.add(e);
				continue;
			} 
		
			while (h > list.get(index).getVerticalHeight() && index < list.size()-1) 
				index++;
			list.add(index,e);
		}
		toAdd.clear();
	}
	
	/**
	 * Adds the entity to the list such that it is sorted
	 * by its vertical height from least to greatest.
	 * @param e
	 */
	public void add(Entity e) {
		toAdd.add(e);
	}
	
	public void remove(Entity e) {
		toRemove.add(e);
	}
	
	public Entity get(int index) {
		return list.get(index);
	}
	
	public int size() {
		return list.size();
	}
	
	public List<Entity> get() {
		return this.list;
	}
	
	public List<Entity> get(String ... tags) {
		List<Entity> getList = new ArrayList<Entity>();
		for (int i = 0; i < this.list.size(); i++) {
			if (i > list.size()-1)
				break; //concurrency occurred
			Entity e = list.get(i);
			if (e == null)
				break; //somehow a null object got stuck in the list, will be removed later
			boolean cont = true;
			for (String tag : e.getTags()) {
				for (String tag2 : tags) {
					if (tag.equals(tag2)) {
						getList.add(e);
						cont = false;
						break;
					}
				}
				if (!cont)
					break;
			}
		}
		return getList;
	}
	
	public Region getRegion() {
		return this.region;
	}
}
