package levelEditor.editorComponents;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Represents an EntityObject in the Level Editor
 */
public class EditorObject implements EditorComponent {
	public int id;
	public float x, y;
	
	public EditorObject() {
		this(0,0,0);
	}
	
	public EditorObject(int id, float x, float y) {
		this.id = id;
		this.x = x;
		this.y = y;
	}
	
	public void write(DataOutputStream out) throws IOException {
		out.write(id);
		out.write((int)x);
		out.write((int)(x%1.0f*255));
		out.write((int)y);
		out.write((int)(y%1.0f*255));
	}
}
