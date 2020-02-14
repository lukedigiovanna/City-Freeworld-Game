package world.regions;

import java.util.ArrayList;
import java.util.List;

import entities.Path;
import entities.vehicles.Car;
import misc.Line;
import misc.MathUtils;
import misc.Vector2;

public class Road {
	private List<Line> lines;
	//on average how many cars are spawned at the start of the road per real minute
	private float carRate = 5.0f;
	private Region region;
	
	private Car.Model[] models = {Car.Model.RED_CAR};
	
	private static final float MIN_WAIT = 3.0f; //how long to wait between each car
	
	private float timer = 0.0f, wait;
	
	public Road(Region region) {
		this.region = region;
		this.lines = new ArrayList<Line>();
		
		resetWait();
	}
	
	public void addLine(Line l) {
		this.lines.add(l);
	}
	
	public void update(float dt) {
		timer += dt;
		if (timer >= wait) {
			timer %= wait;
			
			//add in a car at the start of the wait
			Car.Model model = models[MathUtils.random(models.length)];
			Line startLine = this.lines.get(0);
			Car car = new Car(model,startLine.getEndpoints()[0].x,startLine.getEndpoints()[0].y);
			this.region.add(car);
			Path path = new Path();
			for (int i = 0; i < this.lines.size(); i++) {
				Line l = lines.get(i);
				Vector2 ep = l.getEndpoints()[0];
				path.add(ep.x,ep.y);
				if (i == this.lines.size() - 1) {
					Vector2 ep2 = l.getEndpoints()[1];
					path.add(ep2.x,ep2.y);
				}
			}
			car.queuePath(path);
		}
	}
	
	private void resetWait() {
		wait = MIN_WAIT + (60-MIN_WAIT) / carRate + MathUtils.random(-4,4);
	}
	
	public float carRate() {
		return this.carRate;
	}
}
