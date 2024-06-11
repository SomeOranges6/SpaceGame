package spaceGame;
import java.util.Random;
import java.awt.Rectangle;

@SuppressWarnings("serial")
public class Powerups extends Rectangle {
	int diameter, x, y;
	String type;
	boolean intersect;
	
	Random rand = new Random();
	Powerups(int x, int y) {
		this.x = x;
		this.y = y;
		this.intersect = false;
		switch(rand.nextInt(0, 6+1)) {
		case 0:
			this.type = "HP";
			this.diameter = 10;
			break;
		case 1:
			this.type = "DAMAGE";
			this.diameter = 5;
			break;
		default:
			this.type = "DOUBLE";
			this.diameter = 7;
			break;
		}
	}
	
	
}
