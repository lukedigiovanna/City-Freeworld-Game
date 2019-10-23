package main;

import javax.swing.JFrame;

public class Main {
	public static void main(String[] args) {
		new Frame(Program.GAME_NAME+" | "+Program.getVersionString(),new Panel());
	}
}
