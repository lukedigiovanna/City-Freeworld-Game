package misc;

import java.awt.Color;

//represents an 8 bit color
public class Color8 extends Color {
	private static final long serialVersionUID = 1L;
	
	private int r, g, b;
	
	/**
	 * creates an 8 bit color
	 * @param r the red component of the color from 0 to 8
	 * @param g the green component of the color from 0 to 8
	 * @param b the blue component of the color from 0 to 4
	 */
	public Color8(int r, int g, int b) {
		this(r, g, b, 255);	
	}
	
	public Color8(int r, int g, int b, int a) {
		super((int)(r * (255/8.0)),(int)(g * (255/8.0)),(int)(b * (255/4.0)), a);
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public Color8 setAlpha(int alpha) {
		return new Color8(r, g, b, alpha);
	}
	
	public static Color8 to8Bit(Color c) {
		Color8 c8 = new Color8(c.getRed()/(255/8),c.getGreen()/(255/8),c.getBlue()/(255/4));
		c8 = c8.setAlpha(c.getAlpha());
		return c8;
	}
	
	public final static Color8 RED = new Color8(8,0,0),
							   ORANGE = new Color8(8,4,0),
							   YELLOW = new Color8(8,8,0),
							   GREEN = new Color8(0,8,0),
							   BLUE = new Color8(0,0,4),
							   MAGENTA = new Color8(8,0,4),
							   BLACK = new Color8(0,0,0),
							   WHITE = new Color8(8,8,4),
							   GRAY = new Color8(4,4,2),
							   LIGHT_GRAY = new Color8(6,6,3),
							   DARK_GRAY = new Color8(2,2,1),
							   CYAN = new Color8(0,8,4); 
}
