package world;

import misc.ImageTools;
import misc.Line;
import misc.MathUtils;
import misc.Vector2;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

import entities.Entity;
import entities.EntityList;
import main.Program;

public class Camera {
	private static final int CELL_SIZE = 32;
	
	private Vector2 position;
	private float verticalHeight;
	private Vector2 dimension; //cell grid width and height
	
	private int pixelWidth, pixelHeight;
	
	private BufferedImage image;
	
	private Entity focus;	
	private Region region;
	
	//rendering hints for more high quality graphics
	private Map<RenderingHints.Key,Object> map = new HashMap<RenderingHints.Key,Object>();
	private RenderingHints rh;
	
	public Camera(Region region, float x, float y, float width, float height, int pixelWidth, int pixelHeight) {
		this.position = new Vector2(x,y,0);
		this.dimension = new Vector2(width,height);
		this.pixelWidth = pixelWidth;
		this.pixelHeight = pixelHeight;
		
		image = new BufferedImage(pixelWidth,pixelHeight,BufferedImage.TYPE_INT_ARGB);

		this.region = region;
		
		map.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		map.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		map.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		map.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
		map.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		rh = new RenderingHints(map);
	}
	
	public Camera(Region region, float x, float y, float width, float height) {
		this(region,x,y,width,height,(int)(width*CELL_SIZE),(int)(height*CELL_SIZE));
	}
	/**
	 * Generates a camera given a world position and world dimension
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public Camera(float x, float y, float width, float height) {
		this(null,x,y,width,height);
	}
	
	/**
	 * This one allows you to input the width / height in pixel format
	 * @param x left starting point for x
	 * @param y top starting point for y
	 * @param width pixel width of camera
	 * @param height pixel height of camera
	 */
	public Camera(Region region, float x, float y, int width, int height) {
		this(region, x, y, (float)width/CELL_SIZE, (float)height/CELL_SIZE);
	}
	
	public void linkToRegion(Region region) {
		this.region = region;
	}
	
	public void setFocus(Entity e) {
		this.focus = e;
	}
	
	/**
	 * The value for 1 world unit converted to pixels.
	 * @return
	 */
	public int getUnitSize() {
		return CELL_SIZE;
	}
	
	public float getX() {
		return position.x;
	}
	
	public float getY() {
		return position.y;
	}
	
	public float getWidth() {
		return dimension.x;
	}
	
	public float getHeight() {
		return dimension.y;
	}
	
	/**
	 * Moves the camera to adjust for centering on the focused entity
	 */
	private float margin = 0.2f;
	public void adjustPosition(float dt) {
		if (focus == null)
			return; //don't adjust camera pos if there is no focus
		float cx = position.x + dimension.x/2, cy = position.y + dimension.y/2;
		float mx = dimension.x*margin, my = dimension.y*margin;
		float fx = focus.centerX(), fy = focus.centerY();
		float dx = 0,dy = 0;
		float left = cx-mx, right = cx + mx, bottom = cy+my, top = cy-my;
		float shrinker = 0.5f;
		if (fx < left)
			dx = (fx-left)/shrinker;
		else if (fx > right)
			dx = (fx-right)/shrinker;
		if (fy < top)
			dy = (fy-top)/shrinker;
		else if (fy > bottom)
			dy = (fy-bottom)/shrinker;
		//ensure a minimum speed for the camera to move at
		float minValue = 0.5f;
		if (Math.abs(dx) < minValue)
			dx = MathUtils.sign(dx)*minValue;
		if (Math.abs(dy) < minValue)
			dy = MathUtils.sign(dy)*minValue;
		move(dx*dt,dy*dt);
	}
	
	/**
	 * Zooms the dimensions of the camera view by some factor
	 * Positive factor zooms out, negative factor zooms in.
	 * @param factor
	 */
	public void zoom(float factor) {
		float xPercent = (focus.getX()-this.getX())/this.getWidth(),
              yPercent = (focus.getY()-this.getY())/this.getHeight();
		dimension.x += dimension.x * factor;
		dimension.y += dimension.y * factor;
		this.position.x = focus.getX()-xPercent*dimension.x;
		this.position.y = focus.getY()-yPercent*dimension.y;
	}
	
	/**
	 * Getter for camera view
	 * @return the camera view
	 */
	public BufferedImage getView() {
		return image;
	}
	
	private Vector2 drawPos, drawDim;
	/**
	 * Redraws the camera view to a BufferedImage
	 * Only draws what is in view of the camera. Ignores other cells.
	 * @param world the world to draw. includes the grid and entities
	 */
	public void draw(){
		updateVectors();
		
		refreshGraphics();
		
		//clear the screen
		g.setColor(Color.WHITE); //dont do now because of dumbass glitch
		g.fillRect(0, 0, pixelWidth, pixelHeight);
		
		if (region == null)  {
			//draw something saying there is nothing to draw
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, pixelWidth, pixelHeight);
			g.setFont(new Font(Program.FONT_FAMILY,Font.BOLD,pixelHeight/10));
			String str = "No Region to Draw";
			g.setColor(Color.WHITE);
			g.drawString(str, pixelWidth/2-g.getFontMetrics().stringWidth(str)/2, pixelHeight/2);
			return;
		}
		
