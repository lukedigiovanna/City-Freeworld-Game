package world;

import display.Animation;

public class CellTemplate {
	
	private static CellTemplate[] cellTemps;
	
	public static void initialize(String jsonPath) {
		cellTemps = new CellTemplate[256]; //256 possible ones
	}
	
	public static CellTemplate get(int index) {
		return cellTemps[index];
	}
	
	private Animation animation;
	private String stringID;
	private int id;
	
	public CellTemplate() {
		
	}
}
