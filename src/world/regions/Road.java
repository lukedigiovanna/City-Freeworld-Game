package world.regions;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import entities.Path;
import entities.vehicles.Car;
import misc.Line;
import misc.MathUtils;
import misc.Vector2;
import world.Camera;

public class Road implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id; //for linking purposes in the data format representation of the road map
	
	private List<Vector2> points; //every road should be made up of AT LEAST 2 points
	private List<Road> linkedRoads; //possible roads for the car to go to once it reaches the end of this road
	private List<Integer> linkedRoadsID;
	private List<Road> intersectionRoads;
	private List<Integer> intersectionRoadsID;
	private List<Car> cars; 
	private List<Integer> goals; //the index of the cars and goals arrays should match
	//on average how many cars are spawned at the start of the road per real minute
	private float carRate = 4.0f;
	private Region region;
	
	private Car.Model[] models = Car.Model.values();
	
	private static final float MIN_WAIT = 1.5f; //how long to wait between each car
	
	private float timer = 0.0f, wait;
	
	private float speedLimit = 3; //measured in m/s
	
	public Road(Region region, int id) {
		this.id = id;
		this.region = region;
		this.points = new ArrayList<Vector2>();
		this.cars = new ArrayList<Car>();
		this.goals = new ArrayList<Integer>();
		this.linkedRoads = new ArrayList<Road>();
		this.linkedRoadsID = new ArrayList<Integer>();
		this.intersectionRoads = new ArrayList<Road>();
		this.intersectionRoadsID = new ArrayList<Integer>();
		
		resetWait();
	}
	
	public void draw(Camera c) {
		c.setStrokeWidth(0.025f);
		c.setColor(Color.GREEN);
		for (int i = 0; i < points.size()-1; i++) {
			Vector2 p1 = points.get(i), p2 = points.get(i+1);
			c.drawLine(p1.x,p1.y,p2.x,p2.y);
		}
	}
	
	public void addPoint(Vector2 p) {
		this.points.add(p);
	}
	
	public void linkRoad(Road road) {
		this.linkedRoads.add(road);
	}
	
	public void linkRoad(Integer id) {
		this.linkedRoadsID.add(id);
	}
	
	public List<Integer> getLinkedIDs() {
		return this.linkedRoadsID;
	}
	
	public void intersectRoad(Road road) {
		this.intersectionRoads.add(road);
	}
	
	public void intersectRoad(Integer id) {
		this.intersectionRoadsID.add(id);
	}
	
	public List<Integer> getIntersectionIDs() {
		return this.intersectionRoadsID;
	}
	
	public int getID() {
		return this.id;
	}
	
	public void setCarRate(float rate) {
		this.carRate = rate;
		resetWait();
	}
	
	private float carWaitTimer = 0f;
	private boolean isCarAtStopSign = false;
	
	public void update(float dt) {
		timer += dt;
		if (timer >= wait) {
			timer %= wait;
			
			//add in a car at the start of the wait
			Car.Model model = models[MathUtils.random(models.length)];
			Vector2 startPoint = this.points.get(0);
			Car car = new Car(model,startPoint.x-model.width/2,startPoint.y-model.height/2);
			addCar(car);
		}
		
		//loop through each car on the road to update it
		for (int i = 0; i < cars.size(); i++) {
			Car car = cars.get(i);
			if (!car.isAIControlled()) {
				removeCar(car);
				i--;
				continue;
			}
			int goalIndex = goals.get(i);
			if (goalIndex < this.points.size()) {
				Vector2 goal = this.points.get(goalIndex);
				//set the cars trajectory to be towards the goal
				float angleToGoal = (float)MathUtils.getAngle(car.center(), goal);
				car.setRotation(angleToGoal);
				float distanceToGoal = MathUtils.distance(car.center(), goal); 
				float currentSpeed = car.getVelocity().getLength();
				boolean accelerate = true;
				if (this.hasStopSign() && goalIndex == this.points.size()-1) {
					//if we are approaching the last point
					if (distanceToGoal < speedLimit * 1) {
						if (car.getVelocity().getLength() > 0.75)
							car.brake(dt);
						accelerate = false;
						if (car.getVelocity().getLength() < 0.75)
							accelerate = true;
					}
				}
				if (accelerate) {
					//if that currentSpeed is below our speed limit we want to accelerate the car
					if (currentSpeed < this.speedLimit) {
						car.accelerate(dt);
					} else if (currentSpeed > this.speedLimit) { //slow down if we are above the speed limit (we are law abiding citizens here)
						car.accelerate(-dt);
					}
				}
				//if the car has reached the goal within some threshold, then move on to the next
				
				if (distanceToGoal < 0.15) {
					goals.set(i,goals.get(i)+1);
				}
			}
			goalIndex = goals.get(i);
			if (goalIndex >= this.points.size()) {
				if (this.hasStopSign()) {
					car.brake(dt * 3);
					if (!this.isCarAtStopSign)
						this.carWaitTimer = 0f;
					this.isCarAtStopSign = true;
					if (this.carWaitTimer > 2f) {
						Road other = this.linkedRoads.get(MathUtils.random(this.linkedRoads.size()));
						other.addCar(car);
						removeCar(car);
						this.isCarAtStopSign = false;
					}
				} else {
					//we finished the road so determine the next action for the car
					if (this.linkedRoads.size() > 0) {
						//choose a road at random for it to go to
						Road other = this.linkedRoads.get(MathUtils.random(this.linkedRoads.size()));
						other.addCar(car);
					} else {
						car.destroy();
						i--; //do this because removing a car will adjust the cars list size	
					}
					removeCar(car);	
				}
			}
		}
		if (this.isCarAtStopSign) {
			this.carWaitTimer += dt;
		}
	}
	
	public void addCar(Car car) {
		if (car.getRegion() != this.region)
			this.region.add(car);
		cars.add(car);
		goals.add(0);
	}
	
	private void removeCar(Car car) {
		int index = cars.indexOf(car);
		cars.remove(index);
		goals.remove(index);
	}
	
	public void setSpeedLimit(float speedLimit) {
		this.speedLimit = speedLimit;
	}
	
	public boolean hasStopSign() {
		return (this.intersectionRoads.size() > 0);
	}
	
	private void resetWait() {
		if (carRate == 0) {
			wait = MathUtils.INFINITY;
			return;
		}
		
		wait = MIN_WAIT + (60-MIN_WAIT) / carRate + MathUtils.random(-4,4);
		wait = MathUtils.max(MIN_WAIT,wait);
	}
	
	public float carRate() {
		return this.carRate;
	}
}
