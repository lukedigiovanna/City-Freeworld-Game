package levelEditor;

import javax.swing.*;

import display.*;
import levelEditor.editorComponents.EditorPortal;
import levelEditor.editorComponents.EditorRegion;
import main.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class EditorPanel extends JPanel {
	
	private BufferedImage screen;
	private List<TileTexture> tiles;
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
		tiles = new ArrayList<TileTexture>();
		for (int i = 0; i < pack.getNumberOfTiles(); i++)
			tiles.add(pack.getTileTexture(i));
		
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
					
					for (int i = 0; i < pack.getNumberOfTiles(); i++) {
						TileTexture tt = pack.getTileTexture(i);
						float dt = (float)(now-last)/1000.0f;
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
				size -= e.getPreciseWheelRotation() * (size/10);
			}
		});
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
	
	private boolean showGrid = true;
	
	private double size = 30;
	private int offX = 50, offY = 0;
	
	public void redraw() {
		Graphics2D g = screen.createGraphics();
		
		//draw background
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, Program.DISPLAY_WIDTH, Program.DISPLAY_HEIGHT);
		
		g.setColor(Color.GRAY);
		g.fillRect(0, Program.DISPLAY_HEIGHT-20, Program.DISPLAY_WIDTH, 20);
		g.setColor(Color.DARK_GRAY);
		g.setFont(new Font("Arial",Font.BOLD,12));
		g.drawString("Tile ID: "+tiles.get(curTile).getStringID(), 230, Program.DISPLAY_HEIGHT-8);
		
		/*
		 * Draw the region
		 */
		
		int vx = 230, vy = 100;
		int vw = (int)(Program.DISPLAY_WIDTH * 0.7), vh = (int)(Program.DISPLAY_HEIGHT * 0.7);
		
		BufferedImage worldImg = new BufferedImage(vw,vh,BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D gw = worldImg.createGraphics();
		
		gw.setColor(Color.BLACK);
		gw.fillRect(0, 0, vw, vh);
		
		//start with the tiles
		if (region != null) {
			for (int x = 0; x < region.getWidth(); x++) {
				for (int y = 0; y < region.getHeight(); y++) {
					int gridSize = (int)(size * 0.1);
					int px = offX + x * (int)size + gridSize/2, py = offY + y * (int)size + gridSize/2;
					int pw = (int)size - gridSize, ph = (int)size - gridSize;
					if (px + pw < 0 || px > vw || py + ph < 0 || py > vh)
						continue;
					if (!showGrid) {
						px -= gridSize/2;
						py -= gridSize/2;
						pw += gridSize;
						ph += gridSize;
					}
					int val = region.getGridValue(x, y);
					if (val < 0) {
						gw.setColor(Color.WHITE);
						gw.fillRect(px, py, pw, ph);
					} else {
						gw.drawImage(tiles.get(region.getGridValue(x, y)).getAnimation().getCurrentFrame(), px, py, pw, ph, null);
					}
					//check for mouse clicks.. only if left button
					if (mouse.isMouseDown(Mouse.LEFT_BUTTON) && !keyboard.keyDown(KeyEvent.VK_CONTROL)) {
						if (mouse.getX() > vx + px && mouse.getX() < vx + px + pw && mouse.getY() > vy + py && mouse.getY() < vy + py + ph) {
							switch (curTool) {
							case DRAW:
								region.setGridValue(x, y, curTile);
								break;
							case ERASE:
								region.setGridValue(x, y, -1);
								break;
							case FILL:
								region.fillGrid(x,y,curTile);
								break;
							case PORTAL:
								EditorPortal p = new EditorPortal();
								p.x = x;
								p.y = y;
								p.destinationNumber = Integer.parseInt(JOptionPane.showInputDialog(this,"Enter region destination","Add Portal",JOptionPane.QUESTION_MESSAGE));
								p.width = 0.5f;
								p.height = 0.5f;
								String[] vals = JOptionPane.showInputDialog(this,"Enter portal destination coords","Add Portal",JOptionPane.QUESTION_MESSAGE).split(" ");
								p.destX = Float.parseFloat(vals[0]);
								p.destY = Float.parseFloat(vals[1]);
								this.region.getPortals().add(p);
								mouse.setIsMouseDown(Mouse.LEFT_BUTTON, false);
								break;
							default:  //if any other tool, do nothing
								break;
							}
						}
					}
				}
			}
			for (EditorPortal p : region.getPortals()) {
				int px = offX + (int)(p.x * size), py = offY + (int)(p.y * size);
				int pw = (int)(p.width * size), ph = (int)(p.height * size);
				gw.setColor(Color.MAGENTA);
				gw.fillRoundRect(px, py, pw, ph, (int)size/10, (int)size/10);
			}
		}
		switch (curTool) {
		case TOGGLE_GRID:
			showGrid = !showGrid;
			curTool = prevTool;
			break;
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
		}
		
		g.drawImage(worldImg, vx, vy, vw, vh, null);
		
		//left bar lists out each tile
		g.setColor(Color.RED.darker());
		g.fillRect(0, 0, 200, Program.DISPLAY_HEIGHT);
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(10));
		g.drawRect(0, 0, 200, Program.DISPLAY_HEIGHT);
		g.setColor(Color.LIGHT_GRAY);
		g.fillRoundRect(30,20,140,36,12,12);
		g.setColor(Color.DARK_GRAY );
		g.setFont(new Font("Arial",Font.BOLD,30));
		g.drawString("TILES", 100-g.getFontMetrics().stringWidth("TILES")/2, 50);
		int tileSize = 60;
		for (int i = 0; i < tiles.size(); i++) {
			int x = 60 - tileSize/2;
			if (i % 2 == 1)
				x += 80;
			int y = (i / 2) * (tileSize+10) + 70;
			g.drawImage(tiles.get(i).getAnimation().getCurrentFrame(),x,y,tileSize,tileSize,null);
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
		
		for (MenuButton b : menuButtons) {
			b.check(mouse);
			b.draw(g);
		}
		
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
		
		for (ToolButton t : toolButtons) {
			t.check(mouse);
			t.draw(g);
		}
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(screen,0,0,getWidth(),getHeight(),null);
	}
}
