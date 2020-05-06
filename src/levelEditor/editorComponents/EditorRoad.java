package levelEditor.editorComponents;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import misc.Vector2;

public class EditorRoad implements EditorComponent {
	List<Vector2> points; //direction is classified by the order of points

	@Override
	public void write(DataOutputStream out) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void translate(int dx, int dy) {
		// TODO Auto-generated method stub
		
	}
	
	public String getString() {
		return "road";
	}
	
}
