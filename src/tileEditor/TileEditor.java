package tileEditor;

import javax.imageio.ImageIO;
import javax.swing.*;

import json.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;

import main.*;
import misc.MathUtils;
import misc.Rectangle;

/**
 * 
 */
public class TileEditor extends JPanel  {
	public final static int DISPLAY_WIDTH = 600, DISPLAY_HEIGHT = 500;
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Tile Editor");
		frame.setSize(DISPLAY_WIDTH,DISPLAY_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new TileEditor());
		frame.requestFocus();
		frame.setVisible(true);
	}
	
	private Mouse mouse;
	private Keyboard keyboard;
	private BufferedImage screen;
	
	/*
	 *	a+ssume the screen coordinate grid is structured as so:
	 *	0, 0 in the top left
	 *  1, 1 in the bottom right. positions represent a percentage across the display width and height 
	 */
	
	private Rectangle tileView = new Rectangle(0.8f,0.0f,0.2f,1.0f);
	
	//file stuff
	private static final int TILE_N = 256;
	private static final int TILE_SIZE = 8;
	private static final String TILE_PATH = "assets/images/tiles/";
	private String tilePack = "default";
	private JSONFile tilesJson;
	private BufferedImage spriteSheet;
	
	public TileEditor() {
		this.setFocusable(true);
		
		screen = new BufferedImage(DISPLAY_WIDTH,DISPLAY_HEIGHT,BufferedImage.TYPE_INT_ARGB);
		mouse = new Mouse(this,DISPLAY_WIDTH,DISPLAY_HEIGHT);
		keyboard = new Keyboard(this);
		
		tilesJson = new JSONFile(TILE_PATH+tilePack+"/tiles.json");
		tilesJson.clear();
		//lets add in 256 tiles
		JSONArray arr = new JSONArray("");
		for (int id = 0; id < 256; id++) {
			JSONObject frame = new JSONObject("");
			frame.set("id", id);
			frame.set("stringId", "null");
			JSONObject position = new JSONObject("");
			position.set("x", 0);
			position.set("y", 0);
			position.set("w", 0);
			position.set("h", 0);
			frame.set("position", position);
			arr.add(frame);
		}
		tilesJson.set("frames", arr);
		tilesJson.set("sprite_path", TILE_PATH+tilePack+"/sheet.png");
	
		spriteSheet = new BufferedImage(TILE_SIZE*TILE_N,TILE_SIZE*TILE_N,BufferedImage.TYPE_INT_ARGB);
		
		for (int x = 0; x < TILE_N; x++) {
			for (int y = 0; y < TILE_N; y++) {
				int rgb = MathUtils.random(0xFFFFFF); 
				Color c = new Color(rgb);
				System.out.println(c);
				for (int ix = 0; ix < TILE_SIZE; ix++) 
					for (int iy = 0; iy < TILE_SIZE; iy++)
						spriteSheet.setRGB(x*TILE_SIZE+ix, y*TILE_SIZE+iy, rgb);
			}
		}
		
		System.out.println("sprite sheet size: "+spriteSheet.getWidth()+" x "+spriteSheet.getHeight());
//		for (int x = 0; x < spriteSheet.getWidth(); x++)
//			for (int y = 0; y < spriteSheet.getHeight(); y++)
//				System.out.println("at pxl "+x+", "+y+": "+spriteSheet.getRGB(x, y));
		
		save();
		
		Thread repaintThread = new Thread(new Runnable() {
			public void run() {
				while (true) { //while the program is running
					try {
						Thread.sleep(50);
					} catch (Exception e) {}
					update();
					repaint();
					save();
				}
			}
		});
		repaintThread.start();
	}
	
	public void update() {
		Graphics2D g = screen.createGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT);
		g.setColor(Color.WHITE);
		//g.drawImage(spriteSheet, 0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT, null);
		fillRect(g,tileView);
	}
	
	private void fillRect(Graphics2D g, Rectangle rect) {
		fillRect(g,rect.getX(),rect.getY(),rect.getWidth(),rect.getHeight());
	}
	
	private void fillRect(Graphics2D g, float x, float y, float w, float h) {
		g.fillRect((int)(x*DISPLAY_WIDTH), (int)(y*DISPLAY_HEIGHT), (int)(w*DISPLAY_WIDTH), (int)(h*DISPLAY_HEIGHT));
	}
	
	public void paintComponent(Graphics g) {
		//draw the screen image
		g.drawImage(screen, 0, 0, getWidth(), getHeight(), null);
	}
	
	public void save() {
		tilesJson.save();
		try {
			File outFile = new File(TILE_PATH+tilePack+"/shee.jpg");
			ImageIO.write(spriteSheet, "jpg", outFile);
		} catch (IOException e) { e.printStackTrace(); }
	}
	
}
