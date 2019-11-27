package entities.player;

import java.awt.Color;
import java.awt.image.BufferedImage;

import entities.*;

import display.Animation;
import main.Program;
import main.Settings;
import misc.ImageTools;
import misc.MathUtils;
import misc.Vector2;
import world.Camera;

public class Player extends Entity {

	private String name = "Ronald McFcksht";
	private BankAccount bankAct;
	private Inventory inventory;
	private BufferedImage profilePicture;
	
	public Player(float x, float y) {
		super(x, y, 0.8f, 0.8f);
		this.bankAct = new BankAccount(this);
		this.inventory = new Inventory();
		this.profilePicture = ImageTools.getBufferedImage("character1.png");
		addTag("player");
	}
	
	private Animation ani = new Animation("rainbow","rainbow_",18);

	@Override
	public void draw(Camera camera) {
		camera.drawImage(ani.getCurrentFrame(), getX(), getY(), getWidth(), getHeight());
	}
	
	public String getMoneyDisplay() {
		double money = MathUtils.round(bankAct.getMoney(), 0.01);
		String monStr = money+"";
		if (money % 0.1 == 0)
			monStr+="0";
		if (money % 1.0 == 0)
			monStr+="0";
		return "$"+monStr;
	}

	private float speed = 0.0f;
	private float maxSpeed = 4.0f;
	@Override
	public void update(float dt) {
		ani.animate(dt);
		
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
			Color[] cols = {Color.RED,Color.GREEN,Color.BLUE};
			this.getRegion().addParticles(Particle.Type.BALL, cols[MathUtils.random(cols.length)], 1, 0.0f, ep.x, ep.y, 0, 0);
		}
	}
}
