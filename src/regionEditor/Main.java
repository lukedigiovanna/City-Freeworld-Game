package regionEditor;

import javax.swing.JFrame;

import main.Program;

/*
 * class that contains the main method for launching the 
 * region editor program
 */
public class Main {
	public static void main(String[] args) {
		JFrame frame = new JFrame(Program.GAME_NAME+" "+Program.getVersionString()+" - REGION EDITOR "+Program.DEVELOPMENT_PERIOD);
		frame.setSize(640,480);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new EditorPanel());
		frame.requestFocus();
		frame.setVisible(true);
	}
}
