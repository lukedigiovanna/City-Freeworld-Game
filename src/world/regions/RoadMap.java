package world.regions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import misc.Line;
import world.Camera;

/**
 * Each 1 x 1 dimension on the region represents a road behavior
 * This should correspond to the cell grid
 *
 */
public class RoadMap implements Serializable {
	private static final long serialVersionUID = 1L;

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
	
	public void draw(Camera c) {
		for (Road road : roads) {
			road.draw(c);
		}
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
	
	/**
	 * Uses the link IDs to actually link the roads together
	 */
	public void linkRoads() {
		for (Road road : this.roads) {
			for (Integer id : road.getLinkedIDs()) {
				for (Road link : this.roads) {
					if (id == link.getID()) {
						road.linkRoad(link);
					}
				}
			}
		}
	}
	
	public Region getRegion() {
		return this.region;
	}
}
