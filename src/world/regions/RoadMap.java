package world.regions;

import java.util.ArrayList;
import java.util.List;

import misc.Line;

/**
 * Each 1 x 1 dimension on the region represents a road behavior
 * This should correspond to the cell grid
 *
 */
public class RoadMap {
	private Region region;

	/**
	 * A road is defined by a line where the car travels
	 * from endpoint 1 to endpoint 2.
	 */
	private List<Road> roads; 
	
	public RoadMap(Region region) {
		this.region = region;
		
		this.roads = new ArrayList<Road>();
	}
	
	public void update(float dt) {
		//go through each road
		for (Road road : this.roads) {
			//run the road update
			road.update(dt); //spawns in a car if it should
		}
	}
	
	public void addRoad(Road road) {
		this.roads.add(road);
	}
	
	public List<Road> getRoads() {
		return this.roads;
	}
	
	public Region getRegion() {
		return this.region;
	}
}
