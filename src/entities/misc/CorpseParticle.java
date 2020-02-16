package entities.misc;

import java.awt.image.BufferedImage;

import display.Animation;
import entities.Human;
import misc.ImageTools;
import world.Camera;

public class CorpseParticle extends Particle {
	
	private transient Animation ani;
	
	private Human human;
	
	public CorpseParticle(Animation ani, float x, float y) {
		super(Particle.Type.CORPSE,x,y);
		this.ani = ani;
		this.setDimension(14f/16f*0.8f,24f/16f*0.8f);
		this.setHitboxToDimension();
		this.setVerticalHeight(0.0f);
	}
	
	public void loadAssets() {
		this.ani = human.getCorpseAnimation();
	}
	
	@Override
	public void draw(Camera c) {
		BufferedImage img = ImageTools.setTransparency(ani.getCurrentFrame(),this.getAlpha());
		c.drawImage(img,getX(),getY(),getWidth(),getHeight());
	}
	
	public void update(float dt) {
		super.update(dt);
		ani.animate(dt);
	}
}
