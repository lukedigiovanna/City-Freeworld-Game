package world.regions;

import java.util.ArrayList;
import java.util.List;

import entities.Path;
import entities.vehicles.Car;
import misc.Line;
import misc.MathUtils;
import misc.Vector2;

public class Road {
	private List<Vector2> points;
	private List<Car> cars;
	//on average how many cars are spawned at the start of the road per real minute
	private float carRate = 10.0f;
	private Region region;
	
	private Car.Model[] models = Car.Model.values();
	
	private static final float MIN_WAIT = 1.5f; //how long to wait between each car
	
	private float timer = 0.0f, wait;
	
	public Road(Region region) {
		this.region = region;
		this.points = new ArrayList<Vector2>();
		this.cars = new ArrayList<Car>();
		
		resetWait();
	}
	
	public void addPoint(Vector2 p) {
		this.points.add(p);
	}
	
	public void update(float dt) {
		timer += dt;
		if (timer >= wait) {
			timer %= wait;
			
			//add in a car at the start of the wait
			Car.Model model = models[MathUtils.random(models.length)];
			Vector2 startPoint = this.points.get(0);
			Car car = new Car(model,startPoint.x,startPoint.y);
			this.region.add(car);
			Path path = new Path(car);
			for (int i = 1; i < this.points.size(); i++) 
				path.add(points.get(i).x,points.get(i).y);
			path.print();
			car.queuePath(path);
			this.cars.add(car);
		}
	}
	
	private void resetWait() {
		wait = MIN_WAIT + (60-MIN_WAIT) / carRate + MathUtils.random(-4,4);
		wait = MathUtils.max(MIN_WAIT,wait);
	}
	
	public float carRate() {
		return this.carRate;
	}
}
