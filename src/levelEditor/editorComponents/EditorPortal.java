package levelEditor.editorComponents;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class EditorPortal implements EditorComponent {
	public int destinationNumber;
	
	public float x, y, width, height;
	public float destX, destY;
	
	public EditorPortal() {
		this(0,0,0,0.5f,0.5f,0,0); //initializes everything to 0
	}
	
	public EditorPortal(int destinationNumber, float x, float y, float width, float height, float destX, float destY) {
		this.destinationNumber = destinationNumber;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.destX = destX;
		this.destY = destY;
	}
	
	public void write(DataOutputStream out) throws IOException {
		out.write(destinationNumber);       //destination
		out.write((int)(x));                //x-coord whole number
		out.write((int)((x%1.0f)*255));     //x-coord decimal
		out.write((int)(y));                //y coord whole number
		out.write((int)((y%1.0f)*255));     //y coord decimal
		out.write((int)(width));            //width whole number
		out.write((int)((width%1.0f)*255)); //width decimal
		out.write((int)(height));           //height whole number
		out.write((int)((height%1.0f)*255));//height decimal
		out.write((int)(destX));            //dest x-coord whole number
		out.write((int)((destX%1.0f)*255)); //dest x-coord decimal
		out.write((int)(destY));            //dest y-coord whole number
		out.write((int)((destY%1.0f)*255)); //dest y-coord decimal
	}
	
	public void read(DataInputStream in) throws IOException {
		this.destinationNumber = in.read();
		this.x = in.read() + in.read() / 256.0f;
		this.y = in.read() + in.read() / 256.0f;
		this.width = in.read() + in.read() / 256.0f;
		this.height = in.read() + in.read() / 256.0f;
		this.destX = in.read() + in.read() / 256.0f;
		this.destY = in.read() + in.read() / 256.0f;
	}
	
	public void translate(int dx, int dy) {
		this.x += dx;
		this.y += dy;
	}
	
	public String getString() {
		return "portal";
	}
	
	public String toString() {
		return "Destination: "+destinationNumber+" x: "+x+" y: "+y+" dx: "+destX+" dy: "+destY;
	}
}
