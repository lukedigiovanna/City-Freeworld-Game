package game;

public class FrameTimer {
	public FrameTimer() {
		
	}
	
	private long last = System.currentTimeMillis();
	
	public float mark() {
		long now = System.currentTimeMillis();
		long dt = now-last;
		last = now;
		return dt/1000.0f;
	}
}
