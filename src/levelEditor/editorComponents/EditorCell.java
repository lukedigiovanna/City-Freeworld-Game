package levelEditor.editorComponents;

public class EditorCell {
	public int value;
	public int rotation;
	public int flip; // 0 = flip, 1 = don't flip
	
	public EditorCell() {
		value = -1;
		rotation = 0;
		flip = 0; 
	}
}
