package json;

public class JSONField {
	//json fields are string - type pairs
	private String name;
	private JSONValue value;
	
	/**
	 * Generates the field based on a given token
	 * @param token
	 */
	public JSONField(String token) {
		try {
		name = token.substring(0,token.indexOf(':'));
		name = name.substring(name.indexOf('\"')+1);
		name = name.substring(0,name.indexOf('\"'));
		String value = token.substring(token.indexOf(':')+1);
		//by default the value should be type string
		this.value = new JSONValue(value);
		} catch (Exception e) {
			System.out.println(token);
			e.printStackTrace();
			System.exit(69);
		}
	}
	
	public String getName() {
		return name;
	}
	
	public JSONValue getValue() {
		return value;
	}
}
