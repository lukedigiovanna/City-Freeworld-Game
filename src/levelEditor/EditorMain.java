package levelEditor;

import javax.swing.*;

import display.textures.TexturePack;
import main.Program;

/**
 * Holds the main function for running the Level Editor
 */
public class EditorMain {
	
	private static JFrame frame;
	private static EditorPanel panel;
	
	public static void main(String[] args) {
		TexturePack.initialize();
		frame = new JFrame("Level Editor");
		frame.setSize(Program.SCREEN_WIDTH/2,(int)(Program.SCREEN_HEIGHT*0.6));
		frame.setLocation(Program.SCREEN_WIDTH/2-frame.getWidth()/2, Program.SCREEN_HEIGHT/2-frame.getHeight()/2);
		frame.requestFocus();
		panel = new EditorPanel();
		frame.setContentPane(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
