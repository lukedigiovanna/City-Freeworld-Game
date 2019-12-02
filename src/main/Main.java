package main;

import java.io.*;

import entities.player.Player;
import json.JSONArray;
import json.JSONFile;
import json.JSONObject;
import json.JSONValue;
import misc.*;
import world.Hitbox;

public class Main {
	public static void main(String[] args) {
		//Program.init();
		
		//testing JSON stuff
		JSONFile json = new JSONFile("assets/something.json");
		json.set("name","Luke DiGiovanna");
		json.set("age", 16);
		JSONArray arr = new JSONArray("3.0,true,null");
		json.set("siblings", arr);
		JSONObject mom = new JSONObject("");
		mom.set("name", "Deanna DiGiovanna");
		json.set("mother", mom);
		json.save();
		
		JSONArray siblings = (JSONArray)json.get("siblings");
		System.out.println(siblings.get(1));
		
		JSONObject mother = (JSONObject)json.get("mother");
		System.out.println(mother.get("name"));
		
		System.out.println(json.get("name"));
		
//		try {
//			DataOutputStream out = new DataOutputStream(new FileOutputStream("assets/saves/sample_world/regions/reg-0/grid.dat"));
//			int width = 60, height = 40;
//			out.write(width);
//			out.write(height);
//			for (int y = 0; y < height; y++) {
//				for (int x = 0; x < width; x++) {
//					if ((x*0.25-30*.25) * (x*0.25-30*0.25) <= y)
//						out.write(1);
//					else
//						out.write(0);
//				}
//			}
////			System.out.println("finished");
////			DataInputStream in = new DataInputStream(new FileInputStream("assets/saves/sample_world/regions/reg-0/grid.dat"));
////			int inNum;
////			while ((inNum = in.read()) != -1)
////				System.out.println(inNum);
////			in.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
}
