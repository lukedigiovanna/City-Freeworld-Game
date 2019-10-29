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
	
	public float x, y;
	
	public Vector2(float x, float y) {
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
	
	public void multiply(float scalar) {
		this.x *= scalar;
		this.y *= scalar;
	}
	
	public void divide(float scalar) {
		if (scalar == 0)
			return;
		else {
			this.x/=scalar;
			this.y/=scalar;
		}
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	//returns the radian value of the angle
	public float getAngle() {
		return (float)MathUtils.getAngle(this.x, this.y);
	}
	
	public float getLength() {
		return (float)Math.sqrt(x*x+y*y);
	}
	
	public void normalize() {
		float length = getLength();
		if (length == 0)
			return;
		else
			this.divide(length);
	}
	
	public String toString() {
		return "<"+this.x+", "+this.y+">";
	}
}
