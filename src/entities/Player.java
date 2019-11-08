package entities;

import java.awt.Color;

import main.Program;
import main.Settings;
import world.Camera;

public class Player extends Entity {

	public Player(float x, float y) {
		super(x, y, 0.8f, 0.8f);
		addTag("player");
	}

	@Override
	public void draw(Camera camera) {
		camera.setColor(new Color(255,0,255,125));
		camera.fillRect(getX(), getY(), getWidth(), getHeight());
	}

	private float speed = 2.0f;
	@Override
	public void update(float dt) {
		if (Program.keyboard.keyDown(Settings.getSetting("move_right").charAt(0)))
			this.move(speed * dt, 0);
		if (Program.keyboard.keyDown(Settings.getSetting("move_left").charAt(0)))
			this.move(-speed * dt, 0);
		if (Program.keyboard.keyDown(Settings.getSetting("move_down").charAt(0)))
			this.move(0, speed * dt);
		if (Program.keyboard.keyDown(Settings.getSetting("move_up").charAt(0)))
			this.move(0, - speed * dt);
		
		this.rotate((float)Math.PI*2*0.2f*dt);
	}
}
