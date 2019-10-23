package main;

import javax.swing.*;

public class Frame {
	private JFrame frame;
	
	public Frame(String name, JPanel panel) {
		frame = new JFrame(name);
		frame.setSize(Program.SCREEN_WIDTH/2,Program.SCREEN_HEIGHT/2);
		this.center();
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
