package levelEditor.editorComponents;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Represents an EntityObject in the Level Editor
 */
public class EditorObject implements EditorComponent {
	public int id;
	public float x, y, rotation;
	
	public EditorObject() {
		this(0,0,0,0);
	}
	
	public EditorObject(int id, float x, float y, float r) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.rotation = r;
		this.rotation = (float)Math.PI/4;
	}
	
	public void write(DataOutputStream out) throws IOException {
		out.write(id);
		out.write((int)x);
		out.write((int)(x%1.0f*255));
		out.write((int)y);
		out.write((int)(y%1.0f*255));
		out.write((int)rotation);
		out.write((int)(rotation%1.0f*255));
	}
	
	public void read(DataInputStream in) throws IOException {
		this.id = in.read();
		this.x = in.read() + in.read()/256.0f;
		this.y = in.read() + in.read()/256.0f;
		this.rotation = in.read() + in.read()/256.0f;
	}
	
	public void translate(int dx, int dy) {
		this.x += dx;
		this.y += dy;
	}
	
	public String getString() {
		return "object";
	}
}
