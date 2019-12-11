package misc;

import java.awt.image.BufferedImage;

import display.Animation;
import json.JSONFile;

/*
 * Contains the image of the sheet and a JSON which gives each animation
 */
public class SpriteSheet {
	private BufferedImage sheet;
	private JSONFile json;
	private String name;
	private String folderPath;
	
	/**
	 * Generates a sprite sheet with a file path to the folder in which to make the sheet
	 * @param folderPath The folder that the sprite sheet is in
	 * @param name The name of the sprite sheet
	 */
	public SpriteSheet(String folderPath, String name) {
		this.name = name;
		this.folderPath = folderPath;
	}
	
	public SpriteSheet(BufferedImage sheet, String jsonPath) {
		
	}
	
	public SpriteSheet(BufferedImage sheet, JSONFile json) {
		
	}
	
	public void add(Animation ani) {
		//need to put each frame into the sprite sheet
		//first extend the size of the sprite sheet
	}
}
