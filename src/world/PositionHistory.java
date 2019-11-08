package world;

import java.util.List;

import misc.Vector2;

public class PositionHistory {
	private WorldObject object;
	private List<HistoryPoint> history;
	
	public PositionHistory(WorldObject o) {
		object = o;
		update(0); //assign a position from time = 0.
	}
	
	public void update(float dt) {
		if (true)
			return;
		
		HistoryPoint last = null;
		if (history.size() > 0)
			last = history.get(0);
		
		HistoryPoint hp = new HistoryPoint(object.position.copy(),last,dt);
		
		history.add(0,hp);
	}
	
	private class HistoryPoint {
		private float dt;
		private Vector2 position, velocity, acceleration;
		
		public HistoryPoint(Vector2 position, HistoryPoint last, float dt) {
			this.dt = dt;
			
			if (last == null) {
				this.position = position;
				this.velocity = new Vector2(0,0);
				this.acceleration = new Vector2(0,0);
			}
		}
	}
}
