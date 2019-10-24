package misc;

public class MathUtils {
	public static int max(int max, int val) {
		if (val > max)
			return max;
		else
			return val;
	}
	
	public static float max(float max, float val) {
		if (val > max)
			return max;
		else
			return val;
	}
	
	public static double max(double max, double val) {
		if (val > max)
			return max;
		else 
			return val;
	}
	
	public static int min(int min, int val) {
		if (val < min)
			return min;
		else 
			return val;
	}
	
	public static float min(float min, float val) {
		if (val < min)
			return min;
		else 
			return val;
	}
	
	public static double min(double min, double val) {
		if (val < min)
			return min;
		else
			return val;
	}
	
	public static int clip(int min, int max, int val) {
		if (min(min,val) == min)
			return min;
		if (max(max,val) == max)
			return max;
		return val;
	}
	
	public static float clip(float min, float max, float val) {
		if (min(min,val) == min)
			return min;
		if (max(max,val) == max)
			return max;
		return val;
	}
	
	public static double clip(double min, double max, double val) {
		if (min(min,val) == min)
			return min;
		if (max(max,val) == max)
			return max;
		return val;
	}
	
	//give in y = mx + b form
	public static Vector2 intersects(double m1, double b1, double m2, double b2) {
		if (m1 == m2) //parallel so they will never intersect or will have infinite intersections
			return null;
		//set the equations equal to each other to find x point of intersection
		// m1x + b1 = m2x + b2
		double x = (b2-b1)/(m1-m2);
		//just plug that x value back into either solution to get y
		double y = m1 * x + b1;
		return new Vector2(x,y);
	}
	
	//given as an origin and an angle
	public static Vector2 intersects(Vector2 origin1, double angle1, Vector2 origin2, double angle2) {
		//get the slope and intercept of each
		double m1 = Math.tan(angle1);
		double m2 = Math.tan(angle2);
		//we have an x, y, a slope, but we need b
		//y = mx + b -> b = y - mx
		double b1 = origin1.getY() - m1 * origin1.getX();
		double b2 = origin2.getY() - m2 * origin2.getY();
		return intersects(m1,b1,m2,b2);
	}
	
	//returns the angle given by two coordinate components
	public static double getAngle(double x, double y) {
		//tan = y/x
		if (x == 0) { //avoid the divide by 0 case
			if (y > 0)
				return Math.PI/2;
			else if (y < 0)
				return 3*Math.PI/2;
			else
				return 0;
		}
		double theta = Math.atan(y/x);
		if (x < 0) { //tan is defined from -pi/2 to pi/2.. so if x is negative we need to add pi
			theta += Math.PI;
		}
		return theta;
	}
	
	public static double getAngle(double x1, double y1, double x2, double y2) {
		return getAngle(x2-x1,y2-y1);
	}
}
