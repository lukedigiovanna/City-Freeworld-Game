package main;

import javax.swing.*;

public class Frame {
	private JFrame frame;
	
	public Frame(String name, JPanel panel) {
		frame = new JFrame(name);
		double defaultRatio = 4.0/3; //W:H
		int width = (int)(Program.SCREEN_WIDTH * 0.45);
		int height = (int)(width/defaultRatio);
		frame.setSize(width,height);
		frame.setLocation(20,20);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(panel);
		frame.requestFocus();
		frame.setVisible(true);
	}
	
	public void setPanel(JPanel panel) {
		frame.setContentPane(panel);
	}
	
	public int getWidth() {
		return frame.getWidth();
	}
	
	public int getHeight() {
		return frame.getHeight();
	}
	
	public void center() {
		frame.setLocation(Program.SCREEN_WIDTH/2-getWidth()/2,Program.SCREEN_HEIGHT/2-getHeight()/2);
	}
}
