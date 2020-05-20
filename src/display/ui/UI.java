package display.ui;

import main.Keyboard;
import main.Program;

/**
 * This class is mostly for ID purposes and handling keyboard/mouse inputs
 * Drawing and other interface type actions will be handled elsewhere.
 * 
 * This is not including in the Display class as separate UIs can be active within
 * a display, though displays themselves are considered UIs.
 */
public class UI {
	private String id; //purely for identification purposes in other classes
	public UI(String id) {
		this.id = id;
	}
	
	public String getID() {
		return this.id;
	}
	
	/*
	 * Methods for KEYBOARD and MOUSE inputs
	 * Only can return true if this is the active UI
	 * 
	 * All these methods simply utilize the program wide keyboard/mouse functions
	 * They only check for the inputs if this is the active UI.
	 */
	
	public boolean keyDown(int keycode) {
		if (isActive())
			return Program.keyboard.keyDown(keycode);
		else
			return false;
	}
	
	public boolean keyDown(char c) {
		if (isActive())
			return Program.keyboard.keyDown(c);
		else
			return false;
	}
	
	public boolean keyPressed(int keycode) {
		if (isActive())
			return Program.keyboard.keyPressed(keycode);
		else
			return false;
	}
	
	public boolean keyPressed(int ... keycodes) {
		if (isActive())
			return Program.keyboard.keyPressed(keycodes);
		else
			return false;
	}
	
	public boolean keyPressed(char c) {
		if (isActive())
			return Program.keyboard.keyPressed(c);
		else
			return false;
	}
	
	public Keyboard.Key getNextKey() {
		if (isActive())
			return Program.keyboard.getNextKey();
		else
			return null;
	}
	
	public boolean isMouseDown(int button) {
		if (isActive())
			return Program.mouse.isMouseDown(button);
		else
			return false;
	}
	
	public boolean isMouseDown() {
		if (isActive()) 
			return Program.mouse.isMouseDown();
		else
			return false;
	}
	
	public boolean isActive() {
		return UIController.getActiveUI() == this;
	}
}
