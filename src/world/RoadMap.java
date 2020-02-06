package world;

/**
 * Each 1 x 1 dimension on the region represents a road behavior
 * This should correspond to the cell grid
 *
 */
public class RoadMap {
	private Region region;
	private int width, height;
	
	private RoadCell[][] map;
	
	public RoadMap(Region region) {
		this.region = region;
	}
	
	public Region getRegion() {
		return this.region;
	}
}
