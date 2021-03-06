package world.event;

import java.io.Serializable;

import misc.Line;
import misc.MathUtils;
import misc.Vector2;
import world.WorldObject;

public interface CollisionEvent {
	public static final CollisionEvent
		/**
		 * Object simply stops all movement when it hits a wall
		 */
		STOP = (CollisionEvent & Serializable)(WorldObject obj, Line wall) -> {
			obj.getVelocity().zero();
		},
		/**
		 * Object slides along the wall in the direction of its prior movement and the shape of the wall
		 */
		SLIDE = (CollisionEvent & Serializable)(WorldObject obj, Line wall) -> {
			Vector2 vel = obj.getVelocity();

			float objDirection = vel.getAngle();
			float objSpeed = vel.getLength();
			float wallDirection = wall.angle();

			float finalDirection = wallDirection;

			obj.setVelocity((float)(Math.cos(finalDirection)*objSpeed), (float)(Math.sin(finalDirection)*objSpeed), vel.r);
		},
		/**
		 * The velocity vector is reflected off the wall
		 */
		BOUNCE = (CollisionEvent & Serializable)(WorldObject obj, Line wall) -> {
			float objDirection = obj.getVelocity().getAngle();
			float lineDirection = wall.angleToXAxis();
			float newDirection = 2 * lineDirection - objDirection;
			
			if (MathUtils.equals(lineDirection,0) || MathUtils.equals(lineDirection, (float)Math.PI)) {
				if (objDirection < Math.PI)
					newDirection =  - objDirection;
				else
					newDirection = (float)Math.PI - (objDirection - (float)Math.PI);
			}
			
			if (MathUtils.equals(lineDirection, (float)Math.PI/2) || MathUtils.equals(lineDirection, (float)Math.PI*3/2)) {
				newDirection = (float)Math.PI - objDirection;
			}
			
			obj.getVelocity().setAngle(newDirection);
		};
		
	void run(WorldObject object, Line wall);
}