		//Get the new grid and entities
		CellGrid grid = region.getGrid();
		EntityList entities = region.getEntities();
		Walls walls = region.getWalls();
		
		//find the index on the grid where we need to start
		int startIndexX = (int)this.drawPos.getX(), startIndexY = (int)this.drawPos.getY();
		//how many cells we need to get in the x, y directions
		int gridWidth = (int)drawDim.x+2, gridHeight = (int)drawDim.y+2;
		
		//draw the grid
		for (int ix = startIndexX; ix < startIndexX+gridWidth; ix++) { 
			for (int iy = startIndexY; iy < startIndexY+gridHeight; iy++) {
				Cell cell = grid.get(ix, iy);
				if (cell != null) {
					setLightValue(cell.getLightValue());
					drawImage(cell.getImage(), cell.getX(), cell.getY(), 1.0f, 1.0f);
				}
			}
		}
		
		//draw the entities on top of the grid
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			float rotation = e.getRotation();
			float x = e.centerX(), y = e.centerY();
			rotate(rotation,x,y);
			setLightValue(e.getLightValue());
			e.draw(this);
			rotate(-rotation,x,y);
			
			//e.drawHitbox(this);
		}
		
		walls.draw(this);
	}
	
	/*
	 * Graphics functions so we can call position/dimension in world coordinates
	 * rather than in pixel coordinates
	 */
	
	private Graphics2D g;
	
	public void refreshGraphics() {
		//image = new BufferedImage(pixelWidth,pixelHeight,BufferedImage.TYPE_INT_ARGB);
		g = image.createGraphics();
		g.setRenderingHints(rh);
	}
	
	private void updateVectors() {
		this.drawPos = this.position.copy();
		this.drawDim = this.dimension.copy();
	}
	 
	/*
		GRAPHICS ENGINE COMPONENT OF CAMERA CLASS
	*/
	
	private float lightValue = 1.0f; //only modify this with setter
	private Color color = Color.WHITE; //default
	
	public void setLightValue(float value) {
		if (value == this.lightValue)
			return;
		this.lightValue = value;
		setLightnessColor();
	}
	
	public void setColor(Color color) {
		this.color = color;
		setLightnessColor();
	}
	
	private void setLightnessColor() {
		Color c = new Color((int)(lightValue * this.color.getRed()),(int)(lightValue * this.color.getGreen()),(int)(lightValue * this.color.getBlue()),this.color.getAlpha());
		g.setColor(c);
	}
	
	public void fillRect(float x, float y, float width, float height) {
		g.fillRect(toPX(x), toPY(y), toPW(width), toPH(height));
	}
	
	public void drawRect(float x, float y, float width, float height) {
		g.drawRect(toPX(x), toPY(y), toPW(width), toPH(height));
	}
	
	public void fillOval(float x, float y, float width, float height) {
		g.fillOval(toPX(x),toPY(y),toPW(width),toPH(height));
	}
	
	public void drawOval(float x, float y, float width, float height) {
		g.drawOval(toPX(x), toPY(y), toPW(width), toPH(height));
	}
	
	public void drawLine(float x1, float y1, float x2, float y2) {
		g.drawLine(toPX(x1), toPY(y1), toPX(x2), toPY(y2));
	}
	
	public void drawLine(Line l) {
		Vector2[] ep = l.getEndpoints();
		drawLine(ep[0].x,ep[0].y,ep[1].x,ep[1].y);
	}
	
	public void setStrokeWidth(float width) {
		g.setStroke(new BasicStroke(toPH(width)));
	}
	
	public void drawImage(BufferedImage image, float x, float y, float width, float height) {
		g.drawImage(ImageTools.darken(image,lightValue), toPX(x), toPY(y), toPW(width), toPH(height), null);
	}
	
	public void rotate(float theta, float rx, float ry) {
		g.rotate(theta,toPX(rx),toPY(ry));
	}
	
	private int toPX(float x) {
		float rel = x-this.drawPos.x;
		int sx = (int)(rel/drawDim.x * pixelWidth);
		return sx;
	}
	
	private int toPW(float width) {
		return (int)(width/drawDim.x * pixelWidth + 1);
	}
	
	private int toPY(float y) {
		float rel = y-this.drawPos.y;
		int sy = (int)(rel/drawDim.y * pixelHeight);
		return sy;
	}
	
	private int toPH(float height) {
		return (int)(height/drawDim.y * pixelHeight + 1);
	}

	public void move(float dx, float dy) {
		position.add(new Vector2(dx,dy));
		//now correct off the position
		if (region != null) {
			if (position.getX() < 0)
				position.setX(0);
			if (position.getX() > region.getWidth()-this.dimension.x)
				position.setX(region.getWidth()-this.dimension.x);
			if (position.getY() < 0)
				position.setY(0);
			if (position.getY() > region.getHeight()-this.dimension.y)
				position.setY(region.getHeight()-this.dimension.y);
		}
	}
	
	public void moveX(float dx) {
		move(dx,0);
	}
	
	public void moveY(float dy) {
		move(0,dy);
	}
}
