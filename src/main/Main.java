package main;

import misc.*;

public class Main {
	public static void main(String[] args) {
		Program.init();
		
		Line l = new Line(new Vector2(2,2),new Vector2(3,4));
		System.out.println(l.distance(new Vector2(4,6)));
	}
}
