package world.event;

import world.WorldObject;

public interface ObjectEvent {
	void run(WorldObject object);
}
