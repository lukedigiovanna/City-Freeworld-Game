package world.regions;

import java.util.List;
import java.util.*;
import java.awt.*;

import misc.Line;
import world.Camera;

/**
 * To do:
 * 
 * * make region-component type thing class
 * * add this wall thing to the world test thing you know
 *
 */

public class Walls {
	private List<Line> walls;
	
	public Walls() {
		walls = new ArrayList<Line>();
	}
	
	public void add(Line wallLine) {
		walls.add(wallLine);
	}
	
	public List<Line> getWalls() {
		return walls;
	}
	
	public void draw(Camera c) {
		c.setColor(Color.YELLOW);
		c.setStrokeWidth(0.025f);
		for (Line l : walls) 			
			c.drawLine(l);
	}
}
