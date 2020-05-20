package display.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import display.DisplayController;

/**
 * Static class to describe 
 */
public class UICodex {
	
	private static Map<String,UI> uis;
	
	public static UI get(String id) {
		UI ui = uis.get(id);
		if (ui == null) {
			uis.put(id, new UI(id));
			return get(id);
		} else {
			return ui;
		}
	}
	
	public static void add(String id) {
		UI ui = new UI(id);
		uis.put(id, ui);
	}
	
	/**
	 * Creates UIs for each display screen
	 */
	public static void initialize() {
		uis = new HashMap<String,UI>(); //initialize the list
		//initialize the UIs for the displays
		for (DisplayController.Screen display : DisplayController.Screen.values()) {
			String id = display.toString().toLowerCase(); //UIs are already encoded as id format (lower case, underscore spaces)
			UI ui = new UI(id);
			uis.put(id, ui);
		}
		
		//other UIs will be initialized when their id is passed
	}
}
