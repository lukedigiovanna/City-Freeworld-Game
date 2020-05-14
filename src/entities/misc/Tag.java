package entities.misc;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

import entities.Entity;
import main.Program;
import misc.ImageTools;
import world.Camera;
import world.Properties;

public class Tag extends Entity {
	private String text;
	private Color color, borderColor;
	private BufferedImage icon;
	
	private static final BufferedImage marker = ImageTools.getImage("tag_marker.png");
	
	public Tag(String text, float x, float y) {
		this(null, text,Color.WHITE,x,y);
	}
	
	public Tag(BufferedImage icon, String text, float x, float y) {
		this(icon, text,Color.WHITE,x,y);
	}
	
	public Tag(BufferedImage icon, String text, Color color, float x, float y) {
		this(icon,text,Color.WHITE,null,x,y);
	}
	
	public Tag(String text, Color color, Color borderColor, float x, float y) {
		this(null,text,color,borderColor,x,y);
	}
	
	public Tag(BufferedImage icon, String text, Color color, Color borderColor, float x, float y) {
		super(x,y,.5f,.5f);
		this.text = text;
		this.color = color;
		this.borderColor = borderColor;
		this.icon = icon;
		
		this.setVerticalHeight(99f); //we want this to be above most things
		
		//properties
		this.setProperty(Properties.KEY_HAS_COLLISION, Properties.VALUE_HAS_COLLISION_FALSE);
		this.setProperty(Properties.KEY_INVULNERABLE, Properties.VALUE_INVULNERABLE_TRUE);
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public void setBorderColor(Color color) {
		this.borderColor = color;
	}
	
	public void setText(String text) {
		this.setText(text);
	}
	
	public void update(float dt) {
		
	}
	
	public void draw(Camera c) {
		c.setFont(Program.FONT_FAMILY, Font.BOLD, 0.5f);
		c.setColor(this.borderColor);
		float d = 0.03f;
		c.drawStringCentered(this.text, this.getX()-d, this.getY()-d);
		c.drawStringCentered(this.text, this.getX()+d, this.getY()-d);
		c.drawStringCentered(this.text, this.getX()-d, this.getY()+d);
		c.drawStringCentered(this.text, this.getX()+d, this.getY()+d);
		c.setColor(this.color);
		c.drawStringCentered(this.text, this.getX(), this.getY());
		if (this.icon != null) {
			c.drawImage(icon, this.getX() - c.stringWidth(this.text)/2-0.5f, this.getY(), 0.5f, 0.5f);
		}
		//draw the marker
		c.drawImage(marker, this.getX()-0.2f, this.getY()+0.1f, 0.4f, 0.4f);
	}
}
