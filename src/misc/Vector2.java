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
	
	//this value does not play a role in the actual rotation of the vector..
	//it merely holds information
	public float r; //not necessarily used but can be useful in some circumstances -- denotes rotation
	
	public Vector2(float x, float y) {
		this(x,y,0);
	}
	
	public Vector2(float x, float y, float r) {
		this.x = x;
		this.y = y;
		this.r = r;
	}
	
	//this + other
	public void add(Vector2 other) {
		this.x += other.x;
		this.y += other.y;
		this.r += other.r;
	}
	
	//this - other
	public void subtract(Vector2 other) {
		this.x -= other.x;
		this.y -= other.y;
		this.r -= other.r;
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
	
	/**
	 * Gets the dot product of this vector and another inputted vector
	 * @param b
	 * @return
	 */
	public float dotProduct(Vector2 b) {
		return x*b.x+y*b.y;
	}
	
	public void round(float decimalPoint) {
		this.x = MathUtils.round(this.x, decimalPoint);
		this.y = MathUtils.round(this.y, decimalPoint);
		this.r = MathUtils.round(this.r, decimalPoint);
	}
	
	/**
	 * Returns a new vector with the same information
	 * @return
	 */
	public Vector2 copy() {
		return new Vector2(this.x,this.y,this.r);
	}
	
	public String toString() {
		return "<"+this.x+", "+this.y+">";
	}
}