package misc;

public class Rectangle {
	public Vector2 position, dimension;
	
	public Rectangle(float x, float y, float w, float h) {
		this.position = new Vector2(x,y);
		this.dimension = new Vector2(w,h);
	}
	
	public float getX() {
		return position.x;
	}
	
	public float getY() {
		return position.y;
	}
	
	public float getWidth() {
		return dimension.x;
	}
	
	public float getHeight() {
		return dimension.y;
	}
}
