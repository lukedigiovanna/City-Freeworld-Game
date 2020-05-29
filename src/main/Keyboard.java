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
	
	/*
	 * METHODS:
	 * 	keyDown(int keycode); returns true if the specified VK keycode is held down
	 * 	keyDown(char c); returns true if the specified character is held down
	 *  keyPressed(int keycode); returns true once if the specified VK keycode is pressed
	 *  keyPressed(int ... keycodes); returns true if all the keys are pressed
	 *  keyPressed(char c); returns true if the character is pressed
	 */
	
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
	 * returns true if the keycode is in the list and removes it so it is only called once
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
	
	public boolean keyPressed(int ... keycodes) {
		boolean boo = true;
		for (int kc : keycodes) {
			boolean has = false;
			//make sure the testing keycode in the key list
			for (Key k : keys) {
				has = has || (k.keycode == kc);
				if (has)
					break;
			}
			boo = boo && has;
			if (!boo)
				return false; //dont have it
		}
		for (int kc : keycodes)
			for (int i = 0; i < keys.size(); i++)
				if (keys.get(i).keycode == kc) {
					keys.remove(keys.get(i));
					i--;
				}
		return true;
	}
	
	/**
	 * returns true if the character is in the list and removes it so it is only called once
	 * @param c
	 * @return
	 */
	public boolean keyPressed(char c) {
		for (Key k : keys)
			if (k.character() == c) {
				keys.remove(k);
				return true;
			}
		return false;
	}
	
	public Key getNextKey() {
		if (keys.size() == 0)
			return null;
		else {
			Key next = keys.get(0);
			keys.remove(0);
			return next;
		}
	}
	
	public Keyboard.Key[] getAllKeysDown() {
		Keyboard.Key[] arr = new Keyboard.Key[keys.size()];
		for (int i = 0; i < arr.length; i++) 
			arr[i] = keys.get(i);
		return arr;
	}
	
	public class Key {
		private int keycode;
		private char c;
		
		public Key(KeyEvent e) {
			keycode = e.getKeyCode();
			c = Character.toLowerCase(e.getKeyChar());
		}
		
		public int keycode() {
			return keycode;
		}
		
		public char character() {
			return c;
		}
	}
}
