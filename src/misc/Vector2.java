package misc;

public class Vector2 {
	/*
	 * simulates a mathematical vector with some operations:
	 * mutators:
	 * add another vector
	 * subtract another vector
	 * multiply by scalar
	 * divide by scalar
	 * normalize
	 * set x
	 * set y
	 * accessors:
	 * get x
	 * get y
	 * get length
	 * get angle
	 */
	
	private double x, y;
	
	public Vector2(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	//this + other
	public void add(Vector2 other) {
		this.x += other.x;
		this.y += other.y;
	}
	
	//this - other
	public void subtract(Vector2 other) {
		this.x -= other.x;
		this.y -= other.y;
	}
	
	public void multiply(double scalar) {
		this.x *= scalar;
		this.y *= scalar;
	}
	
	public void divide(double scalar) {
		if (scalar == 0)
			return;
		else {
			this.x/=scalar;
			this.y/=scalar;
		}
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	//returns the radian value of the angle
	public double getAngle() {
		return MathUtils.getAngle(this.x, this.y);
	}
	
	public double getLength() {
		return Math.sqrt(x*x+y*y);
	}
	
	public void normalize() {
		double length = getLength();
		if (length == 0)
			return;
		else
			this.divide(length);
	}
	
	public String toString() {
		return "<"+this.x+", "+this.y+">";
	}
}
