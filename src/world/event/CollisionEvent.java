package world.event;

import misc.Line;
import world.WorldObject;

public interface CollisionEvent {
	public static final CollisionEvent
		/**
		 * Object simply stops all movement when it hits a wall
		 */
		STOP = (WorldObject obj, Line wall) -> {
			obj.getVelocity().zero();
		},
		/**
		 * Object slides along the wall in the direction of its prior movement and the shape of the wall
		 */
		SLIDE = (WorldObject obj, Line wall) -> {
			float objDirection = obj.getVelocity().getAngle();
			
		};
		
	void run(WorldObject object, Line wall);
}
