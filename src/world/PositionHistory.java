package world;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import misc.Vector2;
import world.event.CollisionEvent;

public class PositionHistory implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private static final int MAX_LENGTH = 10;
	
	private WorldObject object;
	private List<HistoryPoint> history;
	
	public PositionHistory(WorldObject o) {
		object = o;
		history = new ArrayList<HistoryPoint>();
	}
	
	public void update(float dt) {
		HistoryPoint last = null;
		if (history.size() > 0)
			last = history.get(0);
		
		HistoryPoint hp = new HistoryPoint(object.getPosition().copy(),last,object.getAge());
		
		history.add(0,hp);
		
		//this is to avoid using an extreme amount of memory and eventually crashing the program
		if (history.size() > MAX_LENGTH-1)
			history.remove(MAX_LENGTH-1);
	}
	
	/**
	 * Gets the history point given how long ago.. returns the closest one
	 * @param timeAgo
	 * @return The history point if the time is in there, null if it is not.
	 */
	public HistoryPoint getHistory(float timeAgo) {
		for (HistoryPoint p : history) {
			float dt = object.getAge()-p.time;
			if (dt >= timeAgo)
				return p;
		}
		return null;
	}
	
	public HistoryPoint getHistory(int ticksAgo) {
		if (ticksAgo < 0 || ticksAgo > this.history.size()-1)
			return null;
		else
			return history.get(ticksAgo);
	}
	
	public Vector2 getPosition(float timeAgo) {
		HistoryPoint h = getHistory(timeAgo);
		return h == null ? null : h.position;
	}
	
	public Vector2 getPosition(int ticksAgo) {
		HistoryPoint h = getHistory(ticksAgo);
		return h == null ? null : h.position;
	}
	
	private class HistoryPoint implements Serializable {
		private static final long serialVersionUID = 1L;
		
		public float time;
		public Vector2 position, velocity, acceleration;
		
		public HistoryPoint(Vector2 position, HistoryPoint last, float time) {
			this.time = time;
			this.position = position;
			if (last != null) {
				Vector2 lastPos = last.position;
				float dt = time-last.time;
				this.velocity = new Vector2((this.position.x-lastPos.x)/dt,(this.position.y-lastPos.y)/dt,(this.position.r-lastPos.r)/dt);
				Vector2 lastVel = last.velocity;
				if (lastVel != null) {
					this.acceleration = new Vector2((this.velocity.x-lastVel.x)/dt,(this.velocity.y-lastVel.y)/dt,(this.velocity.r-lastVel.r)/dt);
				}
			}
		}
		
		public String toString() {
			String pos = "not found", vel = "not found", acc = "not found";
			if (position != null)
				pos = "("+position.x+", "+position.y+", "+position.r+" rad)";
			if (velocity != null)
				vel = "("+velocity.x+", "+velocity.y+", "+velocity.r+" rad)";
			if (acceleration != null)
				acc = "("+acceleration.x+", "+acceleration.y+", "+acceleration.r+" rad)";
			return "t: "+time+" p: "+pos+" v: "+vel+" a "+acc;
		}
	}
	
	public void print() {
		for (HistoryPoint p : history) {
			System.out.println(p);
		}
	}
}
