package levelEditor;

import javax.swing.*;

import main.Mouse;
import main.Program;
import misc.Color8;

import java.awt.*;
import java.awt.image.BufferedImage;

public class EditorPanel extends JPanel {
	
	private JMenuBar mb;
	private JMenu fileMenu;
	
	public EditorPanel() {
		mb = new JMenuBar();
		fileMenu = new JMenu("File");
		JMenuItem fm1 = new JMenuItem("New World");
		fileMenu.add(fm1);
		mb.add(fileMenu);
		add(mb);
	}
}
