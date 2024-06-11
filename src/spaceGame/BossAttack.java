package spaceGame;

import java.awt.Rectangle;

public class BossAttack {
	Rectangle r1, r2;
	
	BossAttack(String move) {
		if (move.equals("ARM")) {
			r1.x = 100;
			r1.y = 100;
			r1.width = 50;
			r1.height = 100;
			r2.x = 400;
			r1.y = 100;
			r1.width = 50;
			r1.height = 100;
		}
		if (move.equals("LASER")) {
			r1.x = 0;
			r1.y = 0;
			r1.width = 20;
			r1.height = 0;
			r2.x = 580;
			r2.y = 0;
			r2.width = 20;
			r2.height = 0;
		}
	}
}
