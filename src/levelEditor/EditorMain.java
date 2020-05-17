package levelEditor;

import javax.swing.*;

import display.textures.TexturePack;
import main.Program;

/**
 * Holds the main function for running the Level Editor
 */
public class EditorMain {
	
	/*
	 * These methods open up a frame that contains the 
	 * EditorPanel for specified region
	 */
	
	/**
	 * Loads up a current region number for the world
	 * @param world
	 * @param regionNumber
	 */
	public static void launch(EditorWorld world, int regionNumber) {
		EditorPanel panel = new EditorPanel(world,regionNumber);
		createFrame("EDITOR: "+world.getName()+" reg#: "+regionNumber,panel);
	}
	
	/**
	 * Creates a new region for the world
	 * @param world
	 */
	public static void launch(EditorWorld world) {
		EditorPanel panel = new EditorPanel(world,-1);
		createFrame("EDITOR: "+world.getName()+" reg#: "+world.getNumberOfRegions(),panel);
	}
	
	private static void createFrame(String name, JPanel panel) {
		JFrame frame = new JFrame(name);
		frame.setSize(Program.SCREEN_WIDTH/2,(int)(Program.SCREEN_HEIGHT*0.6));
		frame.setLocation(Program.SCREEN_WIDTH/2-frame.getWidth()/2, Program.SCREEN_HEIGHT/2-frame.getHeight()/2);
		frame.requestFocus();
		frame.setContentPane(panel);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}
}
