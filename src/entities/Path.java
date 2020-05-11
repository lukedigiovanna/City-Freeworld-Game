package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import misc.MathUtils;
import misc.Vector2;
import world.Camera;

/**
 * Defines a path for an Entity to follow
 */
public class Path implements Serializable {
	private static final long serialVersionUID = 1L;
	
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
	
	public void print() {
		System.out.println("Entity Tags: "+entity.getTags());
		for (int i = 0; i < this.points.size(); i++) {
			System.out.println(i+": "+points.get(i).x+", "+points.get(i).y);	
		}
		System.out.println("goal: "+position);
	}
	
	private boolean forceSpeed = true;
	public void forceSpeed(boolean value) {
		this.forceSpeed = value;
	}
	
	public void setEntity(Entity e) {
		this.entity = e;
	}
	
	public void add(Vector2 point) {
		this.points.add(point);
	}
	
	/**
	 * Adds a the next point along the path that the entity
	 * must reach
	 * @param x
	 * @param y
	 */
	public void add(float x, float y) {
		this.add(new Vector2(x,y));
	}
	
	public void follow(float dt) {
		if (completed()) {
			if (forceSpeed)
				this.entity.getVelocity().zero();
			return;
		}
		Vector2 goal = this.points.get(position);
		//set the velocity to that point
		double angle = MathUtils.getAngle(entity.center(),goal);
		entity.setRotation((float)angle);
		if (!forceSpeed)
			speed = entity.getVelocity().getLength(); //use the current speed of the entity.
		entity.setVelocity(speed*(float)Math.cos(angle),speed*(float)Math.sin(angle));
		if (MathUtils.distance(entity.center(),goal) < 0.1)
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
	
	public void draw(Camera c) {
		if (this.points.size() <= 1)
			return;
		
		c.setColor(java.awt.Color.WHITE);
		for (int i = 0; i < this.points.size()-1; i++) {
			c.drawLine(points.get(i).x, points.get(i).y, points.get(i+1).x, points.get(i+1).y);
		}
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
