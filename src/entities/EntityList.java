package entities;

import java.util.*;

import entities.player.*;

import world.Region;

public class EntityList {
	//for now lets just hold an array list of entities
	
	private List<Entity> list;
	private List<Entity> toRemove, toAdd;
	private Region region;
	private Player playerReference;
	
	public EntityList(Region region) {
		list = new ArrayList<Entity>();
		toRemove = new ArrayList<Entity>();
		toAdd = new ArrayList<Entity>();
		this.region = region;
	}
	
	public void update(float dt) {
		for (Entity e : list) {
			e.update(dt);
			e.generalUpdate(dt);
		}
		
		//get rid of entities that we want to get rid of
		for (Entity e : toRemove)
			list.remove(e);
		toRemove.clear();
		
		//add entities that we want to add
		for (Entity e : toAdd) 
			list.add(e);
		toAdd.clear();
	}
	
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
		for (Entity e : list) {
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
}
