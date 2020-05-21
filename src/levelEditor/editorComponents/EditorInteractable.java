package levelEditor.editorComponents;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class EditorInteractable implements EditorComponent {

	public float x, y;
	public String name;
	
	public EditorInteractable() {
		
	}
	
	public EditorInteractable(String name, float x, float y) {
		this.name = name;
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void write(DataOutputStream out) throws IOException {
		out.write((int)x);
		out.write((int)(x%1.0f*256));
		out.write((int)y);
		out.write((int)(y%1.0f*256));
		out.write(name.length());
		for (int i = 0; i < name.length(); i++)
			out.writeChar(name.charAt(i));
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		x = in.read() + in.read()/256.0f;
		y = in.read() + in.read()/256.0f;
		int textLength = in.read();
		this.name = "";
		for (int i = 0; i < textLength; i++)
			this.name+=in.readChar();
	}

	@Override
	public void translate(int dx, int dy) {
		this.x+=dx;
		this.y+=dy;
	}

	@Override
	public String getString() {
		return "interactable";
	}

}
