package entities;

import java.util.ArrayList;
import java.util.List;

import misc.MathUtils;
import misc.Vector2;
import world.regions.Region;

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
	
	public void follow(float dt) {
		if (completed()) 
			return;
		Vector2 goal = this.points.get(position);
		//set the velocity to that point
		double angle = MathUtils.getAngle(entity.getPosition(),goal);
		entity.setRotation((float)angle);
		entity.setVelocity(speed*(float)Math.cos(angle),speed*(float)Math.sin(angle));
		if (MathUtils.distance(entity.getPosition(),goal) < 0.1)
			position++;
		
		Vector2 pastPos = entity.getPositionHistory().getPosition(1);
		if (pastPos != null) { 
			stalled = pastPos.equals(entity.getPosition());
		}
		if (stalled)
			stallTime+=dt;
		else
			stallTime = 0;
	}
	
	private boolean stalled = false;
	private float stallTime = 0;
	public boolean stalled() {
		return stallTime >= 2.0f ;
	}
	
	public boolean completed() {
		return this.position > this.points.size()-1;
	}
}
