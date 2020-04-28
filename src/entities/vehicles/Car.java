package entities.vehicles;

import java.awt.image.BufferedImage;
import java.util.List;

import entities.Entity;
import misc.ImageTools;
import world.Camera;

public class Car extends Vehicle {
	private static final long serialVersionUID = 1L;

	public static enum Model {
		RED_RACECAR("red_racecar.png",19.0f/16.0f,14.0f/16.0f),
		BLUE_RACECAR("blue_racecar.png",19.0f/16.0f,14.0f/16.0f),
		WHITE_RACECAR("white_racecar.png",19.0f/16.0f,14.0f/16.0f),
		SCIFI_RACECAR("scifi_racecar.png",19.0f/16.0f,14.0f/16.0f);
		
		public float width, height;
		String path;
		transient BufferedImage image = ImageTools.IMAGE_NOT_FOUND;
		Model(String path, float width, float height) {
			this.width = width;
			this.height = height;
			this.path = path;
			this.image = ImageTools.getImage("vehicles/"+path);
		}
	}
	
	private Model model;
	
	public Car(Model model, float x, float y) {
		super(x, y, model.width, model.height);
		this.addTag("car");
		this.model = model;
	} 

	public void update(float dt) {
		super.update(dt);
		
		//Check in front of the car: if we are close then slow down, otherwise maintain speed
		//first get the entities that we care about - humans and other cars
		List<Entity> others = this.getRegion().getEntities().get("vehicle","human");
		for (Entity e : others) {
			if (this == e)
				continue; //dont check ourselves -- will always be true
			if (this.canSee_IgnoreWalls(e) && this.squaredDistanceTo(e) < 16) {
				this.brake(dt);
				break;
			}
		}
	}
	
	@Override
	public void draw(Camera camera) {
		camera.drawImage(model.image, getX(), getY(), getWidth(), getHeight());
	}
}
