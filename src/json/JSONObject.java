package json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JSONObject {
	private Map<String, JSONValue> fields;
	
	/**
	 * Makes a new object given some json text
	 * JSON text should include the text just after the 
	 * opening curly bracket to the text right before the curly bracket
	 * @param jsonText
	 */
	public JSONObject(String jsonText) {
		//lets go one character at a time
		int bracketLevel = 0; //1 from the beginning of the json file 
		boolean inString = false;
		List<String> tokens = new ArrayList<String>(); //represents each field
		//we need to split up the json file by each field first
		//separated by a comma on the first bracket level
		String curToken = "";
		for (int i = 0; i < jsonText.length(); i++) {
			char c = jsonText.charAt(i);
			if (c == '\"')
				inString = !inString;
			switch (c) {
			case '{': case '[': 
				bracketLevel++;
				break;
			case '}': case ']': 
				bracketLevel--;
			}
			if (bracketLevel == 0 && !inString && c == ',') {
				tokens.add(curToken);
				curToken = ""; //restart the token
			} else {
				curToken+=c;
			}
		}
		if (!curToken.contentEquals("")) //only if there is something in the token
			tokens.add(curToken); //the last one may not have a comma at the end
		
		fields = new HashMap<String,JSONValue>();
		for (String t : tokens) {
			if (t.indexOf(":") < 0)
				continue;
			JSONField field = new JSONField(t);
			fields.put(field.getName(), field.getValue());
		}
	}
	
	public JSONValue get(String fieldName) {
		return fields.get(fieldName);
	}
	
	public void set(String fieldName, JSONValue value) {
		this.fields.put(fieldName, value);
	}
	
	public void set(String fieldName, String value) {
		set(fieldName,new JSONValue(value));
	}
	
	public void set(String fieldName, boolean value) {
		set(fieldName,new JSONValue(value+""));
	}
	
	public void set(String fieldName, double value) {
		set(fieldName,new JSONValue(value+""));
	}
	
	public void set(String fieldName, JSONObject value) {
		set(fieldName,new JSONValue(value+""));
	}
	
	public void set(String fieldName, JSONArray value) {
		set(fieldName,new JSONValue(value+""));
	}
	
	public String toString() {
		String s = "{\n"; //starts with open curly bracket
		Set<String> keys = fields.keySet();
		for (String key : keys) {
			s+="\""; //open the name with a quote
			s+=key;
			s+="\":"; //end the name with a quote and a colon
			JSONValue val = fields.get(key);
			s+=val.toString()+",\n";
		}
		s += "}"; //ends with closing curly bracket
		return s;
	}
}
