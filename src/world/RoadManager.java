package world;

import java.util.List;

import entities.vehicles.Vehicle;

/**
 * Responsible for spawning the cars the controlling their movement
 * @author luked
 *
 */
public class RoadManager {
	private List<Vehicle> cars;
	private Region region;
	
	public RoadManager(RoadMap roadMap) {
		this.region = roadMap.getRegion();
	}
}
