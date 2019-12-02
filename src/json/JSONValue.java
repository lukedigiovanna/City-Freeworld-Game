package json;

public class JSONValue {
	public static enum Type {
		STRING, //0
		BOOLEAN,//1
		NUMBER, //2
		ARRAY,  //3
		OBJECT, //4
		NULL    //5
 	}
	
	private Type type;
	
	private Object value;
	
	public JSONValue(String value) {
		String str = value;
		if (str.contains("\"")) {
			str = str.substring(str.indexOf('\"')+1);
			str = str.substring(0,str.indexOf('\"'));
		}
		this.value = str; //by default
		type = Type.STRING;
		
		value = removeSurroundingWhitespace(value);
		
		//if null exists then use null
		if (value.contains("null")) {
			this.value = null;
			type = Type.NULL;
		}
		
		try {
			double num = Double.parseDouble(value);
			this.value = num; //this is only called if parse double worked
			this.type = Type.NUMBER;
		} catch (Exception e) {
			//do nothing, this gets called when the value is not a number
		}
		
		if (value.toLowerCase().startsWith("true")) {
			this.value = true;
			this.type = Type.BOOLEAN;
		}
		
		if (value.toLowerCase().startsWith("false")) {
			this.value = false;
			this.type = Type.BOOLEAN;
		}
		
		//now lets check if it is an object 
		if (value.charAt(0) == '{') {
			String txt = value.substring(1,value.length()-1);
			this.value = new JSONObject(txt);
			this.type = Type.OBJECT;
		}
		
		//check if it is an array
		if (value.charAt(0) == '[') {
			String txt = value.substring(1,value.length()-1);
			this.value = new JSONArray(txt);
			this.type = Type.ARRAY;
		}
	}
	
	public Type getType() {
		return this.type;
	}
	
	private String removeSurroundingWhitespace(String str) {
		String s = str;
		while (s.charAt(0) == ' ') 
			s = s.substring(1);
		while (s.charAt(s.length()-1) == ' ')
			s = s.substring(0,s.length()-1);
		return s;
	}
	
	public Object get() {
		return this.value;
	}
	
	public String toString() {
		if (this.type == Type.STRING)
			return "\""+this.value+"\"";
		return this.value+"";
	}
}
