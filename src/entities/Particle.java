package entities;

import java.awt.*;
import java.awt.image.*;
import java.util.List;

import misc.ImageTools;
import misc.MathUtils;
import misc.Vector2;
import world.*;

public class Particle extends Entity {
	private Type type;
	
	private float lifeSpan = 2.5f;
	
	private BufferedImage image;
	private Color color;
	
	public Particle(Type type, float x, float y) {
		this(type,type.defaultColor,x,y);
	}
	
	public Particle(Type type, Color color, float x, float y) {
		this(type,color,0.0f,x,y);
	}
	
	private static float maxHeat = 4.0f;
	
	public Particle(Type type, Color color, float heat, float x, float y) {
		super(x-type.width/2, y-type.height/2, type.width, type.height);
		this.type = type;
		
		this.color = color;
		
		if (type.images != null && type.images.size() > 0)
			image = type.images.get(MathUtils.random(type.images.size()));
		
		if (image != null)
			image = ImageTools.colorscale(image, color);
		
		heat = MathUtils.clip(0.0f, 1.0f, heat); //clips the heat to be within 0 and 1
		float theta = MathUtils.random((float)Math.PI*2);
		float speed = heat * maxHeat;
		this.velocity = new Vector2((float)Math.cos(theta)*speed,(float)Math.sin(theta)*speed);
		
		properties.set(Properties.KEY_HAS_COLLISION, Properties.VALUE_HAS_COLLISION_TRUE);
		properties.set(Properties.KEY_REGENERATE_HITBOX, Properties.VALUE_REGENERATE_HITBOX_FALSE);
	}
	
	public static enum Type {
		BALL(0.3f,0.3f),
		GENERIC(0.4f,0.4f,"generic"),
		TIRE_MARK(0.05f,0.05f,Color.BLACK);
		
		float width, height;
		List<BufferedImage> images;
		Color defaultColor = Color.WHITE;
		Type(float width, float height) {
			this.width = width;
			this.height = height;
		}
		
		Type(float width, float height, Color dc) {
			this(width,height);
			defaultColor = dc;
		}
		
		Type(float width, float height, String fp) {
			this(width,height);
			images = ImageTools.getImages("particle_sprites/"+fp, "");
		}
		
		Type(float width, float height, String fp, Color dc) {
			this(width,height,fp);
			defaultColor = dc;
		}
	}
	
	public static void add(Region region, Type type, Color color, int count, float heat, float x, float y, float width, float height) {
		for (int i = 0; i < count; i++)
			region.add(new Particle(type,color,heat,x+MathUtils.random(width),y+MathUtils.random(height)));
	}
	
	@Override
	public void draw(Camera c) {
		c.setColor(color);
		switch (this.type) {
		case BALL:
			c.fillOval(getX(), getY(), getWidth(), getHeight());
			break;
		case GENERIC:
			c.drawImage(image, getX(), getY(), getWidth(), getHeight());
			break;
		case TIRE_MARK:
			c.fillRect(getX(), getY(), getWidth(), getHeight());
			break;
		}
	}

	@Override
	public void update(float dt) {
		switch (type) {
		case BALL:
			velocity.y = -1;
			break;
		case GENERIC:
			velocity.r = 3.14f;
		}
		if (this.age >= lifeSpan)
			this.destroy();
	}
}
