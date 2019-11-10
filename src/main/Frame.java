package main;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

public class Frame extends JFrame {
	public Frame(String name, JPanel panel) {
		super(name);
		//add something that asks if you actually want to close the frame on close
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (Frame.this.getDefaultCloseOperation() == JFrame.EXIT_ON_CLOSE)
					Program.exit();
				int exit = JOptionPane.showConfirmDialog(Frame.this, "Are you sure you want to exit?\nYou may lose unsaved progress",
						"Exit?", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
				if (exit == JOptionPane.YES_OPTION)
					Program.exit();
			}
		});
		
		double defaultRatio = 4.0/3; //W:H
		int width = (int)(Program.SCREEN_WIDTH * 0.45);
		int height = (int)(width/defaultRatio);
		this.setSize(width,height);
		this.setLocation(20,20);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setContentPane(panel);
		this.requestFocus();
		this.setVisible(true);
	}
	
	public void setPanel(JPanel panel) {
		this.setContentPane(panel);
	}
	
	public void center() {
		this.setLocation(Program.SCREEN_WIDTH/2-getWidth()/2,Program.SCREEN_HEIGHT/2-getHeight()/2);
	}
}
