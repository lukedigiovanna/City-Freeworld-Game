package entities.vehicles;

import java.awt.image.BufferedImage;

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
		//this.regenerateHitbox();
	}
	
	@Override
	public void draw(Camera camera) {
		camera.drawImage(model.image, getX(), getY(), getWidth(), getHeight());
		//this.drawHitbox(camera);
	}
}
