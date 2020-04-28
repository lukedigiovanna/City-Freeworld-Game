package world.event;

import misc.MathUtils;
import misc.Vector2;
import world.WorldObject;

/**
 * Defines how two objects should interact when they collide
 */
public interface ObjectCollisionEvent {
	
	public static final ObjectCollisionEvent 
		CONSERVE_MOMENTUM_AND_KINETIC_ENERGY = (WorldObject obj, WorldObject other) -> {
			//use principles of conservation of linear momentum and kinetic energy and angular momentum to adjust the velocities
			// where momentum: p = mv and p(i) = p(f) and KE = 1/2mv^2
			// this has a corollary to the rotational world where
			// angular momentum: L = Iw (w is angular velocity) and KE = 1/2Iw^2
			// this requires access to the moment of inertia, which can be accessed from the WorldObject class
			// 
			// we set up a system of equations for conservation of p and KE to solve for final velocities
			// we will get two solutions, however one will be identical to the initial velocities
			// so we will choose the other solution as our new velocity
			//
			// this is solved for in each dimension: x, y, and rotational
			float m1 = obj.getMass(), m2 = other.getMass();
			float vx1 = obj.getVelocity().x, vx2 = other.getVelocity().x;
			//now solve in the X direction using our helper method
			Vector2 vfx = getFinalVelocities(m1, m2, vx1, vx2);
			//now solve in the y direction
			float vy1 = obj.getVelocity().y, vy2 = other.getVelocity().y;
			Vector2 vfy = getFinalVelocities(m1, m2, vy1, vy2);
			//now solve in the rotational direction
			float i1 = obj.getMomentOfInertia(), i2 = other.getMomentOfInertia();
			float w1 = obj.getVelocity().r, w2 = other.getVelocity().r;
			Vector2 vfr = getFinalVelocities(i1, i2, w1, w2);
			obj.setVelocity(vfx.x, vfy.x, vfr.x);
//			other.setVelocity(vfx.y, vfy.y, vfr.y);
		};
		
	public static final float ENERGY_LOSS_COEFFICIENT = 0.10f;
	/**
	 * Relates momentum and kinetic energy to calculate final velocity
	 */
	public static Vector2 getFinalVelocities(float m1, float m2, float v1, float v2) {
		float totalKE = 0.5f * (m1 * v1 * v1 + m2 * v2 * v2);
		float loss = totalKE * ENERGY_LOSS_COEFFICIENT;
		loss = 0;
		//get a, b, and c coefficients of standard equation for calculation of quadratic formula
		float a = (m2 * m2) / m1 + m2;
		float b = -2  * m2 * v1 - (2 * m2 * m2 * v2) / m1;
		float c = m1 * v1 * v1 + 2 * m2 * v1 * v2 + (m2 * m2 * v2 * v2) / m1 - m1 * v1 * v1 - m2 * v2 * v2 + 2 * loss;
		//now utilize quadratic equation
		//first in positive direction
		float vf2 = (float)((-b + Math.sqrt(b * b - 4 * a * c))/(2 * a));
		//if that equals our first velocity, try the other solution
		if (MathUtils.equals(vf2, v2)) 
			vf2 = (float)((-b - Math.sqrt(b * b - 4 * a * c))/(2 * a));
		float vf1 = v1 + (m2*(v2-vf2))/m1;
		return new Vector2(vf1,vf2);
	}
	
	void run(WorldObject thisObject, WorldObject otherObject);
}
