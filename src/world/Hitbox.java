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
	public boolean intersecting(Hitbox other) {
		for (Line l : lines) 
			for (Line o : other.lines) {
				Vector2 intersection = l.intersects(o);
				if (intersection != null) {
					return true;
				}
			}
		return false;
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
