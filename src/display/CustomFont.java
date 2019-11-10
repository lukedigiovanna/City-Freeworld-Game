package display;

import java.awt.Color;
import java.awt.image.BufferedImage;

import misc.ImageTools;

public class CustomFont {
	public static final CustomFont 
				HANDDRAWN = new CustomFont("handfont",16,"ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.!?,"),
				SMALL_PIXEL = new CustomFont("smallpixel",6,CustomFont.ALPHABET.toUpperCase()+CustomFont.ALPHABET+"0123456789"),
				PIXEL = new CustomFont("pixelalphabet",5,7,CustomFont.ALPHABET.toUpperCase());
	
	public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
	
	private BufferedImage[] characters;
	private String order;
	
	/**
	 * Creates a custom font
	 * @param ss the sprite sheet
	 * @param width the width of each character
	 * @param height the height of each cahracter
	 * @param order the order of characters
	 */
	public CustomFont(BufferedImage ss, int width, int height, String order) {
		int charWidth = ss.getWidth()/width;
		int charHeight = ss.getHeight()/height;
		this.order = order;
		int index = 0;
//		for (int x = 0; x < ss.getWidth(); x++) 
//			for (int y = 0; y < ss.getHeight(); y++)
//				if ((new java.awt.Color(ss.getRGB(x, y))).getAlpha() == 255) {
//					ss.setRGB(x, y, Color.WHITE.getRGB());
//				}
		characters = new BufferedImage[order.length()];
		for (int y = 0; y < charHeight; y++) {
			for (int x = 0; x < charWidth; x++) {
				characters[index] = ss.getSubimage(x*width, y*height, width, height);
				index++;
				if (index > order.length()-1)
					break;
			}
		}
	}
	
	public CustomFont(String ssLink, int width, int height, String order) {
		this(ImageTools.getBufferedImage("assets/fonts/"+ssLink+".png"),width,height,order);
	}
	
	public CustomFont(String ssLink, int size, String order) {
		this(ssLink,size,size,order);
	}
	
	public BufferedImage getChar(char c) {
		if (c == ' ')
			return ImageTools.BLANK;
		
		int index = order.indexOf(c);
		if (index > -1) {
			return characters[index];
		}
		
		//try changing it to uppercase?
		c = Character.toUpperCase(c);
		index = order.indexOf(c);
		if (index > -1) {
			return characters[index];
		}
		
		//if that didn't work try changing to lowercase
		c = Character.toLowerCase(c);
		if (index > -1) {
			return characters[index];
		}
		
		//if none of that worked then the character is not in the font..
		return ImageTools.IMAGE_NOT_FOUND; //use the not found
	}
}
