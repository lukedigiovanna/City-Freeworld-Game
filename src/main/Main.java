package main;

public class Main {
	public static void main(String[] args) {
		Program.init();
		
		misc.Line l = new misc.Line(new misc.Vector2(0, 0),new misc.Vector2(-4,0));
		System.out.println(l.angle());
		
	}
}
