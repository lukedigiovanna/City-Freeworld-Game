package world;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Properties implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final Key
		/**
		 * Key for whether or not the object should have collision with rigid structures
		 * like walls and rigid bodies. 
		 */
		KEY_HAS_COLLISION = new Key(0),
		/**
		 * Key for whether or not this object needs to regenerate its hitbox
		 * This value should be false for objects that will not be in the world for long
		 * such as particles.
		 */
		KEY_REGENERATE_HITBOX = new Key(1),
		/**
		 * Key for whether or not this object can be hurt by things like
		 * bullets or fire.
		 */
		KEY_INVULNERABLE = new Key(2),
		/**
		 * Key for whether or not the hitbox should be rotated as it moves
		 * or remain static.
		 */
		KEY_HITBOX_HAS_ROTATION = new Key(3),
		/**
		 * Key for whether the hitbox should be treated as a rigid
		 * body that does not allow collisions.
		 */
		KEY_HAS_RIGID_BODY = new Key(4),
		/**
		 * Key for whether or not the object should be destroyed
		 * if it runs into a rigid structure.
		 */
		KEY_DESTROY_ON_COLLISION = new Key(5),
		/**
		 * Objects with this property will repel each other
		 * Makes it so like 10 people cannot be standing in one square
		 * Similar to Minecraft entity collision mechanics
		 */
		KEY_HAS_SQUISHY_BODY = new Key(6);
	public static final Value
		VALUE_HAS_COLLISION_TRUE = new Value(0),
		VALUE_HAS_COLLISION_FALSE = new Value(1),
		VALUE_REGENERATE_HITBOX_TRUE = new Value(2),
		VALUE_REGENERATE_HITBOX_FALSE = new Value(3),
		VALUE_INVULNERABLE_TRUE = new Value(4),
		VALUE_INVULNERABLE_FALSE = new Value(5),
		VALUE_HITBOX_HAS_ROTATION_TRUE = new Value(6),
		VALUE_HITBOX_HAS_ROTATION_FALSE = new Value(7),
		VALUE_HAS_RIGID_BODY_TRUE = new Value(8),
		VALUE_HAS_RIGID_BODY_FALSE = new Value(9),
		VALUE_DESTROY_ON_COLLISION_TRUE = new Value(10),
		VALUE_DESTROY_ON_COLLISION_FALSE = new Value(11),
		VALUE_HAS_SQUISHY_BODY_TRUE = new Value(12),
		VALUE_HAS_SQUISHY_BODY_FALSE = new Value(13);
	
	private Map<Key,Value> map;
	
	public Properties() {
		// default properties
		map = new HashMap<Key,Value>();
		//default properties
		set(KEY_HAS_COLLISION,       VALUE_HAS_COLLISION_TRUE); 
		set(KEY_REGENERATE_HITBOX,   VALUE_REGENERATE_HITBOX_TRUE);
		set(KEY_INVULNERABLE,        VALUE_INVULNERABLE_FALSE);
		set(KEY_HITBOX_HAS_ROTATION, VALUE_HITBOX_HAS_ROTATION_TRUE);
		set(KEY_HAS_RIGID_BODY,      VALUE_HAS_RIGID_BODY_FALSE);
		set(KEY_DESTROY_ON_COLLISION,VALUE_DESTROY_ON_COLLISION_FALSE);
		set(KEY_HAS_SQUISHY_BODY,    VALUE_HAS_SQUISHY_BODY_FALSE);
	}
	
	public void set(Key key, Value value) {
		map.put(key, value);
	}
	
	public Value get(Key key) {
		return map.get(key);
	}
	
	public static class Key implements Serializable {
		private static final long serialVersionUID = 1L;
		
		private int num = 0;
		public Key(int num) {
			this.num = num;
		}
		
		public int hashCode() {
			return num;
		}
	}
	
	public static class Value implements Serializable {
		private static final long serialVersionUID = 1L;
		
		private int num = 0;
		public Value(int num) {
			this.num = num;
		}
		
		public int hashCode() {
			return num;
		}
	}
}
