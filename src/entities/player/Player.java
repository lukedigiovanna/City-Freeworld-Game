package entities.player;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.List;

import entities.*;
import entities.projectiles.*;
import display.Animation;
import main.Program;
import main.Settings;
import misc.*;
import world.Camera;

public class Player extends Entity {
	
	private static final List<BufferedImage> 
		IDLE_FRONT = ImageTools.getImages("character", "idle_front_"),
		WALK_FRONT = ImageTools.getImages("character", "walk_front_"),
		IDLE_BACK = ImageTools.getImages("character", "idle_back_"),
		WALK_BACK = ImageTools.getImages("character", "walk_back_"),
		IDLE_RIGHT_SIDE = ImageTools.getImages("character", "idle_side_"),
		WALK_RIGHT_SIDE = ImageTools.getImages("character", "walk_side_"),
		IDLE_LEFT_SIDE = ImageTools.flipVerticle(ImageTools.getImages("character", "idle_side_")),
		WALK_LEFT_SIDE = ImageTools.flipVerticle(ImageTools.getImages("character", "walk_side_"));
	
	public static enum Orientation {
		UP,
		DOWN,
		LEFT,
		RIGHT
	}
	
	private String name = "Earl";
	private BankAccount bankAct;
	private Inventory inventory;
	private BufferedImage profilePicture;
	
	private Orientation orientation = Orientation.DOWN;
	
	public Player(float x, float y) {
		super(x, y, 1.5f, 2.75f);
		float[] model = {
				0, 2.75f/2,
				1.5f, 2.75f/2,
				1.5f, 2.75f,
				0, 2.75f
		};
		this.setModel(model);
		this.bankAct = new BankAccount(this);
		this.inventory = new Inventory();
		this.profilePicture = ImageTools.getImage("profile_1.png");
		addTag("player");
	}
	
	private Animation idle_front = new Animation(IDLE_FRONT,2),
					  walk_front = new Animation(WALK_FRONT,3),
					  idle_back = new Animation(IDLE_BACK,2),
					  walk_back = new Animation(WALK_BACK,3),
					  idle_right_side = new Animation(IDLE_RIGHT_SIDE,2),
					  walk_right_side = new Animation(WALK_RIGHT_SIDE,4),
					  idle_left_side = new Animation(IDLE_LEFT_SIDE,2),
					  walk_left_side = new Animation(WALK_LEFT_SIDE,4);

	private Animation curAni = idle_front;
	
	@Override
	public void draw(Camera camera) {
		camera.drawImage(curAni.getCurrentFrame(), getX(), getY(), getWidth(), getHeight());
		this.hitbox.draw(camera);
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

	public BufferedImage getProfilePicture() {
		return this.profilePicture;
	}
	
	public String getName() {
		return this.name;
	}

	public String getReputation() {
		return "None";
	}
	
	private float speed = 2.0f;
	private float maxSpeed = 4.0f;
	@Override
	public void update(float dt) {
		char up = Settings.getSetting("move_up").charAt(0);
		char down = Settings.getSetting("move_down").charAt(0);
		char left = Settings.getSetting("move_left").charAt(0);
		char right = Settings.getSetting("move_right").charAt(0);
		
		this.velocity.zero();
		
		speed = 2.0f;
		if (Program.keyboard.keyDown(KeyEvent.VK_SHIFT))
			speed *= 2;
		
		float x = 0, y = 0;
		
		if (Program.keyboard.keyDown(down))
			y++;
		if (Program.keyboard.keyDown(up))
			y--;
		if (Program.keyboard.keyDown(left))
			x--;
		if (Program.keyboard.keyDown(right))
			x++;
		
		
		
		if (x != 0 || y != 0) {
			double dir = MathUtils.getAngle(x, y);
			this.velocity.x = MathUtils.round((float)Math.cos(dir)*speed,0.01f);
			this.velocity.y = MathUtils.round((float)Math.sin(dir)*speed,0.01f);
		}
		//prioritizes horizontal orientation
		if (this.velocity.x < 0)
			this.orientation = Orientation.LEFT;
		else if (this.velocity.x > 0)
			this.orientation = Orientation.RIGHT;
		else if (this.velocity.y > 0)
			this.orientation = Orientation.DOWN;
		else if (this.velocity.y < 0) 
			this.orientation = Orientation.UP;
		
		if (this.velocity.getLength() != 0)
			switch (this.orientation) {
			case DOWN: 
				curAni = walk_front;
				break;
			case UP:
				curAni = walk_back;
				break;
			case RIGHT:
				curAni = walk_right_side;
				break;
			case LEFT:
				curAni = walk_left_side;
			}
		else
			switch (this.orientation) {
			case DOWN:
				curAni = idle_front;
				break;
			case UP:
				curAni = idle_back;
				break;
			case RIGHT: 
				curAni = idle_right_side;
				break;
			case LEFT:
				curAni = idle_left_side;
			}
		
		curAni.animate(dt*(speed/2));
			
		//point and click shooting
		if (Program.mouse.isMouseDown()) {
			float angle = this.angleTo(this.getWorld().getMousePositionOnWorld());
			Projectile b = new Bullet(this,centerX(),centerY(),angle);
			this.region.add(b);
		}
		//arrow key shooting
		
//		float acceleration = 1.0f;
//		float frictionalAcceleration = -0.3f;
//		
//		if (Program.keyboard.keyDown(up)) {
//			speed += acceleration*dt;
//		}
//		
//		if (Program.keyboard.keyDown(down)) {
//			speed -= 2*acceleration*dt;
//		}
//		
//		speed += frictionalAcceleration*dt;
//		
//		speed = MathUtils.clip(0, maxSpeed, speed);
//		
//		this.velocity.r = 0;
//		
//		if (Program.keyboard.keyDown(left)) {
//			this.velocity.r -= (float) (Math.PI/2.0f);
//		}
//		
//		if (Program.keyboard.keyDown(right)) {
//			this.velocity.r += (float) (Math.PI/2.0f);
//		}
//		
//		this.velocity.x = (float) (Math.cos(this.getRotation()-Math.PI/2)*speed);
//		this.velocity.y = (float) (Math.sin(this.getRotation()-Math.PI/2)*speed);
	}
}
