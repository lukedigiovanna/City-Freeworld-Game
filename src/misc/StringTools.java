package misc;

public class StringTools {
	/**
	 * Converts a string in ID form to display form
	 * ex: hello_world => Hello World
	 * @return
	 */
	public static String underscoreToDisplay(String str) {
		String[] tokens = str.split("_");
		String display = "";
		for (int i = 0; i < tokens.length; i++) {
			display+=tokens[i];
			if (i < tokens.length-1)
				display+=" ";
		}
		return display;
	}
}
