package entities;

import java.util.ArrayList;
import java.util.List;

import misc.MathUtils;
import misc.Vector2;
import world.Region;

/**
 * Defines a path for an Entity to follow
 */
public class Path {
	private Entity entity;
	
	private List<Vector2> points; //paths are defined by a list of points for the entity to follow.. like bread crumbs
	
	private int position = 0;
	private float speed = 2.0f;
	
	public Path() {
		this.points = new ArrayList<Vector2>();
	}
	
	public Path(Entity entity) {
		this();
		this.entity = entity;
	}
	
	public void setEntity(Entity e) {
		this.entity = e;
	}
	
	public void add(Vector2 point) {
		this.points.add(point);
	}
	
	public void add(float x, float y) {
		this.add(new Vector2(x,y));
	}
	
	public void follow() {
		if (completed())
			return;
		Vector2 goal = this.points.get(position);
		//set the velocity to that point
		double angle = MathUtils.getAngle(entity.getPosition(),goal);
		entity.setVelocity(speed*(float)Math.cos(angle),speed*(float)Math.sin(angle));
		if (MathUtils.distance(entity.getPosition(),goal) < 0.1)
			position++;
	}
	
	public boolean completed() {
		return this.position >= this.points.size()-1;
	}
}