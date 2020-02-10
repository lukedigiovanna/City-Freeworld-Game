package world.event;

import world.WorldObject;

public class CollisionEvent {
	public static final ObjectEvent
		STOP_X = new ObjectEvent() {
			@Override
			public void run(WorldObject object) {
				
			}
		},
		TEST = (WorldObject obj) -> {
			System.out.println("test");
			System.out.println("double test");
		};
}
