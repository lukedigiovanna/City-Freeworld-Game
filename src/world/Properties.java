package world;

import java.util.HashMap;
import java.util.Map;

public class Properties {
	public static final Key
		/**
		 * Key for whether or not the object should have collision with rigid structures
		 * like walls. 
		 */
		KEY_HAS_COLLISION = new Key(0),
		/**
		 * Key for whether or not this object needs to regenerate its hitbox
		 * This value should be false for objects that will not be in the world for long
		 * such as particles.
		 */
		KEY_REGENERATE_HITBOX = new Key(1);
	public static final Value
		VALUE_HAS_COLLISION_TRUE = new Value(0),
		VALUE_HAS_COLLISION_FALSE = new Value(1),
		VALUE_REGENERATE_HITBOX_TRUE = new Value(2),
		VALUE_REGENERATE_HITBOX_FALSE = new Value(3);
	
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
