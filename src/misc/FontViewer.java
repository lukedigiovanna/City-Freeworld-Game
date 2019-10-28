package misc;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class FontViewer {

	private static final int SIZE = 700;
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Font viewer");
		frame.setSize(SIZE,SIZE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.requestFocus();
		frame.setContentPane(new Panel());
		frame.setVisible(true);
	}
	
	
	private static class Panel extends JPanel {
		private double position = 0;
		
		private String[] fonts;
		
		public Panel() {
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			fonts = ge.getAvailableFontFamilyNames();
			
			this.addMouseWheelListener(new MouseWheelListener() {
				@Override
				public void mouseWheelMoved(MouseWheelEvent e) {
					position+=(e.getPreciseWheelRotation()*5);
					if (position < 0)
						position = 0;
					repaint();
				}
			});
			
			this.setFocusable(true);
			this.addKeyListener(new KeyListener() {
				@Override
				public void keyTyped(KeyEvent e) {
					
				}

				@Override
				public void keyPressed(KeyEvent e) {
					System.out.println("Key Char: \'"+e.getKeyChar()+"\' Code: "+e.getKeyCode()+"\n");
				}

				@Override
				public void keyReleased(KeyEvent e) {
					
				}
			});
		}
		
		public void paintComponent(Graphics g) {
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, SIZE, SIZE);
			g.setColor(Color.BLACK);
			int height = 20;
			int total = SIZE/(height+2)+1;
			for (int i = (int)position; i < position+total; i++) {
				if (i > fonts.length-1)
					break;
				else {
					g.setFont(new Font(fonts[i],Font.PLAIN,height));
					int y = (int)((i-position)*(height+2)+height);
					g.drawString(fonts[i], 10, y);
					g.setFont(new Font("Arial",Font.PLAIN,height));
					g.drawString(fonts[i], SIZE-20-g.getFontMetrics().stringWidth(fonts[i]), y);
				}
			}
		}
	}
}
