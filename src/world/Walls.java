package world;

import java.util.List;
import java.util.*;
import java.awt.*;

import misc.Line;

/**
 * To do:
 * 
 * * make region-component type thing class
 * * add this wall thing to the world test thing you know
 * 
 * @author 814002
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
	
	public void draw(Camera c) {
		c.setColor(Color.YELLOW);
		for (Line l : walls) 			
			c.drawLine(0.025f,l);
		System.out.println("ok");
	}
}
