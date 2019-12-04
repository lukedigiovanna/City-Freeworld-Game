package json;

import java.io.*;

/**
 * Represents a JSON file
 * Can be instantiated by giving it a file path to the JSON file and it reads it in
 * Can be modified with 
 */
public class JSONFile {
	//this holds all the information for a json file
	
	private JSONObject mainObject;
	
	private String filePath;
	private File jsonFile;
	
	/**
	 * Generates a JSON file object from a JSON file path
	 * @param filePath
	 */
	public JSONFile(String filePath) throws JSONException {
		//lets check the file extension
		String extension = filePath.substring(filePath.length()-5);
		if (!extension.contentEquals(".json") && !extension.contentEquals(".JSON"))
			throw new JSONException("File is not of type JSON");
		
		this.filePath = filePath;
		this.jsonFile = new File(filePath);
		
		if (this.jsonFile.exists()) { //we only want to try to read if the json file exists
			String jsonText = "";
			//we need to open an io stream to the file now
			try {
				BufferedReader in = new BufferedReader(new FileReader(filePath));
				String str;
				while ((str = in.readLine()) != null)
					jsonText+=str;
				mainObject = new JSONObject(jsonText.substring(1,jsonText.length()-1));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			mainObject = new JSONObject("");
		}
	}
	
	public Object get(String fieldName) {
		return mainObject.get(fieldName).get();
	}
	
	public void set(String fieldName, JSONValue value) {
		mainObject.set(fieldName,value);
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
	
	/**
	 * Rewrites the current state of the JSON file to the actual file on 
	 * the hard drive.
	 */
	public void save() {
		try {
			PrintWriter out = new PrintWriter(jsonFile);
			out.println(mainObject.toString());
			out.close();
		} catch (IOException e) {
			
		}
	}
	
	/**
	 * Wipes the JSON file to nothing
	 */
	public void clear() {
		mainObject = new JSONObject("");
	}
	
	public String toString() {
		return mainObject.toString();
	}
}
