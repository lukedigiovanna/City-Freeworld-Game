package levelEditor;

import java.io.File;

public class EditorWorld {
	private String name;
	private int numberOfRegions;
	
	public EditorWorld(String name) {
		this.name = name;
		updateRegionNumber();
	}
	
	public String getName() {
		return this.name;
	}
	
	public void updateRegionNumber() {
		this.numberOfRegions = 0;
		while ((new File("assets/worlds/"+name+"/regions/reg-"+numberOfRegions+".DAT")).exists()) 
			this.numberOfRegions++;
	}
	
	public int getNumberOfRegions() {
		updateRegionNumber();
		return this.numberOfRegions;
	}
}
