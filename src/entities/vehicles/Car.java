package entities.vehicles;

import java.awt.image.BufferedImage;

import misc.ImageTools;
import world.Camera;

public class Car extends Vehicle {
	
	public static enum Model {
		RED_CAR("car.png",19.0f/16.0f,14.0f/16.0f);
		
		float width, height;
		String path;
		BufferedImage image = ImageTools.IMAGE_NOT_FOUND;
		Model(String path, float width, float height) {
			this.width = width;
			this.height = height;
			this.path = path;
			this.image = ImageTools.getImage(path);
		}
	}
	
	private Model model;
	
	public Car(Model model, float x, float y) {
		super(x, y, model.width, model.height);
		this.addTag("car");
		this.model = model;
	} 

	@Override
	public void draw(Camera camera) {
		camera.drawImage(model.image, getX(), getY(), getWidth(), getHeight());
		//this.drawHitbox(camera);
	}
}
