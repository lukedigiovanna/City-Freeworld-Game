package levelEditor;

import javax.swing.*;

import display.textures.TexturePack;
import display.textures.Texture;
import levelEditor.editorComponents.EditorObject;
import levelEditor.editorComponents.EditorPortal;
import levelEditor.editorComponents.EditorRegion;
import levelEditor.editorComponents.EditorWall;
import main.*;
import misc.ImageTools;
import misc.Line;
import misc.Vector2;

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
	
	public EditorPanel() {
		this.setFocusable(true);
		
		//screen bi
		screen = new BufferedImage(Program.DISPLAY_WIDTH,Program.DISPLAY_HEIGHT,BufferedImage.TYPE_INT_ARGB);
		
		//mouse init
		mouse = new Mouse(this, Program.DISPLAY_WIDTH, Program.DISPLAY_HEIGHT);
		keyboard = new Keyboard(this);
		
		//menu bar
		menuButtons = new ArrayList<MenuButton>();
		addMenuButton("Load Region", new Runnable() {
			public void run() {
				try {
					String worldName = JOptionPane.showInputDialog(EditorPanel.this, "World Name?", "Load Region", JOptionPane.QUESTION_MESSAGE);
					int regNum = Integer.parseInt(JOptionPane.showInputDialog(EditorPanel.this, "Region Number?", "Load Region", JOptionPane.QUESTION_MESSAGE));
					region = new EditorRegion(worldName, regNum);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(EditorPanel.this, "Invalid format occurred!\nTry again", "Invalid Format", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		addMenuButton("New Region",new Runnable() {
			public void run() {
				try {
					String worldName = JOptionPane.showInputDialog(EditorPanel.this, "World Name?", "Create Region", JOptionPane.QUESTION_MESSAGE);
					int regNum = Integer.parseInt(JOptionPane.showInputDialog(EditorPanel.this, "Region Number?", "Create Region", JOptionPane.QUESTION_MESSAGE));
					String dim = JOptionPane.showInputDialog(EditorPanel.this, "What dimensions?", "Create Region", JOptionPane.QUESTION_MESSAGE);
					String[] vals = dim.split(" ");
					int w = Integer.parseInt(vals[0]);
					int h = Integer.parseInt(vals[1]);
					region = new EditorRegion(worldName, regNum, w, h);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(EditorPanel.this, "Invalid format occured!\nTry again", "Invalid Format", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		addMenuButton("Save", new Runnable() {
			public void run() {
				if (region == null) {
					JOptionPane.showMessageDialog(EditorPanel.this, "Nothing to Save!", "Message", JOptionPane.WARNING_MESSAGE);
					return;
				}
				region.save();
				region.printGrid();
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
					redraw();
					
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
		for (ToolButton b : toolButtons)
			x += b.getWidth() + 10;
		ToolButton tb = new ToolButton(tool, x, Program.DISPLAY_HEIGHT-100, this);
		toolButtons.add(tb);
	}
	
	private int curTile = 0;
	private int curObject = 0;
	
	private static final int SIDE_VIEW_TILE = 0, SIDE_VIEW_OBJECT = 1;
	private int sideView = SIDE_VIEW_TILE;
	
	private boolean showGrid = true;
	
	private double size = 30;
	private int offX = 50, offY = 0;
	private int vx = 230, vy = 100;
	private int vw = (int)(Program.DISPLAY_WIDTH * 0.7), vh = (int)(Program.DISPLAY_HEIGHT * 0.7);
	
	private int rotation = 0;
	
	private Vector2 wallP1 = null;
	
	public void redraw() {
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
		g.drawString("Tile ID: "+tiles.get(curTile).getStringID()+"     X: "+mp.x+" Y: "+mp.y+"    ROT: "+rotation, 230, Program.DISPLAY_HEIGHT-8);
		
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
						gw.drawImage(ImageTools.rotate(tiles.get(region.getGridValue(x, y)).getAnimation().getCurrentFrame(),region.getRotationValue(x, y)), px, py, pw, ph, null);
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
			EditorWall closest = null;
			float dist = 0.25f;
			if (curTool == Tool.DELETE)
				for (EditorWall w : region.getWalls()) {
					Line l = new Line(new Vector2(w.x1,w.y1),new Vector2(w.x2,w.y2));
					float indDist = l.distance(mp);
					if (indDist < dist) {
						dist = indDist;
						closest = w;
					}
				}
			if (mouse.isMouseDown(Mouse.LEFT_BUTTON) && !keyboard.keyDown(KeyEvent.VK_CONTROL) && mouse.getX() > vx && mouse.getX() < vx + vw && mouse.getY() > vy && mouse.getY() < vy + vh) {
				switch (curTool) {
				case PORTAL:
					try {
						EditorPortal p = new EditorPortal();
						p.x = mp.x;
						p.y = mp.y;
						p.destinationNumber = Integer.parseInt(JOptionPane.showInputDialog(this,"Enter region destination","Add Portal",JOptionPane.QUESTION_MESSAGE));
						p.width = 0.5f;
						p.height = 0.5f;
						String[] vals = JOptionPane.showInputDialog(this,"Enter portal destination coords","Add Portal",JOptionPane.QUESTION_MESSAGE).split(" ");
						p.destX = Float.parseFloat(vals[0]);
						p.destY = Float.parseFloat(vals[1]);
						this.region.getPortals().add(p);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(this, "Invalid entry", "Invalid Format", JOptionPane.ERROR_MESSAGE);
					}
					break;
				case WALL:
					if (wallP1 == null) {
						wallP1 = mp.copy();
					} else {
						Vector2 wallP2 = mp.copy();
						//for straight line correction
						if (Math.abs(wallP2.x-wallP1.x)< 0.15)
							wallP2.x = wallP1.x;
						if (Math.abs(wallP2.y-wallP1.y)<0.15)
							wallP2.y = wallP1.y;
						EditorWall e = new EditorWall(wallP1.x,wallP1.y,wallP2.x,wallP2.y);
						region.getWalls().add(e);
						wallP1 = null;
					}
					break;
				case OBJECT:
					EditorObject o = new EditorObject();
					o.id = curObject;
					o.x = mp.x;
					o.y = mp.y;
					region.getObjects().add(o);
					break;
				case DELETE:
					//if the closest wall is not null then we delete it
					if (closest != null) {
						region.getWalls().remove(closest);
						break;
					}
					//check to see if the mouse is over a portal
					for (EditorPortal portal : region.getPortals()) {
						if (mp.x > portal.x && mp.x < portal.x + portal.width && mp.y > portal.y && mp.y < portal.y + portal.height) {
							region.getPortals().remove(portal);
							break;
						}
					}
					for (int i = 0; i < region.getObjects().size(); i++) {
						EditorObject ob = region.getObjects().get(i);
						Texture t = objects.get(ob.id);
						if (mp.x > ob.x && mp.x < ob.x + t.getWidth() && mp.y > ob.y && mp.y < ob.y + t.getHeight()) {
							region.getObjects().remove(ob);
							break;
						}
					}
					break;
				default:
					break;
				}
				if (curTool == Tool.PORTAL || curTool == Tool.OBJECT || curTool == Tool.WALL || curTool == Tool.DELETE)
					mouse.setIsMouseDown(Mouse.LEFT_BUTTON, false);
			}
			for (EditorPortal p : region.getPortals()) {
				int px = offX + (int)(p.x * size), py = offY + (int)(p.y * size);
				int pw = (int)(p.width * size), ph = (int)(p.height * size);
				if (curTool == Tool.DELETE && mp.x > p.x && mp.x < p.x + p.width && mp.y > p.y && mp.y < p.y + p.height)
					gw.setColor(Color.RED);
				else
					gw.setColor(Color.MAGENTA);
				gw.fillRoundRect(px, py, pw, ph, (int)size/10, (int)size/10);
			}
			for (EditorWall w : region.getWalls()) {
				int px1 = offX + (int)(w.x1 * size), py1 = offY + (int)(w.y1 * size);
				int px2 = offX + (int)(w.x2 * size), py2 = offY + (int)(w.y2 * size);
				if (w == closest)
					gw.setColor(Color.RED);
				else
					gw.setColor(Color.YELLOW);
				gw.setStroke(new BasicStroke((int)(size * 0.1)));
				gw.drawLine(px1, py1, px2, py2);
			}
			for (EditorObject o : region.getObjects()) {
				int px = offX + (int)(o.x * size), py = offY + (int)(o.y * size);
				Texture texture = objects.get(o.id);
				int pw = (int)(texture.getWidth() * size), ph = (int)(texture.getHeight() * size);
				BufferedImage img = texture.getAnimation().getCurrentFrame();
				if (curTool == Tool.DELETE && mp.x > o.x && mp.x < o.x + texture.getWidth() && mp.y > o.y && mp.y < o.y + texture.getHeight())
					img = ImageTools.colorscale(img, Color.RED);
				gw.drawImage(img, px, py, pw, ph, null);
			}
			if (curTool == Tool.OBJECT) {
				int px = offX + (int)(mp.x * size), py = offY + (int)(mp.y * size);
				Texture texture = objects.get(curObject);
				int pw = (int)(texture.getWidth() * size), ph = (int)(texture.getHeight() * size);
				gw.drawImage(ImageTools.setTransparency(texture.getAnimation().getCurrentFrame(), 160), px, py, pw, ph, null);
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
			int px2 = (int)(p2.x * size + offX),
				py2 = (int)(p2.y * size + offY);
			gw.setStroke(new BasicStroke((int)(size * 0.1)));
			gw.drawLine(px1,py1,px2,py2);
		}
		if (curTool != Tool.WALL)
			wallP1 = null;
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
	
	private void drawButtons(Graphics2D g) {
		for (ToolButton t : toolButtons) {
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
		String[] info = {
			"No Region",
			"No World",
			"No Number"
		};
		if (region != null ) {
			info[0] = "Region: "+region.getFilePath();
			info[1] = "World: "+region.getWorldName();
			info[2] = "Number: "+region.getRegionNumber();
		}
		for (int i = 0; i < info.length; i++) {
			g.drawString(info[i], 230, 60+i*16);
		}
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
