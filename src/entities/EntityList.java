package entities;

import java.util.*;

public class EntityList {
	//for now lets just hold an array list of entities
	
	private List<Entity> list;
	
	public EntityList() {
		list = new ArrayList<Entity>();
	}
	
	public void update(float dt) {
		for (Entity e : list)
			e.update(dt);
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
