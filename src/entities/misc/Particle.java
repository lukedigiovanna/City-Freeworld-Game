package entities.misc;

import java.awt.*;
import java.awt.image.*;
import java.util.List;

import entities.Entity;
import misc.ImageTools;
import misc.MathUtils;
import misc.Vector2;
import world.*;
import world.event.CollisionEvent;
import world.regions.Region;

public class Particle extends Entity {
	private Type type;
	
	private float fadeTime = 0.5f;
	
	private transient BufferedImage image;
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
		
		if (image != null && color != null)
			image = ImageTools.colorscale(image, color);
		
		heat = MathUtils.clip(0.0f, 1.0f, heat); //clips the heat to be within 0 and 1
		float speed = heat * maxHeat;
		float theta = MathUtils.random((float)Math.PI*2);
		
		setVelocity(new Vector2((float)Math.cos(theta)*speed,(float)Math.sin(theta)*speed));
		
		//setProperty(Properties.KEY_HAS_COLLISION, Properties.VALUE_HAS_COLLISION_FALSE);
		setProperty(Properties.KEY_REGENERATE_HITBOX, Properties.VALUE_REGENERATE_HITBOX_FALSE);
		setProperty(Properties.KEY_INVULNERABLE, Properties.VALUE_INVULNERABLE_TRUE);
		setProperty(Properties.KEY_HAS_COLLISION, Properties.VALUE_HAS_COLLISION_FALSE);
		
		this.addCollisionEvent(CollisionEvent.BOUNCE);
		this.removeCollisionEvent(CollisionEvent.STOP);
		
		this.setVerticalHeight(10.0f);
	}
	
	public static enum Type {
		BALL(0.3f,0.3f),
		SPARKLES(0.3125f,0.3125f,2.5f,"sparkles"),
		TIRE_MARK(0.05f,0.05f,Color.BLACK),
		GUNFIRE(0.15f,0.15f,0.05f,"gunfire"),
		TEXT(0.0f,0.0f,1.5f),
		CORPSE(0.0f,0.0f,60.0f); //this is a place holder for the TextParticles
		
		float width, height;
		transient List<BufferedImage> images;
		Color defaultColor = Color.WHITE;
		float lifeSpan = 5.0f;
		
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
		
		Type(float width, float height, float lifeSpan) {
			this(width,height);
			this.lifeSpan = lifeSpan;
		}
		
		Type(float width, float height, float lifeSpan, String fp) {
			this(width,height,fp);
			this.lifeSpan = lifeSpan;
		}
	}
	
	public static void add(Region region, Type type, Color color, int count, float heat, float x, float y, float width, float height) {
		for (int i = 0; i < count; i++)
			region.add(new Particle(type,color,heat,x+MathUtils.random(width),y+MathUtils.random(height)));
	}
	
	private int alpha = 255;
	
	@Override
	public void draw(Camera c) {
		if (color != null)
			c.setColor(new Color(color.getRed(),color.getGreen(),color.getBlue(),alpha));
		switch (this.type) {
		case BALL:
			c.fillOval(getX(), getY(), getWidth(), getHeight());
			break;
		case SPARKLES:
			image = ImageTools.setTransparency(image, alpha);
			c.drawImage(image, getX(), getY(), getWidth(), getHeight());		
			break;
		case TIRE_MARK:
			c.fillRect(getX(), getY(), getWidth(), getHeight());
			break;
		case GUNFIRE:
			c.drawImage(image, getX(), getY(), getWidth(), getHeight());
			break;
		default:
			break;
		}
	}

	@Override
	public void update(float dt) {
		float timeUntilDeath = this.type.lifeSpan - this.getAge();
		
		if (timeUntilDeath <= this.fadeTime) {
			float percent = timeUntilDeath / this.fadeTime;
			alpha = MathUtils.clip(0, 255, (int)(percent * 255));
		}
		
		switch (type) {
		case BALL:
			this.getVelocity().y = -1;
			break;
		case SPARKLES:
			this.getVelocity().r = 3.14f;
			break;
		case TEXT:
			this.getVelocity().y += dt * 4;
		default:
			break;
		}
		
		if (timeUntilDeath <= 0)
			this.destroy();
	}
	
	public Color getColor() {
		Color c = new Color(color.getRed(),color.getGreen(),color.getBlue(),alpha);
		return c;
	}
	
	public int getAlpha() {
		return this.alpha;
	}
}
