package main;

import misc.*;

public class Main {
	public static void main(String[] args) {
		Program.init();
		
		Line l = new Line(new Vector2(0,-5),new Vector2(0,5));
		Line l2 = new Line(new Vector2(-5,0), new Vector2(5,0));
		
		System.out.println(l.intersects(l2));
	}
}
