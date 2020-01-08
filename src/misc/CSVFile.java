package misc;

import java.io.*;
import java.util.*;

public class CSVFile {
	
	private List<ArrayList<String>> data;
	
	public CSVFile(String fileName) {
		data = new ArrayList<ArrayList<String>>();
		
		try (
			BufferedReader in = new BufferedReader(new FileReader(fileName));
		) {
			String next;
			while ((next = in.readLine()) != null) {
				ArrayList<String> row = new ArrayList<String>();
				String[] values = next.split(",");
				for (String s : values)
					row.add(s);
				data.add(row);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int getNumberOfRows() {
		return this.data.size();
	}
	
	public int getNumberOfColumns(int row) {
		return this.data.get(row).size();
	}
	
	public String getString(int row, int col) {
		return data.get(row).get(col);
	}
	
	public int getInt(int row, int col) {
		return Integer.parseInt(getString(row,col));
	}
	
	public float getFloat(int row, int col) {
		return Float.parseFloat(getString(row,col));
	}
	
	public double getDouble(int row, int col) {
		return Double.parseDouble(getString(row,col));
	}
	
	public boolean getBoolean(int row, int col) {
		return Boolean.parseBoolean(getString(row,col));
	}
	
	public void removeRows(int howMany) {
		for (int i = 0; i < howMany; i++)
			if (data.size() > 0)
				data.remove(0);
	}
}
