package entities;

import java.awt.Color;

import display.Animation;
import main.Program;
import main.Settings;
import misc.MathUtils;
import misc.Vector2;
import world.Camera;

public class Player extends Entity {

	public Player(float x, float y) {
		super(x, y, 0.8f, 0.8f);
		int vertices = 5;
		float[] model = new float[vertices*2];
		for (int i = 0; i < vertices; i++) {
			float theta = (float)i/vertices*(float)Math.PI*2;
			model[i*2] = 0.4f+(float)Math.cos(theta)*0.4f;
			model[i*2+1] = 0.4f+(float)Math.sin(theta)*0.4f;
		}
		//this.setModel(model);
		addTag("player");
	}
	
	private Animation ani = new Animation("rainbow","rainbow",1);

	@Override
	public void draw(Camera camera) {
		camera.drawImage(ani.getCurrentFrame(), getX(), getY(), getWidth(), getHeight());
	}

	private float speed = 0.0f;
	private float maxSpeed = 4.0f;
	@Override
	public void update(float dt) {
		ani.animate(dt);
//		this.velocity.x = 0;
//		this.velocity.y = 0;
//		if (Program.keyboard.keyDown(Settings.getSetting("move_right").charAt(0)))
//			this.velocity.x += speed;
//		if (Program.keyboard.keyDown(Settings.getSetting("move_left").charAt(0)))
//			this.velocity.x += -speed;
//		if (Program.keyboard.keyDown(Settings.getSetting("move_down").charAt(0)))
//			this.velocity.y += speed;
//		if (Program.keyboard.keyDown(Settings.getSetting("move_up").charAt(0)))
//			this.velocity.y += -speed;
		
		char accelerate = Settings.getSetting("move_up").charAt(0);
		char stop = Settings.getSetting("move_down").charAt(0);
		char turnLeft = Settings.getSetting("move_left").charAt(0);
		char turnRight = Settings.getSetting("move_right").charAt(0);
		
		float acceleration = 1.0f;
		float frictionalAcceleration = -0.3f;
		
		if (Program.keyboard.keyDown(accelerate)) {
			speed += acceleration*dt;
		}
		
		if (Program.keyboard.keyDown(stop)) {
			speed -= 2*acceleration*dt;
		}
		
		speed += frictionalAcceleration*dt;
		
		speed = MathUtils.clip(0, maxSpeed, speed);
		
		this.velocity.r = 0;
		
		if (Program.keyboard.keyDown(turnLeft)) {
			this.velocity.r -= (float) (Math.PI/2.0f);
		}
		
		if (Program.keyboard.keyDown(turnRight)) {
			this.velocity.r += (float) (Math.PI/2.0f);
		}
		
		this.velocity.x = (float) (Math.cos(this.getRotation()-Math.PI/2)*speed);
		this.velocity.y = (float) (Math.sin(this.getRotation()-Math.PI/2)*speed);
		
		Vector2[] eps = this.hitbox.getVertices();
		for (Vector2 ep : eps) {
			this.getRegion().add(new Particle(Particle.Type.BALL,ep.x,ep.y));
		}
	}
}
