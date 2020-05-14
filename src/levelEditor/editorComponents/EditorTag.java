package levelEditor.editorComponents;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class EditorTag implements EditorComponent {
	public String text;
	public float x, y;
	
	public EditorTag() {
		this("",0,0);
	}
	
	public EditorTag(String text, float x, float y) {
		this.text = text;
		this.x = x;
		this.y = y;
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.write(this.text.length());
		//now print each character in the text
		for (int i = 0; i < this.text.length(); i++)
			out.writeChar(this.text.charAt(i));
		out.write((int)this.x);
		out.write((int)(this.x%1.0f*256));
		out.write((int)this.y);
		out.write((int)(this.y%1.0f*256));
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		int textLength = in.read();
		this.text = "";
		for (int i = 0; i < textLength; i++)
			this.text+=in.readChar();
		this.x = in.read() + in.read()/256.0f;
		this.y = in.read() + in.read()/256.0f;
	}

	@Override
	public void translate(int dx, int dy) {
		this.x+=dx;
		this.y+=dy;
	}

	@Override
	public String getString() {
		return "tag";
	}
}
