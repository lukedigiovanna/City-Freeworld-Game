package entities.vehicles;

import java.awt.image.BufferedImage;

import misc.ImageTools;
import world.Camera;

public class Car extends Vehicle {
	
	private static final BufferedImage img = ImageTools.getImage("car.png");
	
	public Car(float x, float y) {
		super(x, y, 19.0f/16, 14.0f/16);
		this.addTag("car");
	}

	@Override
	public void draw(Camera camera) {
		camera.drawImage(img, getX(), getY(), getWidth(), getHeight());
		hitbox.draw(camera);
	}
}
