package levelEditor.editorComponents;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import misc.Vector2;

public class EditorRoad implements EditorComponent {
	int id; //identification of the road for linking purposes
	List<Vector2> points; //direction is classified by the order of points
	List<Integer> linkedRoads; //links to the ID of the other roads that this is linked to
	List<Integer> intersectionRoads; //links to the ID of other roads that cross the intersection of this road
	private float carRate; //how many cars spawn, on average, per minute
	private float speedLimit; //the maximum speed cars can travel on this road, in m/s
	
	public EditorRoad(int id) {
		this();
		this.id = id;
	}
	
	public EditorRoad() {
		this.points = new ArrayList<Vector2>();
		this.linkedRoads = new ArrayList<Integer>();
		this.intersectionRoads = new ArrayList<Integer>();
		this.carRate = 4; //default
		this.speedLimit = 2; //default
	}
	
	@Override
	public void write(DataOutputStream out) throws IOException {
		//first write the id
		out.write(id);
		//now write the points of this road
		out.write(points.size());
		for (Vector2 point : points) {
			out.write((int)point.x);
			out.write((int)((point.x%1.0f)*255));
			out.write((int)point.y);
			out.write((int)((point.y%1.0f)*255));
		}
		//now write the linked roads
		out.write(linkedRoads.size());
		for (Integer i : linkedRoads) 
			out.write(i);
		out.write(intersectionRoads.size());
		for (Integer i : intersectionRoads)
			out.write(i);
		//now write the attributes
		out.write((int)carRate);
		out.write((int)((carRate%1.0f)*255));
		out.write((int)speedLimit);
		out.write((int)((speedLimit%1.0f)*255));
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.id = in.read();
		int numOfPoints = in.read();
		for (int i = 0; i < numOfPoints; i++) {
			Vector2 point = new Vector2(0,0);
			point.x = in.read() + in.read()/256.0f;
			point.y = in.read() + in.read()/256.0f;
			this.points.add(point);
		}
		int numOfLinkedRoads = in.read();
		for (int i = 0; i < numOfLinkedRoads; i++) {
			this.linkedRoads.add(in.read());
		}
		int numOfIntersectionRoads = in.read();
		for (int i = 0; i < numOfIntersectionRoads; i++) {
			this.intersectionRoads.add(in.read());
		}
		this.carRate = in.read() + in.read()/256.0f;
		this.speedLimit = in.read() + in.read()/256.0f;
	}

	@Override
	public void translate(int dx, int dy) {
		for (Vector2 point : this.points) {
			point.x+=dx;
			point.y+=dy;
		}
	}
	
	public Vector2 getLastPoint() {
		return this.points.size() == 0 ? null : this.points.get(this.points.size()-1);
	}
	
	public Vector2 getFirstPoint() {
		return this.points.size() == 0 ? null : this.points.get(0);
	}
	
	public void add(Vector2 point) {
		this.points.add(point);
	}
	
	public void link(EditorRoad road) {
		this.linkedRoads.add(road.id);
	}
	
	public List<Integer> getLinkedIDs() {
		return this.linkedRoads;
	}
	
	public void intersect(EditorRoad road) {
		this.intersectionRoads.add(road.id);
		System.out.println(this.id+" to "+road.id);
	}
	
	public List<Integer> getIntersectionIDs() {
		return this.intersectionRoads;
	}
	
	public int getID() {
		return this.id;
	}
	
	public void setCarRate(float val) {
		this.carRate = val;
	}
	
	public void setSpeedLimit(float val) {
		this.speedLimit = val;
	}
	
	public float getCarRate() {
		return this.carRate;
	}
	
	public float getSpeedLimit() {
		return this.speedLimit;
	}
	
	public List<Vector2> getPoints() {
		return this.points;
	}
	
	public String getString() {
		return "road";
	}
	
}
