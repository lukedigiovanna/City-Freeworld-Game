package game;

public class FrameTimer {
	public FrameTimer() {
		
	}
	
	private float last = System.currentTimeMillis();
	
	public float mark() {
		float now = System.currentTimeMillis();
		System.out.println(now+", "+last);
		float dt = now-last;
		last = now;
		return dt;
	}
}
