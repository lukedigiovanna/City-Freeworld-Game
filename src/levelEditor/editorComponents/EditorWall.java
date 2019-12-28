package levelEditor.editorComponents;

import java.io.DataOutputStream;
import java.io.IOException;

public class EditorWall {
	public float x1, y1, x2, y2;
	
	public EditorWall() {
		this(0,0,0,0);
	}
	
	public EditorWall(float x1, float y1, float x2, float y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	public void write(DataOutputStream out) throws IOException {
		out.write((int)(x1));                //x1-coord whole number
		out.write((int)((x1%1.0f)*256));     //x1-coord decimal
		out.write((int)(y1));                //y1 coord whole number
		out.write((int)((y1%1.0f)*256));     //y1 coord decimal
		out.write((int)(x2));                //x2 whole number
		out.write((int)((x2%1.0f)*256));     //x2 decimal
		out.write((int)(y2));                //y2 whole number
		out.write((int)((y2%1.0f)*256));     //y2 decimal
	}
}
