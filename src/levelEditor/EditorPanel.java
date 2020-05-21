package levelEditor;

import javax.swing.*;

import display.textures.TexturePack;
import display.textures.Texture;
import levelEditor.editorComponents.*;
import main.*;
import misc.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class EditorPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private BufferedImage screen;
	private List<Texture> tiles;
	private List<Texture> objects;
	private Mouse mouse;
	private Keyboard keyboard;
	
	private List<MenuButton> menuButtons;
	private List<ToolButton> toolButtons;
	
	private EditorRegion region;
	private Tool curTool = Tool.DRAW;
	private Tool prevTool = Tool.DRAW;
	
	private float timer = 0.0f;
	
	public EditorPanel(EditorWorld world, int number) {
		this.setFocusable(true);
		
		if (number < 0) {
			String dim = JOptionPane.showInputDialog(EditorPanel.this, "What dimensions?", "Create Region", JOptionPane.QUESTION_MESSAGE);
			String[] vals = dim.split(" ");
			int w = Integer.parseInt(vals[0]);
			int h = Integer.parseInt(vals[1]);
			region = new EditorRegion(world.getName(),world.getNumberOfRegions(),w,h);
		} else
			region = new EditorRegion(world.getName(),number);
		
		//screen bi
		screen = new BufferedImage(Program.DISPLAY_WIDTH,Program.DISPLAY_HEIGHT,BufferedImage.TYPE_INT_ARGB);
		
		//mouse init
		mouse = new Mouse(this, Program.DISPLAY_WIDTH, Program.DISPLAY_HEIGHT);
		keyboard = new Keyboard(this);
		
		//menu bar
		menuButtons = new ArrayList<MenuButton>();
		addMenuButton("Save", new Runnable() {
			public void run() {
				if (region == null) {
					JOptionPane.showMessageDialog(EditorPanel.this, "Nothing to Save!", "Message", JOptionPane.WARNING_MESSAGE);
					return;
				}
				region.save();
				JOptionPane.showMessageDialog(EditorPanel.this, "Succesfully saved region "
						+ "\n"+region.getFilePath(), "Region Save", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		//tool buttons
		toolButtons = new ArrayList<ToolButton>();
		for (Tool t : Tool.values())
			addToolButton(t);
		
		//setup texture pack
		TexturePack pack = TexturePack.DEFAULT;
		tiles = new ArrayList<Texture>();
		for (int i = 0; i < pack.getNumberOfTiles(); i++)
			tiles.add(pack.getTileTexture(i));
		objects = new ArrayList<Texture>();
		for (int i = 0; i < pack.getNumberOfObjects(); i++)
			objects.add(pack.getObjectTexture(i));
		
		//update thread
		Thread redrawThread = new Thread(new Runnable() {
			public void run() {
				long last = System.currentTimeMillis();
				while (true) {
					try {
						Thread.sleep(50);
					} catch (Exception e) {
						
					}
					try {
						redraw();
					} catch (Exception e) {
						JOptionPane.showMessageDialog(EditorPanel.this, "Something unexpected occurred!");
						e.printStackTrace();
					}
					long now = System.currentTimeMillis();
					
					float dt = (float)(now-last)/1000.0f;
					
					for (int i = 0; i < pack.getNumberOfTiles(); i++) {
						Texture tt = pack.getTileTexture(i);
						tt.getAnimation().animate(dt);
					}
					
					for (int i = 0; i < pack.getNumberOfObjects(); i++) {
						Texture tt = pack.getObjectTexture(i);
						tt.getAnimation().animate(dt);
					}
					
					timer+=dt;
					
					last = now;
				}
			}
		});
		redrawThread.start();
		
		this.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (SwingUtilities.isMiddleMouseButton(e) || (keyboard.keyDown(KeyEvent.VK_CONTROL) && SwingUtilities.isLeftMouseButton(e))) {
					Point p = e.getPoint();
					offX += p.x - prev.x;
					offY += p.y - prev.y;
				}
				prev = e.getPoint();
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				prev = e.getPoint();
			}
		});
		
		this.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (mouse.getX() > 200) {
					float mpxOnView = (float)(mouse.getX() - vx - offX),
						  mpyOnView = (float)(mouse.getY() - vy - offY);
					float mxOnRegion = (float)(mpxOnView / size),
						  myOnRegion = (float)(mpyOnView / size);
					float ds = (float)(e.getPreciseWheelRotation() * (size/10));
					size -= ds;
					float newMX = (float)(mpxOnView / size),
						  newMY = (float)(mpyOnView / size);
					float dx = (float)((newMX - mxOnRegion)*size),
					      dy = (float)((newMY - myOnRegion)*size);
					offX += dx;
					offY += dy;
				} else {
					if (EditorPanel.this.sideView == EditorPanel.SIDE_VIEW_TILE)
						tileScrollPos -= e.getPreciseWheelRotation() * 15;
					else
						objectScrollPos -= e.getPreciseWheelRotation() * 15;
				}
			}
		});
	}
	
	private int tileScrollPos = 0, objectScrollPos = 0;
	
	private Vector2 mouseOnRegion() {
		float mpxOnView = (float)(mouse.getX() - vx - offX),
			  mpyOnView = (float)(mouse.getY() - vy - offY);
		float mxOnRegion = (float)(mpxOnView / size),
			  myOnRegion = (float)(mpyOnView / size);
		return new Vector2(mxOnRegion,myOnRegion);
	}
	
	public void setTool(Tool tool) {
		this.prevTool = curTool;
		this.curTool = tool;
	}
	
	public Tool getTool() {
		return this.curTool;
	}
	
	private Point prev = null;
	
	private void addMenuButton(String name, Runnable onClick) {
		int x = 220;
		for (MenuButton b : menuButtons) 
			x += b.getWidth()+15;
		MenuButton mb = new MenuButton(name, x, 15, onClick);
		menuButtons.add(mb);
	}
	
	private void addToolButton(Tool tool) {
		int x = 220;
		int y = Program.DISPLAY_HEIGHT-170;
		for (ToolButton b : toolButtons) {
			x += b.getWidth() + 10;
			if (x > Program.DISPLAY_WIDTH-b.getWidth()) {
				x = 220;
				y += b.getHeight() + 25;
			}
		}
		ToolButton tb = new ToolButton(tool, x, y, this);
		toolButtons.add(tb);
	}
	
	private int curTile = 0;
	private int curObject = 0;
	
	private static final int SIDE_VIEW_TILE = 0, SIDE_VIEW_OBJECT = 1;
	private int sideView = SIDE_VIEW_TILE;
	
	private boolean showGrid = true;
	
	private double size = 30;
	private int offX = 50, offY = 0;
	private int vx = 230, vy = 70;
	private int vw = (int)(Program.DISPLAY_WIDTH * 0.7), vh = (int)(Program.DISPLAY_HEIGHT * 0.7);
	
	private int rotation = 0;
	private float objRotation = 0;
	
	private Vector2 wallP1 = null;
	private EditorRoad curRoad = null;
	private EditorRoad linkingRoad = null;
	private EditorRegion portalRegion = null; //represents the region of the portal when placing the portal down
	private EditorPortal portal = null; //when not null - the portal is being placed
	
	public void redraw() throws Exception {
		Graphics2D g = screen.createGraphics();
		
		//draw background
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, Program.DISPLAY_WIDTH, Program.DISPLAY_HEIGHT);
		
		g.setColor(Color.GRAY);
		g.fillRect(0, Program.DISPLAY_HEIGHT-20, Program.DISPLAY_WIDTH, 20);
		g.setColor(Color.DARK_GRAY);
		g.setFont(new Font("Arial",Font.BOLD,12));
		Vector2 mp = this.mouseOnRegion();
		mp.round(0.01f);
		g.drawString("Tile ID: "+tiles.get(curTile).getStringID()+" ("+curTile+")     X: "+mp.x+" Y: "+mp.y+"    ROT: "+rotation, 230, Program.DISPLAY_HEIGHT-8);
		
		if (this.curTool == Tool.DRAW || this.curTool == Tool.FILL) {
			if (keyboard.keyPressed('1'))
				rotation = 0;
			if (keyboard.keyPressed('2'))
				rotation = 1;
			if  (keyboard.keyPressed('3'))
				rotation = 2;
			if (keyboard.keyPressed('4'))
				rotation = 3;
			
			if (keyboard.keyPressed(KeyEvent.VK_RIGHT))
				rotation = (rotation + 1)%4;
			if (keyboard.keyPressed(KeyEvent.VK_LEFT)) {
				rotation--;
				if (rotation < 0)
					rotation = 3;
			}
		}
		
		/*
		 * Draw the region
		 */
		
		BufferedImage worldImg = new BufferedImage(vw,vh,BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D gw = worldImg.createGraphics();
		
		gw.setColor(Color.BLACK);
		gw.fillRect(0, 0, vw, vh);
		
		//start with the tiles
		if (region != null) {
			for (int x = 0; x < region.getWidth(); x++) {
				for (int y = 0; y < region.getHeight(); y++) {
					int gridSize = (int)(size * 0.1);
					int px = offX + (int)(x * size + gridSize/2), py = offY + (int)(y * size + gridSize/2);
					int pw = (int)size - gridSize, ph = (int)size - gridSize;
					if (px + pw < 0 || px > vw || py + ph < 0 || py > vh)
						continue;
					if (!showGrid) {
						px -= gridSize/2;
						py -= gridSize/2;
						pw += gridSize + 1;
						ph += gridSize + 1;
					}
					int val = region.getGridValue(x, y);
					if (val < 0) {
						gw.setColor(Color.WHITE);
						gw.fillRect(px, py, pw, ph);
					} else {
						int gridVal = region.getGridValue(x, y);
						if (gridVal >= 0 && gridVal < tiles.size()) {	
							Texture text = tiles.get(gridVal);
							if (text != null)
								gw.drawImage(ImageTools.rotate(text.getAnimation().getCurrentFrame(),region.getRotationValue(x, y)), px, py, pw, ph, null);
						}
					}
					//check for mouse clicks.. only if left button
					if (mouse.isMouseDown(Mouse.LEFT_BUTTON) && !keyboard.keyDown(KeyEvent.VK_CONTROL)) {
						if (mouse.getX() > vx + px && mouse.getX() < vx + px + pw && mouse.getY() > vy + py && mouse.getY() < vy + py + ph) {
							switch (curTool) {
							case DRAW:
								region.setGridValue(x, y, rotation, curTile);
								break;
							case ERASE:
								region.setGridValue(x, y, rotation, -1);
								break;
							case FILL:
								region.fillGrid(x,y, rotation, curTile);
								break;
							default:  //if any other tool, do nothing
								break;
							}
						}
					}
				}
			}
			EditorComponent closest = null;
			float dist = 0.25f;
			if (curTool == Tool.DELETE) {
				for (EditorComponent c : region.getType("wall")) {
					EditorWall w = (EditorWall)c;
					Line l = new Line(new Vector2(w.x1,w.y1),new Vector2(w.x2,w.y2));
					float indDist = l.distance(mp);
					if (indDist < dist) {
						dist = indDist;
						closest = w;
					}
				}
				for (EditorComponent c : region.getType("road")) {
					EditorRoad r = (EditorRoad)c;
					for (int i = 0; i < r.getPoints().size()-1; i++) {
						Vector2 p1 = r.getPoints().get(i), p2 = r.getPoints().get(i+1);
						Line l = new Line(new Vector2(p1.x,p1.y),new Vector2(p2.x,p2.y));
						float indDist = l.distance(mp);
						if (indDist < dist) {
							dist = indDist;
							closest = r;
						}
					}
				}
				for (EditorComponent c : region.getType("tag")) {
					EditorTag t = (EditorTag)c;
					Vector2 p = new Vector2(t.x+0.25f,t.y+0.25f);
					float indDist = p.getDistanceSquared(mp);
					if (indDist < dist) {
						dist = indDist;
						closest = t;
					}
				}
			}
			EditorRoad linkerClosest = null;
			dist = 0.25f;
			if (this.curTool == Tool.ROAD_LINKER || this.curTool == Tool.ROAD_ATTRIB || this.curTool == Tool.ROAD_STOP) {
				for (EditorComponent c : region.getType("road")) {
					EditorRoad r = (EditorRoad)c;
					for (int i = 0; i < r.getPoints().size()-1; i++) {
						Vector2 p1 = r.getPoints().get(i), p2 = r.getPoints().get(i+1);
						Line l = new Line(new Vector2(p1.x,p1.y),new Vector2(p2.x,p2.y));
						float indDist = l.distance(mp);
						if (indDist < dist) {
							dist = indDist;
							linkerClosest = r;
						}
					}
				}
			}
			//handle tool operations that deal with clicking
			if (mouse.isMouseDown(Mouse.LEFT_BUTTON) && !keyboard.keyDown(KeyEvent.VK_CONTROL) && mouse.getX() > vx && mouse.getX() < vx + vw && mouse.getY() > vy && mouse.getY() < vy + vh) {
				switch (curTool) {
				case PORTAL:
					if (this.portal == null) {
						try {
							portal = new EditorPortal();
							portal.x = mp.x;
							portal.y = mp.y;
							portal.destinationNumber = Integer.parseInt(JOptionPane.showInputDialog(this,"Enter region destination","Add Portal",JOptionPane.QUESTION_MESSAGE));
							portal.width = 0.5f;
							portal.height = 0.5f;
							this.portalRegion = this.region;
							this.region = new EditorRegion(this.region.getWorldName(),portal.destinationNumber);
							this.disableToolButtons();
						} catch (Exception e) {
							JOptionPane.showMessageDialog(this, "Invalid entry", "Invalid Format", JOptionPane.ERROR_MESSAGE);
						}
					} else {
						//then we should be in the other region
						this.enableToolButtons();
						this.region = this.portalRegion;
						portal.destX = mp.x;
						portal.destY = mp.y;
						this.region.addComponent(portal);
						this.portal = null;
						this.portalRegion = null;
					}
					break;
				case WALL:
					if (wallP1 == null) {
						wallP1 = mp.copy();
						if (this.keyboard.keyDown(KeyEvent.VK_SHIFT)) {
							wallP1.x = Math.round(wallP1.x);
							wallP1.y = Math.round(wallP1.y);
						}
					} else {
						Vector2 wallP2 = mp.copy();
						//for straight line correction
						if (Math.abs(wallP2.x-wallP1.x)< 0.15)
							wallP2.x = wallP1.x;
						if (Math.abs(wallP2.y-wallP1.y)<0.15)
							wallP2.y = wallP1.y;
						if (this.keyboard.keyDown(KeyEvent.VK_SHIFT)) {
							wallP2.x = Math.round(wallP2.x);
							wallP2.y = Math.round(wallP2.y);
						}
						EditorWall e = new EditorWall(wallP1.x,wallP1.y,wallP2.x,wallP2.y);
						region.addComponent(e);
						wallP1 = null;
					}
					break;
				case ROAD:
					if (curRoad == null) {
						int id = 0;
						boolean cont = true;
						while (cont) {
							cont = false;
							id = MathUtils.random(255);
							for (EditorComponent c : region.getType("road")) {
								EditorRoad r = (EditorRoad)c;
								if (r.getID() == id) {
									cont = true; //continue searching for an unused ID
									break;
								}
							}
						}
						curRoad = new EditorRoad(id);
						region.addComponent(curRoad);
						curRoad.add(mp.copy());
					} else {
						Vector2 roadP2 = mp.copy();
						Vector2 roadP1 = curRoad.getLastPoint();
						//for straight line correction
						if (Math.abs(roadP2.x-roadP1.x)< 0.15)
							roadP2.x = roadP1.x;
						if (Math.abs(roadP2.y-roadP1.y)<0.15)
							roadP2.y = roadP1.y;
						curRoad.add(roadP2);
					}
					break;
				case OBJECT:
					EditorObject o = new EditorObject();
					o.id = curObject;
					o.x = mp.x;
					o.y = mp.y;
					o.rotation = objRotation;
					region.addComponent(o);
					break;
				case DELETE:
					//if the closest wall is not null then we delete it
					if (closest != null) {
						region.removeComponent(closest);
						break;
					}
					//check to see if the mouse is over a portal
					for (EditorComponent c : region.getType("portal")) {
						EditorPortal portal = (EditorPortal)c;
						if (mp.x > portal.x && mp.x < portal.x + portal.width && mp.y > portal.y && mp.y < portal.y + portal.height) {
							region.removeComponent(portal);
							break;
						}
					}
					for (EditorComponent c : region.getType("object")) {
						EditorObject ob = (EditorObject)c;
						Texture t = objects.get(ob.id);
						if (mp.x > ob.x && mp.x < ob.x + t.getWidth() && mp.y > ob.y && mp.y < ob.y + t.getHeight()) {
							region.removeComponent(ob);
							break;
						}
					}
					break;
				case ROAD_LINKER: case ROAD_STOP:
					if (linkerClosest != null) {
						if (this.linkingRoad != null && this.linkingRoad != linkerClosest) {
							if (this.curTool == Tool.ROAD_LINKER) 
								this.linkingRoad.link(linkerClosest);
							else
								this.linkingRoad.intersect(linkerClosest);
							this.linkingRoad = null;
						} else {
							this.linkingRoad = linkerClosest;
						}
					}
					break;
				case ROAD_ATTRIB:
					if (linkerClosest != null) {
						//set the properties
						float carRate = Float.parseFloat(JOptionPane.showInputDialog(this,"Enter the car rate",linkerClosest.getCarRate()+""));
						float speedLimit = Float.parseFloat(JOptionPane.showInputDialog(this,"Enter the speed limit",linkerClosest.getSpeedLimit()+""));
						linkerClosest.setCarRate(carRate);
						linkerClosest.setSpeedLimit(speedLimit);
					}
					break;
				case TAG:
					String text = JOptionPane.showInputDialog(this, "What should it say?");
					EditorTag tag = new EditorTag(text,mp.x,mp.y);
					this.region.addComponent(tag);
					break;
				case INTERACTABLE:
					String id = JOptionPane.showInputDialog(this, "What destination?");
					EditorInteractable inter = new EditorInteractable(id,mp.x,mp.y);
					this.region.addComponent(inter);
					break;
				default:
					break;
				}
				Tool[] tests = {Tool.INTERACTABLE,Tool.TAG,Tool.ROAD,Tool.ROAD_ATTRIB,Tool.ROAD_LINKER,Tool.ROAD_STOP,Tool.PORTAL,Tool.OBJECT,Tool.WALL,Tool.DELETE};
				for (Tool tool : tests)
					if (curTool == tool) {
						mouse.setIsMouseDown(Mouse.LEFT_BUTTON, false);
						break;
					}
			}
			for (EditorComponent c : region.getType("portal")) {
				EditorPortal p = (EditorPortal)c;
				int px = offX + (int)(p.x * size), py = offY + (int)(p.y * size);
				int pw = (int)(p.width * size), ph = (int)(p.height * size);
				if (curTool == Tool.DELETE && mp.x > p.x && mp.x < p.x + p.width && mp.y > p.y && mp.y < p.y + p.height)
					gw.setColor(Color.RED);
				else
					gw.setColor(Color.MAGENTA);
				gw.fillRoundRect(px, py, pw, ph, (int)size/10, (int)size/10);
			}
			for (EditorComponent c : region.getType("wall")) {
				EditorWall w = (EditorWall)c;
				int px1 = offX + (int)(w.x1 * size), py1 = offY + (int)(w.y1 * size);
				int px2 = offX + (int)(w.x2 * size), py2 = offY + (int)(w.y2 * size);
				if (w == closest)
					gw.setColor(Color.RED);
				else
					gw.setColor(Color.YELLOW);
				gw.setStroke(new BasicStroke((int)(size * 0.1)));
				gw.drawLine(px1, py1, px2, py2);
			}
			for (EditorComponent c : region.getType("road")) {
				EditorRoad r = (EditorRoad)c;
				if (r == closest)
					gw.setColor(Color.RED);
				else
					gw.setColor(Color.GREEN);
				if (curRoad == r && timer % 1f > 0.5f)
					gw.setColor(Color.CYAN);
				if (linkerClosest == r)
					gw.setColor(Color.BLUE);
				if (linkingRoad == r && timer % 1f > 0.5f)
					gw.setColor(Color.YELLOW);
				
				gw.setStroke(new BasicStroke((int)(size * 0.1)));
				List<Vector2> points = r.getPoints();
				for (int i = 0; i < points.size()-1; i++) {
					Vector2 p1 = points.get(i), p2 = points.get(i+1);
					drawLine(gw,p1.x,p1.y,p2.x,p2.y);
					//draw the arrows too
					float interval = 2;
					float wingSize = 0.5f;
					double angle = MathUtils.getAngle(p1.x, p1.y, p2.x, p2.y);
					float distance = MathUtils.distance(p1.x, p1.y, p2.x, p2.y);
					for (float d = interval; d < distance; d += interval) {
						double px = (p1.x + Math.cos(angle) * d), py = (p1.y + Math.sin(angle) * d);
						double backAngle = angle + Math.PI*6/5;
						double epx = px + Math.cos(backAngle)*wingSize;
						double epy = py + Math.sin(backAngle)*wingSize;
						drawLine(gw,px,py,epx,epy);
						backAngle = angle - Math.PI*6/5.0;
						epx = px + Math.cos(backAngle)*wingSize;
						epy = py + Math.sin(backAngle)*wingSize;
						drawLine(gw,px,py,epx,epy);
					}
				}
				if (curTool == Tool.ROAD_LINKER || curTool == Tool.ROAD_STOP) {
					List<Integer> ids;
					if (curTool == Tool.ROAD_LINKER) {
						gw.setColor(Color.CYAN);
						ids = r.getLinkedIDs();
					} else {
						gw.setColor(Color.RED);
						ids = r.getIntersectionIDs();
					}
					float[] dash = {(float) (0.2f * size)};
					gw.setStroke(new BasicStroke((int)(0.1 * size),BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND,5,dash,0));
					for (Integer id : ids) {
						for (EditorComponent other : region.getType("road")) {
							EditorRoad otherR = (EditorRoad)other;
							if (r == otherR)
								continue;
							if (id == otherR.getID()) {
								//draw a dashed line
								if (curTool == Tool.ROAD_LINKER)
									drawLine(gw, r.getLastPoint().x, r.getLastPoint().y, otherR.getFirstPoint().x, otherR.getFirstPoint().y);
								else
									drawLine(gw, r.getLastPoint().x, r.getLastPoint().y, otherR.getLastPoint().x, otherR.getLastPoint().y);
							} 
						}
					}
				}
			}
			for (EditorComponent c : region.getType("object")) {
				EditorObject o = (EditorObject)c;
				int px = offX + (int)(o.x * size), py = offY + (int)(o.y * size);
				Texture texture = objects.get(o.id);
				int pw = (int)(texture.getWidth() * size), ph = (int)(texture.getHeight() * size);
				BufferedImage img = texture.getAnimation().getCurrentFrame();
				if (curTool == Tool.DELETE && mp.x > o.x && mp.x < o.x + texture.getWidth() && mp.y > o.y && mp.y < o.y + texture.getHeight())
					img = ImageTools.colorscale(img, Color.RED);
				gw.rotate(o.rotation, px+pw/2, py+ph/2);
				gw.drawImage(img, px, py, pw, ph, null);
				gw.rotate(-o.rotation, px+pw/2, py+ph/2);
			}
			for (EditorComponent c : region.getType("tag")) {
				EditorTag t = (EditorTag)c;
				int px = offX + (int)(t.x * size), py = offY + (int)(t.y * size);
				gw.setFont(new Font(Program.FONT_FAMILY,Font.BOLD,(int)size/2));
				gw.setColor(Color.WHITE);
				if (c == closest)
					gw.setColor(Color.RED);
				gw.drawString(t.text, px-gw.getFontMetrics().stringWidth(t.text)/2, py);
			}
			for (EditorComponent c : region.getType("interactable")) {
				EditorInteractable i = (EditorInteractable)c;
				int px = offX + (int)(i.x * size), py = offY + (int)(i.y * size);
				gw.setColor(new Color(125,125,125,125));
				gw.fillOval(px-(int)size/2, py-(int)size/2, (int)size, (int)size);
			}
			if (curTool == Tool.OBJECT) {
				int px = offX  + (int)(mp.x * size), py = offY + (int)(mp.y * size);
				Texture texture = objects.get(curObject);
				int pw = (int)(texture.getWidth() * size), ph = (int)(texture.getHeight() * size);
				gw.rotate(objRotation,px+pw/2,py+ph/2);
				gw.drawImage(ImageTools.setTransparency(texture.getAnimation().getCurrentFrame(), 160), px, py, pw, ph, null);
				gw.rotate(-objRotation,px+pw/2,py+ph/2);
			}
		}
		if (wallP1 != null) {
			gw.setColor(Color.CYAN);
			int px1 = (int)(wallP1.x * size + offX),
				py1 = (int)(wallP1.y * size + offY);
			Vector2 p2 = mp;
			if (Math.abs(p2.x - wallP1.x) < 0.15)
				p2.x = wallP1.x;
			if (Math.abs(p2.y - wallP1.y) < 0.15)
				p2.y = wallP1.y;
			if (this.keyboard.keyDown(KeyEvent.VK_SHIFT)) {
				p2.x = Math.round(p2.x);
				p2.y = Math.round(p2.y);
			}
			int px2 = (int)(p2.x * size + offX),
				py2 = (int)(p2.y * size + offY);
			gw.setStroke(new BasicStroke((int)(size * 0.1)));
			gw.drawLine(px1,py1,px2,py2);
		}
		if (curRoad != null) {
			gw.setColor(Color.CYAN);
			Vector2 p1 = curRoad.getLastPoint();
			int px1 = (int)(p1.x * size + offX), py1 = (int)(p1.y * size + offY);
			Vector2 p2 = mp;
			if (Math.abs(p2.x - p1.x) < 0.15)
				p2.x = p1.x;
			if (Math.abs(p2.y - p1.y) < 0.15)
				p2.y = p1.y;
			int px2 = (int)(p2.x * size + offX), py2 = (int)(p2.y * size + offY);
			gw.setStroke(new BasicStroke((int)(size * 0.1)));
			gw.drawLine(px1, py1, px2, py2);
			if (this.keyboard.keyPressed(KeyEvent.VK_ENTER))
				curRoad = null;
		}
		if (curTool != Tool.WALL)
			wallP1 = null;
		if (curTool != Tool.ROAD)
			curRoad = null;
		switch (curTool) {
		case TOGGLE_GRID:
			showGrid = !showGrid;
			curTool = prevTool;
			break;
		case SET_LIGHT:
			try {
				String input = JOptionPane.showInputDialog(this, "Enter the local light value as a number\nbetween 0 and 1");
				float val = Float.parseFloat(input);
				this.region.setLocalLightValue(val);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "Must be a number!");
			}
			curTool = prevTool;
		case EXPAND:
			if (keyboard.keyPressed(KeyEvent.VK_UP))
				region.addRowTop();
			if (keyboard.keyPressed(KeyEvent.VK_DOWN))
				region.addRowBottom();
			if (keyboard.keyPressed(KeyEvent.VK_LEFT))
				region.addColumnLeft();
			if (keyboard.keyPressed(KeyEvent.VK_RIGHT))
				region.addColumnRight();
			break;
		case SHRINK:
			if (keyboard.keyPressed(KeyEvent.VK_UP))
				region.removeRowTop();
			if (keyboard.keyPressed(KeyEvent.VK_DOWN))
				region.removeRowBottom();
			if (keyboard.keyPressed(KeyEvent.VK_LEFT))
				region.removeColumnLeft();
			if (keyboard.keyPressed(KeyEvent.VK_RIGHT))
				region.removeColumnRight();
			break;
		default:
			break;
		}
		
		if (this.curTool == Tool.OBJECT) {
			double speed = Math.PI/50;
			if (this.keyboard.keyDown(KeyEvent.VK_LEFT)) {
				this.objRotation+=speed;
			} 
			if (this.keyboard.keyDown(KeyEvent.VK_RIGHT)) {
				this.objRotation-=speed;
			}
		}
		
		if (curTool == Tool.OBJECT)
			sideView = SIDE_VIEW_OBJECT;
		else if (curTool == Tool.DRAW || curTool == Tool.FILL)
			sideView = SIDE_VIEW_TILE;
		
		g.drawImage(worldImg, vx, vy, vw, vh, null);
		
		drawSideBar(g);
		drawRegionInfo(g);
		drawButtons(g);
		
		repaint();
	}
	
	private void drawLine(Graphics2D g, double x1, double y1, double x2, double y2) {
		g.drawLine((int)(offX + x1 * size), (int)(offY + y1 * size), (int)(offX + x2 * size), (int)(offY + y2 * size));
	}
	
	private boolean toolButtonsEnabled = true;
	private void enableToolButtons() {
		for (ToolButton b : toolButtons)
			b.enable();
		this.toolButtonsEnabled = true;
	}
	
	private void disableToolButtons() {
		for (ToolButton b : toolButtons)
			b.disable();
		this.toolButtonsEnabled = false;
	}
	
	private void drawButtons(Graphics2D g) {
		for (ToolButton t : toolButtons) {
			if (toolButtonsEnabled)
				t.check(mouse);
			t.draw(g);
		}
		for (MenuButton b : menuButtons) {
			b.check(mouse);
			b.draw(g);
		}
	}
	
	private void drawRegionInfo(Graphics2D g) {
		g.setColor(Color.DARK_GRAY);
		g.setFont(new Font("Consolas",Font.ITALIC | Font.BOLD,14));
		String info = "No Info";
		if (region != null) {
			info = "Region: "+region.getFilePath()+" | World: "+region.getWorldName()+" | Number: "+region.getRegionNumber();
		}
		g.drawString(info, 230, 60);
	}
	
	private void drawSideBar(Graphics2D g) {
		//left bar lists out each tile
		g.setColor(Color.RED.darker());
		g.fillRect(0, 0, 200, Program.DISPLAY_HEIGHT);
		if (sideView == SIDE_VIEW_TILE) {
			int tileSize = 60;
			for (int i = 0; i < tiles.size(); i++) {
				int x = 60 - tileSize/2;
				if (i % 2 == 1)
					x += 80;
				int y = (i / 2) * (tileSize+10) + 70 + tileScrollPos;
				g.drawImage(ImageTools.rotate(tiles.get(i).getAnimation().getCurrentFrame(), rotation),x,y,tileSize,tileSize,null);
				if (mouse.getX() > x && mouse.getX() < x + tileSize && mouse.getY() > y && mouse.getY() < y + tileSize) {
					g.setColor(Color.GRAY);
					g.setStroke(new BasicStroke(4));
					g.drawRoundRect(x-5, y-5, tileSize+10, tileSize+10, 4, 4);
				}
				if (curTile == i) {
					g.setColor(Color.CYAN);
					g.setStroke(new BasicStroke(4));
					g.drawRoundRect(x-5, y-5, tileSize+10, tileSize+10, 4, 4);
				}
				//check if mouse is clicked over it
				if (mouse.isMouseDown()) {
					if (mouse.getX() > x && mouse.getX() < x + tileSize && mouse.getY() > y && mouse.getY() < y + tileSize) {
						curTile = i;
					}
				}
			}
		} else if (sideView == SIDE_VIEW_OBJECT) {
			int objSize = 60;
			int y = 70+objectScrollPos;
			for (int i = 0; i < objects.size(); i++) {
				Texture t = objects.get(i);
				int width = (int)(t.getWidth() * objSize),
					height = (int)(t.getHeight() * objSize);
				int x = (int)(100 - width/2);
				g.drawImage(t.getAnimation().getCurrentFrame(), x, y, width, height, null);
				if (i == curObject) {
					//draw a dashed blue line around it
					float[] dash = {10.0f};
					g.setStroke(new BasicStroke(5,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND,5,dash,0));
					g.setColor(Color.CYAN);
					g.drawRect(x-4, y-4, width+8, height+8);
				}
				if (mouse.isMouseDown()) {
					if (mouse.getX() > x && mouse.getX() < x + width && mouse.getY() > y && mouse.getY() < y + height) {
						curObject = i;
					}
				}
				y+=(height+10);
			}
		}
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(10));
		g.drawRect(0, 0, 200, Program.DISPLAY_HEIGHT);
		g.setColor(Color.LIGHT_GRAY);
		g.fillRoundRect(30,20,140,36,12,12);
		g.setColor(Color.DARK_GRAY );
		g.setFont(new Font("Arial",Font.BOLD,30));
		String s = "";
		switch (sideView) {
		case SIDE_VIEW_TILE:
			s = "TILES";
			break;
		case SIDE_VIEW_OBJECT:
			s = "OBJECTS";
		}
		g.drawString(s, 100-g.getFontMetrics().stringWidth(s)/2, 50);
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(screen,0,0,getWidth(),getHeight(),null);
	}
}
