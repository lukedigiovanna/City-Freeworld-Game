package levelEditor;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.ArrayList;

import javax.swing.*;

import main.Program;

public class EditorLauncherPanel {
	private JFrame frame;
	private JTextField worldTextBox;
	private JPanel regionListPanel;
	private EditorWorld currentWorld;
	
	public EditorLauncherPanel(JFrame frame) {
		frame.setLayout(new FlowLayout());
		Font font = new Font(Program.FONT_FAMILY,Font.BOLD,14);
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2,2,10,10));
		JLabel worldLabel = new JLabel("World Name: ");
		worldLabel.setFont(font);
		panel.add(worldLabel);
		worldTextBox = new JTextField();
		worldTextBox.setFont(font);
		panel.add(worldTextBox);
		JButton submitButton = new JButton("Submit");
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//remove the current one..
				if (regionListPanel != null)
					frame.remove(regionListPanel);
				String worldName = worldTextBox.getText();
				currentWorld = new EditorWorld(worldName);
				int numOfRegions = currentWorld.getNumberOfRegions();
				regionListPanel = new JPanel();
				regionListPanel.setLayout(new GridLayout(numOfRegions+1,2,10,10));
				
				JLabel newRegionLabel = new JLabel("New Region");
				newRegionLabel.setFont(font);
				regionListPanel.add(newRegionLabel);
				JButton newRegionButton = new JButton("Create");
				newRegionButton.setFont(font);
				newRegionButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						EditorMain.launch(currentWorld);
					}
				});
				regionListPanel.add(newRegionButton);
				for (int i = 0; i < numOfRegions; i++) {
					JLabel regNum = new JLabel(worldName+"."+i);
					regNum.setFont(font);
					regionListPanel.add(regNum);
					JButton launchButton = new JButton("Open");
					launchButton.setFont(font);
					launchButton.addActionListener(new ActionListenerHelper(i) {
						public void actionPerformed(ActionEvent e) {
							EditorMain.launch(currentWorld, this.regionNumber);
						}
					});
					regionListPanel.add(launchButton);
				}
				frame.add(regionListPanel);
				frame.pack();
			}
		});
		panel.add(submitButton);
		
		frame.add(panel);
		
		frame.pack();
	}
	
	private static abstract class ActionListenerHelper implements ActionListener {
		int regionNumber;
		public ActionListenerHelper(int regionNumber) {
			this.regionNumber = regionNumber;
		}
	}
}
