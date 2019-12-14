package entities.player;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.List;

import entities.*;
import entities.projectiles.*;
import entities.vehicles.Vehicle;
import display.Animation;
import main.Program;
import main.Settings;
import misc.*;
import world.Camera;
import world.WorldObject;

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
		super(x, y, 0.75f, 1.375f);
		float[] model = {
				0, getHeight()/2,
				getWidth(), getHeight()/2,
				getWidth(), getHeight(),
				0, getHeight()
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
		if (riding == null) {
			camera.drawImage(curAni.getCurrentFrame(), getX(), getY(), getWidth(), getHeight());
			
		}this.hitbox.draw(camera);
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
		
		if (this.riding == null) {
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
		} else { //then we are in a car
			if (Program.keyboard.keyDown(up))
				this.riding.accelerate(dt);
			
			if (Program.keyboard.keyDown(down))
				this.riding.brake(dt);
			
			if (Program.keyboard.keyDown(left))
				this.riding.turnLeft(dt);
			
			if (Program.keyboard.keyDown(right))
				this.riding.turnRight(dt);
			
			this.setPosition(riding.centerX()-this.getWidth()/2, riding.centerY()-this.getHeight()/2);
		}
		
		if (Program.keyboard.keyPressed(KeyEvent.VK_F)) {
			if (riding == null)
				findVehicle();
			else
				exitVehicle();
		}
			
		//point and click shooting
		if (Program.mouse.isMouseDown()) {
			float angle = this.angleTo(this.getWorld().getMousePositionOnWorld());
			Projectile b = new Bullet(this,centerX(),centerY(),angle);
			this.region.add(b);
		}
	}
	
	/**
	 * Looks around the player for an available car to enter
	 * If there is a car close enough then it will get in
	 */
	private void findVehicle() {
		List<Entity> vehicles = this.getRegion().getEntities().get("vehicle");
		Entity closest = null;
		float maxDistance = 3.0f; //maximum distance the car can be away
		float distanceToVehicle = 99999.0f; //start the distance high
		for (Entity vehicle : vehicles) {
			//can't enter a vehicle that is moving -- that makes no sense
			if (vehicle.getVelocity().getLength() != 0)
				continue;
			//get the distance
			float distance = this.distanceTo(vehicle);
			if (distance < maxDistance && distance < distanceToVehicle) {
				distanceToVehicle = distance;
				closest = vehicle;
			}
		}
		//if the vehicle is found then enter the vehicle
		if (closest != null) 
			enterVehicle((Vehicle)closest);
	}
	
	/**
	 * Sets the player to be riding the vehicle, gives control
	 * @param vehicle
	 */
	public void enterVehicle(Vehicle vehicle) {
		vehicle.setDriver(this);
		this.riding = vehicle;
	}
	
	/**
	 * Leaves the vehicle currently being ridden
	 * if the player is riding one
	 */
	public void exitVehicle() {
		this.riding.setDriver(null);
		this.riding = null;
	}
}
