package misc;

public class Line {
	
	//endpoint info
	private Vector2 endpoint1, endpoint2;
	
	//slopeintercept info -- not very good for vertical lines.. endpoints are better
	private float slope, intercept, ld, rd;
	
	/**
	 * Generates a line given slope intercept form with boundary conditions
	 * Use great caution when inputting an extrememly large slope (infinity)
	 * @param slope
	 * @param intercept
	 * @param ld
	 * @param rd
	 */
	public Line(float slope, float intercept, float ld, float rd) {
		setFromSI(slope,intercept,ld,rd);
	}
	
	/**
	 * Generates a line given 2 endpoints
	 * @param ep1 The first endpoint
	 * @param ep2 The second endpoint
	 */
	public Line(Vector2 ep1, Vector2 ep2) {
		setFromEP(ep1,ep2);
	}
	
	/**
	 * Sets the data given endpoint info
	 */
	private void setFromEP(Vector2 ep1, Vector2 ep2) {
		endpoint1 = ep1;
		endpoint2 = ep2;
		
		if (ep2.x-ep1.x == 0)
			slope = MathUtils.INFINITY;
		else
			slope = (ep2.y-ep1.y)/(ep2.x-ep1.x);
		
		intercept = ep1.y - slope * ep1.x;
		
		ld = MathUtils.min(ep1.x, ep2.x);
		rd = MathUtils.max(ep1.x, ep2.x);
	}
	
	/**
	 * Sets the data given slope - intercept data
	 */
	private void setFromSI(float slope, float intercept, float ld, float rd) {
		this.slope = slope;
		this.intercept = intercept;
		this.ld = ld;
		this.rd = rd;
		
		endpoint1 = new Vector2(ld,get(ld));
		endpoint2 = new Vector2(rd,get(rd));
	}
	
	/**
	 * Returns the point of intersection if the lines intersect
	 * Returns null if there is no point of intersection or if there are infinite solutions
	 * @param other The line to check for intersection with
	 * @return
	 */
	public Vector2 intersects(Line other) {
		return MathUtils.intersects(endpoint1, endpoint2, other.endpoint1, other.endpoint2);
	}
	
	/**
	 * Rotates the line a certain number of radians about the center of the line
	 * @param radians
	 */
	public void rotate(float radians) {
		rotateAbout(midpoint(),radians);
	}
	
	/**
	 * Rotates the line around some point
	 * @param point
	 * @param radians
	 */
	public void rotateAbout(Vector2 point, float radians) {
		Vector2 mp = midpoint();
		float dx = point.x-mp.x, dy = point.y-mp.y;
		float r = length()/2;
		float dist = (float)Math.sqrt(dx*dx+dy*dy);
		float newAngle = (float) (radians + MathUtils.getAngle(point.x, point.y, mp.x, mp.y));
		float theta = radians + angle();
		float mx = (float) (point.x + Math.cos(newAngle)*dist), my = (float) (point.y + Math.sin(newAngle)*dist);
		endpoint1.setX((float) (mx + Math.cos(theta)*r));
		endpoint1.setY((float) (my + Math.sin(theta)*r));
		endpoint2.setX((float) (mx - Math.cos(theta)*r));
		endpoint2.setY((float) (my - Math.sin(theta)*r));
		
		setFromEP(endpoint1,endpoint2);
	}
	
	/**
	 * Gets the length of the line segment
	 * @return
	 */
	public float length() {
		float dx = endpoint1.x-endpoint2.x, dy = endpoint1.y-endpoint2.y;
		return (float)(Math.sqrt(dx*dx+dy*dy));
	}
	
	/**
	 * Gets the angle that the line makes with the horizontal
	 * @return
	 */
	public float angle() {
		return (float)MathUtils.getAngle(endpoint1.x, endpoint1.y, endpoint2.x, endpoint2.y);
//		if (slope == MathUtils.INFINITY) {
//			if (endpoint1.y < endpoint2.y)
//				return (float) (Math.PI/2);
//			else
//				return (float) (3 * Math.PI / 2);
//		} else {
//			float angle = (float)Math.atan(slope);
//			if (endpoint1.x > endpoint2.x)
//				angle += Math.PI;
//			return angle;
//		}
	}
	
	/**
	 * Returns an array of the endpoints, the length will always be 2
	 * @return
	 */
	public Vector2[] getEndpoints() {
		Vector2[] eps = {endpoint1,endpoint2};
		return eps;
	}
	
	/**
	 * Evaluates the y value at an x value
	 * @param x
	 * @return
	 */
	public float get(float x) {
		return x * slope + intercept;
	}
	
	/**
	 * Gets the midpoint of the line
	 * @return
	 */
	public Vector2 midpoint() {
		return new Vector2((endpoint1.x+endpoint2.x)/2.0f,(endpoint1.y+endpoint2.y)/2.0f);
	}
	
	public void translate(float dx, float dy) {
		Vector2 add = new Vector2(dx,dy);
		endpoint1.add(add);
		endpoint2.add(add);
		
		setFromEP(endpoint1,endpoint2);
	}
	
	/**
	 * Calculates the shortest distance from a point to the line segment
	 * this line is defined by two endpoints: (x1, y1), (x2, y2)
	 * the endpoint is defined by one point: (x0,y0)
	 * @param point the other point
	 * @return
	 */
	public float distance(Vector2 point) {
		float l2 = this.endpoint1.getDistanceSquared(this.endpoint2);
		if (l2 == 0.0f) 
			return endpoint1.getDistanceSquared(point);
		float t = ((point.x - endpoint1.x) * (endpoint2.x - endpoint1.x) + (point.y - endpoint1.y) * (endpoint2.y - endpoint1.y))/l2;
		t = MathUtils.max(0.0f, MathUtils.min(1.0f, t));
		Vector2 p = new Vector2((endpoint1.x + t * (endpoint2.x - endpoint1.x)),(endpoint1.y + t * (endpoint2.y - endpoint1.y)));
		return point.getDistanceSquared(p);
	}
	
	/**
	 * Gets a vector that represents the dx and dy of the line segment.
	 * Origin of the vector is at 0, 0
	 * Useful when determining normal lines (vectors) to this line.
	 * @return
	 */
	public Vector2 getVectorRepresentation() {
		return new Vector2(endpoint2.x-endpoint1.x,endpoint2.y-endpoint1.y);
	}
	
	/**
	 * Gets the string representation of the line
	 * Shows the equation in slope - intercept form with the domain
	 */
	public String toString() {
		return "y = "+slope+"x + "+intercept+" {"+ld+" <= x  <= "+rd+"}";
	}
}
	