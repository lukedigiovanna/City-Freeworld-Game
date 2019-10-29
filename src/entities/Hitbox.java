package entities;

public class Hitbox {
	private Entity owner;
	
	public float[] model;
	
	
	public Hitbox(Entity owner, float width, float height) {
		this.owner = owner;
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Checks if two hitboxes are currently intersecting each other (collision)
	 * @param other The other hitbox to check
	 * @return true if they are intersecting, false if not
	 */
	public boolean intersecting(Hitbox other) {
		
	}
	
	private class Line {
		
		
		public Line() {
			
		}
	}
}
