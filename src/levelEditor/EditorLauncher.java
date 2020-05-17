package levelEditor;

import java.awt.*;

import javax.swing.*;

import display.textures.TexturePack;

public class EditorLauncher {
	public static final int WIDTH = 640, HEIGHT = 540;
	
	private static EditorLauncherPanel panel;
	
	public static void main(String[] args) {
		TexturePack.initialize();
		JFrame frame = new JFrame("Editor Launcher");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH,HEIGHT);
		panel = new EditorLauncherPanel(frame);
		frame.setResizable(false);
		frame.setVisible(true);
	}
}
