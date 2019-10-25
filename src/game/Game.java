package game;

public class Game {
	
	public static final int REFRESH_RATE = 50; // milliseconds
	
	private Thread updateLoop;
	private FrameTimer ft;
	
	private boolean paused = false;
	
	public Game() {
		updateLoop = new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(REFRESH_RATE);
						gameLoop();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		updateLoop.start();
	}
	
	public void togglePause() {
		this.paused = !paused;
	}
	
	public void gameLoop() {
		//elapsed time since last loop call.. for regulating game operation speeds
		//across varying operating system performances
		float dt = ft.mark();
		
		if (this.paused)
			return; //dont run the game loop if we are paused
	}
}
