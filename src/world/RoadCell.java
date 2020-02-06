package world;

public class RoadCell {
	public static enum Type {
		NOT_A_ROAD,
		SIDE_WALK,
		EAST_FACING,
		WEST_FACING,
		NORTH_FACING,
		SOUTH_FACING,
		LEFT_TURN,
		RIGHT_TURN
	}
	
	private float rotation;
	
	public RoadCell() {
		
	}
}
