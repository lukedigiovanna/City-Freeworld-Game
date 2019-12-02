package main;

public class Main {
	public static void main(String[] args) {
		Program.init();
		
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
