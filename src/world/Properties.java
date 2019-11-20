package world;

import java.util.HashMap;
import java.util.Map;

public class Properties {
	private Map<Key,Value> map;
	
	public Properties() {
		// default properties
		map = new HashMap<Key,Value>();
	}
	
	public static class Key {
		private int num = 0;
		public Key(int num) {
			this.num = num;
		}
		
		public int hashCode() {
			
		}
	}
	
	public static class Value {
		
	}
}
