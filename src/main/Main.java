package main;

import java.io.*;

import misc.MathUtils;

public class Main {
	public static void main(String[] args) {
		Program.init();
	
			
		//testing some io stuff
		int nums = 256;
		try {
//			DataOutputStream out = new DataOutputStream(new FileOutputStream("test.dat"));
//			for (int i = 0; i < 512; i++)
//				out.writeByte(i);
//			out.close();
			DataInputStream in = new DataInputStream(new FileInputStream("test.dat"));
			int inNum;
			while ((inNum = in.read()) != -1)
				System.out.println(inNum);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
