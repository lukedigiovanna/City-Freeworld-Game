package misc;

public class MathUtils {
	/**
	 * The greatest floating point value the computer can store.. represents infinity
	 */
	public static final float INFINITY = 99999999999999999999999999999999999999f;
	
	public static int ceil(int max, int val) {
		if (val > max)
			return max;
		else
			return val;
	}
	
	public static float ceil(float max, float val) {
		if (val > max)
			return max;
		else
			return val;
	}
	
	public static double ceil(double max, double val) {
		if (val > max)
			return max;
		else 
			return val;
	}
	
	public static int floor(int min, int val) {
		if (val < min)
			return min;
		else 
			return val;
	}
	
	public static float floor(float min, float val) {
		if (val < min)
			return min;
		else 
			return val;
	}
	
	public static double floor(double min, double val) {
		if (val < min)
			return min;
		else
			return val;
	}
	
	public static int clip(int min, int max, int val) {
		if (floor(min,val) == min)
			return min;
		if (ceil(max,val) == max)
			return max;
		return val;
	}
	
	public static float clip(float min, float max, float val) {
		if (floor(min,val) == min)
			return min;
		if (ceil(max,val) == max)
			return max;
		return val;
	}
	
	public static double clip(double min, double max, double val) {
		if (floor(min,val) == min)
			return min;
		if (ceil(max,val) == max)
			return max;
		return val;
	}
	
	public static float round(float val, float decimal) {
		float scale = 1.0f/decimal;
		val *= scale;
		return Math.round(val)/scale;
	}
	
	public static double round(double val, double decimal) {
		double scale = 1.0/decimal;
		val *= scale;
		return Math.round(val)/scale;
	}
	
	public static int min(int num1, int num2) {
		if (num1 < num2)
			return num1;
		else //also includes if they are the same, just return either number it doesn't matter
			return num2;
	}
	
	public static float min(float num1, float num2) {
		if (num1 < num2)
			return num1;
		else
			return num2;
	}
	
	public static double min(double num1, double num2) {
		if (num1 < num2)
			return num1;
		else
			return num2;
	}
	
	public static int max(int num1, int num2) {
		if (num1 > num2)
			return num1;
		else
			return num2;
	}
	
	public static float max(float num1, float num2) {
		if (num1 > num2)
			return num1;
		else
			return num2;
	}
	
	public static double max(double num1, double num2) {
		if (num1 > num2)
			return num1;
		else
			return num2;
	}
	
	//give in y = mx + b form
	/**
	 * Returns the point of intersection on a line if it exists
	 * @param m1 the slope of the first line
	 * @param b1 the intercept of the first line
	 * @param m2 the slope of the second line
	 * @param b2 the intercept of the second line
	 * @return the point of intersection as a vector if it exists, null if they are
	 * parallel, and an infinity vector if there are infinite solutions.
	 */
	public static Vector2 intersects(double m1, double b1, double m2, double b2) {
		if (m1 == m2) {//parallel so they will never intersect or will have infinite intersections
			if (b1 != b2)
				return null;
			else 
				return new Vector2(INFINITY, INFINITY);
		}
		//set the equations equal to each other to find x point of intersection
		// m1x + b1 = m2x + b2
		double x = (b2-b1)/(m1-m2);
		//just plug that x value back into either solution to get y
		double y = m1 * x + b1;
		return new Vector2((float)x,(float)y);
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
	
	/**
	 * Gets the angle from two input coordinates from the origin
	 * @param x horizontal distance from origin
	 * @param y vertical distance from origin
	 * @return angle it makes
	 */
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
	
	/**
	 * Gets the angle between two endpoints
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public static double getAngle(double x1, double y1, double x2, double y2) {
		return getAngle(x2-x1,y2-y1);
	}
}
