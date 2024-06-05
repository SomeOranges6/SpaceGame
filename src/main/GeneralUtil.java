package main;

import java.awt.Polygon;

public class GeneralUtil {
	
	public static Polygon fourPointStar(int x, int y, int r) {
		   
		   Polygon poly = new Polygon();
		   poly.addPoint(x + 4 * r, y);
		   poly.addPoint(x + 2 * r, y + 1 * r);
		   poly.addPoint(x, y + 4 * r);
		   poly.addPoint(x - 2 * r, y + 1 * r);
		   poly.addPoint(x - 4 * r, y);
		   poly.addPoint(x - 2 * r, y - 1 * r);
		   poly.addPoint(x, y - 4 * r);
		   poly.addPoint(x + 2 * r, y - 1 * r);
		   return poly;
		   
	   }
	   
	   public static Polygon rightTriangle(int x, int y, int r, boolean up) {
		   int direction = up ? 1 : -1;
		   Polygon poly = new Polygon();
		   poly.addPoint(x - 8 * r, y);
		   poly.addPoint(x, y);
		   poly.addPoint(x, y - 3 * r * direction);
		   return poly; 
	   }
	
}
