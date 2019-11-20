package entities;

import java.awt.Color;

import main.Program;
import main.Settings;
import misc.Vector2;
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
		camera.setColor(new Color(0,255,0));
		camera.fillOval(centerX()-0.04f,centerY()-0.04f,0.08f,0.08f);
	}

	private float speed = 2.0f;
	@Override
	public void update(float dt) {
		this.velocity.x = 0;
		this.velocity.y = 0;
		if (Program.keyboard.keyDown(Settings.getSetting("move_right").charAt(0)))
			this.velocity.x += speed;
		if (Program.keyboard.keyDown(Settings.getSetting("move_left").charAt(0)))
			this.velocity.x += -speed;
		if (Program.keyboard.keyDown(Settings.getSetting("move_down").charAt(0)))
			this.velocity.y += speed;
		if (Program.keyboard.keyDown(Settings.getSetting("move_up").charAt(0)))
			this.velocity.y += -speed;
		
		this.velocity.r = (float)Math.PI*2*0.2f;
		
		Vector2[] eps = this.hitbox.getVertices();
		for (Vector2 ep : eps) {
			this.getRegion().add(new Particle(Particle.Type.BALL,ep.x,ep.y));
		}
	}
}
