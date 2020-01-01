package entities.misc;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import main.Program;
import misc.MathUtils;
import world.Camera;

public class TextParticle extends Particle {
	
	private float size;
	private String text;
	
	public TextParticle(String text, Color color, float x, float y, float size) {
		super(Particle.Type.TEXT,color,x,y);	
		this.size = size;
		this.text = text;
		this.getVelocity().y = -2.0f;
		this.getVelocity().x = MathUtils.random(-0.9f, 0.9f);
	}
	
	@Override
	public void draw(Camera c) { 
		c.setFont(Program.FONT_FAMILY,Font.BOLD,size);
		c.setColor(getColor());
		c.drawString(text, getX(), getY());
	}
	
}
