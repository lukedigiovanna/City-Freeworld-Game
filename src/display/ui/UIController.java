package display.ui;

import display.DisplayController;

/**
 * Controls the active UI of the program
 * Ensures keyboard/mouse inputs are directed to the proper UI at a given time
 * or locus of control
 */
public class UIController {
	
	/**
	 * The active UI is the one which keyboard and mouse inputs are directed to
	 * Each display has a default UI and other UIs can take precedence.
	 * When a display is switched with an alternate UI active, the alternate UI is removed
	 * from activity
	 */
	private static UI activeUI;
	
	/**
	 * Sets the UI using the ID with reference
	 * to the UICodex, which holds most UIs
	 * @param id
	 */
	public static void setActiveUI(String id) {
		setActiveUI(UICodex.get(id));
	}
	
	/**
	 * Generic function to set the UI from any source.
	 * @param ui
	 */
	public static void setActiveUI(UI ui) {
		activeUI = ui;
		System.out.println(activeUI.getID());
	}
	
	/**
	 * Returns a reference to the current active UI
	 * @return
	 */
	public static UI getActiveUI() {
		return activeUI;
	}
	
	/**
	 * defaults the UI to the current display screen
	 */
	public static void setDefault() {
		//get the current display
		DisplayController.Screen current = DisplayController.getCurrentScreen();
		//convert to ID format
		String currentScreenID = current.toString().toLowerCase();
		setActiveUI(currentScreenID);
	}
	
	/**
	 * Used as a buffer method to the call the initialization
	 * of the generic UIs
	 */
	public static void initialize() {
		UICodex.initialize();
		setDefault();
	}
}
