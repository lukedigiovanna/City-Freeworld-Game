package world;

import java.awt.Color;

import misc.*;

public class Hitbox {
	private WorldObject owner;
	
	public float[] model = {
			/* Example Model (square) */
			0.0f, 0.0f, 
			1.0f, 0.0f,
			1.0f, 1.0f,
			0.0f, 1.0f,
	};
	
	private Line[] lines;
	
	
	public Hitbox(WorldObject owner, float[] model) {
		this.owner = owner;
		this.model = model;
		generateLines();
	}
	
	/**
	 * Generates the array of lines from the current model
	 */
	public void generateLines() {
		int num = model.length;
		lines = new Line[num/2];
		for (int i = 0; i < num; i+=2) {
			int next = (i+2)%num;
			Vector2 ep1 = new Vector2(owner.getX()+model[i],owner.getY()+model[i+1]),
					ep2 = new Vector2(owner.getX()+model[next],owner.getY()+model[next+1]);
			lines[i/2] = new Line(ep1,ep2);
		}
		this.rotate(this.owner.getRotation());
	}
	
	/**
	 * Checks if two hitboxes are currently intersecting each other (collision)
	 * @param other The other hitbox to check
	 * @return true if they are intersecting, false if not
	 */
	public Vector2 intersecting(Hitbox other) {
		for (Line o : other.lines) {
			Vector2 intersection = intersecting(o);
			if (intersection != null)
				return intersection;
		}
		return null;
	}
	
	public Vector2 intersecting(Line line) {
		for (Line l : lines) {
			Vector2 intersection = line.intersects(l);
			if (intersection != null)
				return intersection;
		}
		return null;
	}
	
	public void rotate(float radians) {
		for (Line l : lines)
			l.rotateAbout(owner.center(),radians);
	}
	
	public void translate(float dx, float dy) {
		for (Line l : lines)
			l.translate(dx, dy);
	}
	
	/**
	 * Gets the midpoint of all the hitbox lines
	 * @return
	 */
	public Vector2 midpoint() {
		float x = 0, y = 0;
		for (Line l : lines) {
			Vector2 mp = l.midpoint();
			x+=mp.x;
			y+=mp.y;
		}
		Vector2 mp = new Vector2(x/lines.length,y/lines.length);
		mp.round(0.01f);
		return mp;
	}
	
	public Vector2[] getVertices() {
		Vector2[] verts = new Vector2[lines.length];
		for (int i = 0; i < verts.length; i++) 
			verts[i] = lines[i].getEndpoints()[0];
		return verts;
	}
	
	/**
	 * Gets the array of lines that make up the hitbox
	 * @return
	 */
	public Line[] getLines() {
		return lines;
	}
	
	public void updatePosition() {
		generateLines();
	}
	
	/**
	 * Gets the minimum x and y values in the first vector
	 * Gets the maximum x and y values in the second vector
	 * @return Always an array of vectors with length 2
	 */
	public Vector2[] getDomainAndRange() {
		Vector2[] eps = this.getVertices();
		float minX = eps[0].x, minY = eps[0].y;
		float maxX = eps[0].x, maxY = eps[0].y;
		for (int i = 1; i < eps.length; i++) {
			if (eps[i].x < minX)
				minX = eps[i].x;
			else if (eps[i].x > maxX)
				maxX = eps[i].x;
			if (eps[i].y < minY)
				minY = eps[i].y;
			else if (eps[i].y > maxY)
				maxY = eps[i].y;
		}
		Vector2[] arr = { new Vector2(minX,minY), new Vector2(maxX,maxY) };
		return arr;
	}
	
	public void draw(Camera c) {
		c.setColor(Color.RED);
		c.setStrokeWidth(0.1f);
		for (Line l : lines) {
			if (l == null)
				continue;
			c.drawLine(l);
		}
	}
}
