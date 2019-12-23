package levelEditor;

import javax.swing.*;

import main.Mouse;
import main.Program;
import misc.Color8;

import java.awt.*;
import java.awt.image.BufferedImage;

public class EditorPanel extends JPanel {
	
	private BufferedImage screen;
	
	public EditorPanel() {
		screen = new BufferedImage(Program.DISPLAY_WIDTH,Program.DISPLAY_HEIGHT,BufferedImage.TYPE_INT_ARGB);
	}
	
	public void paintComponent(Graphics g) {
		
	}
}
