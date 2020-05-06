package levelEditor.editorComponents;

import java.io.*;

public interface EditorComponent {
	void write(DataOutputStream out) throws IOException;
	void read(DataInputStream in) throws IOException;
	
	void translate(int dx, int dy);
	
	String getString();
}
