package entities;

import java.util.*;

public class EntityList {
	//for now lets just hold an array list of entities
	
	private List<Entity> list;
	
	public EntityList() {
		list = new ArrayList<Entity>();
	}
	
	public void add(Entity e) {
		list.add(e);
	}
	
	public void remove(Entity e) {
		list.remove(e);
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
}
