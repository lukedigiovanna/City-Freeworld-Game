package display;

import java.awt.image.BufferedImage;

import misc.ImageTools;

public class CustomFont {
	public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
	
	private BufferedImage[] characters;
	private String order;
	
	/**
	 * Creates a custom font
	 * @param ss the sprite sheet
	 * @param size the size of each cell for each letter on the sheet
	 * @param order the order of characters
	 */
	public CustomFont(BufferedImage ss, int size, String order) {
		int width = ss.getWidth()/size;
		int height = ss.getHeight()/size;
		this.order = order;
		int index = 0;
		characters = new BufferedImage[order.length()];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				characters[index] = ss.getSubimage(x*size, y*size, size, size);
				index++;
				if (index > order.length()-1)
					break;
			}
		}
	}
	
	public CustomFont(String ssLink, int size, String order) {
		this(ImageTools.getBufferedImage("assets/fonts/"+ssLink+".png"),size,order);
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
