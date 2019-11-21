package world;

import java.util.HashMap;
import java.util.Map;

public class Properties {
	public static final Key
		KEY_HAS_COLLISION = new Key(0);
	public static final Value
		VALUE_HAS_COLLISION_TRUE = new Value(0),
		VALUE_HAS_COLLISION_FALSE = new Value(1);
	
	private Map<Key,Value> map;
	
	public Properties() {
		// default properties
		map = new HashMap<Key,Value>();
		//default properties
		set(KEY_HAS_COLLISION,VALUE_HAS_COLLISION_TRUE); //has collision by default
	}
	
	public void set(Key key, Value value) {
		map.put(key, value);
	}
	
	public Value get(Key key) {
		return map.get(key);
	}
	
	public static class Key {
		private int num = 0;
		public Key(int num) {
			this.num = num;
		}
		
		public int hashCode() {
			return num;
		}
	}
	
	public static class Value {
		private int num = 0;
		public Value(int num) {
			this.num = num;
		}
		
		public int hashCode() {
			return num;
		}
	}
}
