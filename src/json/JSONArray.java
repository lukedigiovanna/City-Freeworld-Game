package json;

import java.util.ArrayList;
import java.util.List;

public class JSONArray {
	private List<JSONValue> values;
	
	public JSONArray(String jsonText) {
		List<String> tokens = new ArrayList<String>();
		int bracketLevel = 0;
		String curToken = "";
		for (int i = 0; i < jsonText.length(); i++) {
			char c = jsonText.charAt(i);
			switch (c) {
			case '{': case '[':
				bracketLevel++;
				break;
			case '}': case ']':
				bracketLevel--;
			}
			if (bracketLevel == 0 && c == ',') {
				tokens.add(curToken);
				curToken = "";
			} else {
				curToken+=c;
			}
		}
		if (!curToken.contentEquals(""))
			tokens.add(curToken);
		values = new ArrayList<JSONValue>();
		for (String s : tokens) {
			values.add(new JSONValue(s));
		}
	}
	
	public void add(JSONValue val) {
		values.add(val);
	}
	
	public void add(String value) {
		add(new JSONValue(value+""));
	}
	
	public void add(boolean value) {
		add(new JSONValue(value+""));
	}
	
	public void add(double value) {
		add(new JSONValue(value+""));
	}
	
	public void add(JSONObject value) {
		add(new JSONValue(value+""));
	}
	
	public void add(JSONArray value) {
		add(new JSONValue(value+""));
	}
	
	public JSONValue get(int index) {
		if (index < 0 || index > this.values.size()-1)
			throw new JSONException("index "+index+" out of bounds for JSON array");
		else
			return values.get(index);
	}
	
	public int size() {
		return values.size();
	}
	
	public String toString() {
		String s = "[";
		for (int i = 0; i < values.size(); i++) {
			s+=values.get(i);
			if (i < values.size()-1)
				s+=", ";
		}
		s += "]";
		return s;
	}
}
