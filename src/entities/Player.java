package entities;

import java.awt.Color;

import main.Program;
import main.Settings;
import world.Camera;

public class Player extends Entity {

	public Player(float x, float y) {
		super(x, y, 0.8f, 1.6f);
		tags.add("player");
	}

	@Override
	public void draw(Camera camera) {
		camera.setColor(new Color(255,0,255,125));
		camera.fillRect(getX(), getY(), 0.8f, 1.6f);
	}
	
	public float centerX() {
		return getX() + 0.4f;
	}
	
	public float centerY() {
		return getY() + 0.8f;
	}

	@Override
	public void update(float dt) {
		if (Program.keyboard.keyDown(Settings.getSetting("move_right").charAt(0)))
			this.move(1 * dt, 0);
		if (Program.keyboard.keyDown(Settings.getSetting("move_left").charAt(0)))
			this.move(-1 * dt, 0);
		if (Program.keyboard.keyDown(Settings.getSetting("move_down").charAt(0)))
			this.move(0, 1 * dt);
		if (Program.keyboard.keyDown(Settings.getSetting("move_up").charAt(0)))
			this.move(0, - 1 * dt);
		
		this.rotate((float)Math.PI*2*0.2f*dt);
	}
}
