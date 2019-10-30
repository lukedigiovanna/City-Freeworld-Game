package entities;

import misc.MathUtils;
import misc.Vector2;

public class Hitbox {
	private Entity owner;
	
	public float[] model = {
			/* Example Model (square) */
			0.0f, 0.0f, 
			1.0f, 0.0f,
			1.0f, 1.0f,
			0.0f, 1.0f,
			
			/*
			    ^       ^
			  
			  
			    ^       ^
			 */
	};
	
	private Line[] lines;
	
	
	public Hitbox(Entity owner, float[] model) {
		this.owner = owner;
		this.model = model;
		generateLines();
	}
	
	/**
	 * Generates the array of lines from the current model
	 */
	public void generateLines() {
		int lineNum = this.model.length/2;
		lines = new Line[lineNum];
		for (int i = 0; i < lineNum; i+=2) {
			int next = (i+2)%lineNum;
			lines[i/2] = Line.generateLine(owner.getX()+model[i],model[i+1],model[next],model[next+1]);
		}
	}
	
	/**
	 * Checks if two hitboxes are currently intersecting each other (collision)
	 * @param other The other hitbox to check
	 * @return true if they are intersecting, false if not
	 */
	public boolean intersecting(Hitbox other) {
		for (Line l : lines) 
			for (Line o : other.lines) 
				if (l.intersects(o))
					return true;
		return false;
	}
	
	private static class Line {
		private float leftDomain, rightDomain;
		private float slope, intercept;
		
		public Line(float slope, float intercept, float ld, float rd) {
			this.slope = slope;
			this.intercept = intercept;
			this.leftDomain = ld;
			this.rightDomain = rd;
		}
		
		public static Line generateLine(float x1, float y1, float x2, float y2) {
			float slope = (y2-y1)/(x2-x1);
			
			// y = mx + b, b = y - mx
			float intercept = y1 - slope * x1;
			
			float ld = MathUtils.min(x1, x2),
				  rd = MathUtils.max(x1, x2);
			
			return new Line(slope,intercept,ld,rd);
		}
		
		public boolean intersects(Line other) {
			Vector2 point = MathUtils.intersects(this.slope, this.intercept, other.slope, other.intercept);
			
			if (point != null) {
				if (point.x == MathUtils.INFINITY) 
					return true; //we have infinite solutions
				if (point.x >= leftDomain && point.x <= rightDomain) //if the intersection is along any part of either line
					return true;
			} 
			
			return false;
		}
		
		public String toString() {
			return "y = "+slope+"x + "+intercept+" {"+leftDomain+" <= x  <= "+rightDomain+"}";
		}
	}
}
