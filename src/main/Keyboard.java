package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.*;

import javax.swing.JPanel;

public class Keyboard {
	private JPanel panel;
	
	private List<Key> keys;
	
	
	public Keyboard(JPanel panel) {
		this.panel = panel;
		
		keys = new ArrayList<Key>();
		
		this.panel.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				Key k = new Key(e);
				for (Key key : keys) 
					if (k.keycode() == key.keycode())
						return; //it is already in the key list
				keys.add(k);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				for (int i = 0; i < keys.size(); i++) {
					if (e.getKeyCode() == keys.get(i).keycode()) {
						keys.remove(i);
						i--;
					}
				}
			}
		});
	}
	
	public boolean keyDown(int keycode) {
		for (Key k : keys)
			if (k.keycode() == keycode)
				return true;
		return false;
	}
	
	public boolean keyDown(char c) {
		for (Key k : keys)
			if (k.character() == c)
				return true;
		return false;
	}
	
	/**
	 * //returns true if the keycode is in the list and removes it so it is only called once
	 * @param keycode key event keycode
	 * @return true if the key is in the lest
	 */
	public boolean keyPressed(int keycode) {
		for (Key k : keys)
			if (k.keycode() == keycode) {
				keys.remove(k);
				return true;
			}
		return false;
	}
	
	private class Key {
		private int keycode;
		private char c;
		public Key(KeyEvent e) {
			keycode = e.getKeyCode();
			c = e.getKeyChar();
		}
		
		public int keycode() {
			return keycode;
		}
		
		public char character() {
			return c;
		}
	}
}
