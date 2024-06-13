package spaceGame;

@SuppressWarnings("serial")
abstract class Enemy extends GameEntities {
	public int hp, iFrames; //iFrames are not used
	public int firerate, fireBuffer, untilFire; 
	boolean fireTrigger; //has the enemy fired? True(no) False(yes)
	public double rotation;
	int displacement = 0, rotationSpd, pointX, pointY; //for rotater
	double findRotation(int x1, int y1, int x2, int y2) {
		double rotation = 0;
		rotation = Math.toDegrees(Math.atan2(y2-y1, x2-x1));
		return rotation; 
	}
}

@SuppressWarnings("serial")
//Average line-moving joe
class Liner extends Enemy {
	Liner() {
		this.hp=1;
		this.iFrames=25;
		this.size=16;
		this.firerate=1000;
		this.fireBuffer=0;
		this.fireTrigger = true;
		this.untilFire=0;
		this.x=WINB/2;
		this.y=0;
		this.spd = 2;
		this.rotation = randNum.nextInt(-135,-45+1)*-1;
	}
	
	Liner(int x, int y) {
		this.hp=1;
		this.iFrames=25;
		this.size=16;
		this.firerate=1000;
		this.fireBuffer=0;
		this.fireTrigger = true;
		this.untilFire=0;
		this.x=x;
		this.y=y;
		this.spd = 2;
		this.rotation = randNum.nextInt(-135,-45+1)*-1;
	}
	
	Liner(int x, int y, double rotation) {
		this.hp=1;
		this.iFrames=25;
		this.size=16;
		this.firerate=1000;
		this.fireBuffer=0;
		this.fireTrigger = true;
		this.untilFire=0;
		this.x=x;
		this.y=y;
		this.spd = 2;
		this.rotation = rotation;
	}

}

//Can take more damage
@SuppressWarnings("serial")
class Tanker extends Enemy {
	Tanker() {
		this.hp=4;
		this.iFrames=75;
		this.size=30;
		this.firerate=1000;
		this.fireBuffer=0;
		this.untilFire=0;
		this.x=WINB/2;
		this.y=0;
		this.spd = 4;
		this.rotation = 0;
	}
	
	Tanker(int x, int y) {
		this.hp=4;
		this.iFrames=75;
		this.size=30;
		this.firerate=1000;
		this.fireBuffer=0;
		this.untilFire=0;
		this.x=x;
		this.y=y;
		this.spd = randNum.nextInt(3,4+1);
		this.rotation = 0;
	}
	
}

//Rotates around a point that may move
@SuppressWarnings("serial")
class Rotater extends Enemy { 
	//x and y are the points they rotate, displacement is how far from the point
	Rotater() {
		this.hp=1;
		this.iFrames=75;
		this.size=16;
		this.firerate=1000;
		this.fireBuffer=0;
		this.fireTrigger = true;
		this.untilFire=0;
		this.x=0;
		this.y=0;
		this.pointX = x;
		this.pointY = y;
		this.spd = 1;
		this.rotationSpd = 2;
		this.displacement = 0;
		this.rotation = 0;
	}
	
	Rotater(int x, int y, int displacement) {
		this.hp=1;
		this.iFrames=75;
		this.size=16;
		this.firerate=1000;
		this.fireBuffer=0;
		this.fireTrigger = true;
		this.untilFire=0;
		this.x=0;
		this.y=0;
		this.rotationSpd = 2;
		this.pointX = x;
		this.pointY = y;
		this.displacement = displacement;
		this.spd = 1;
		this.rotation = 0;
	}
	
	Rotater(int x, int y, int displacement, int rotation) {
		this.hp=1;
		this.iFrames=75;
		this.size=16;
		this.firerate=1000;
		this.fireBuffer=0;
		this.fireTrigger = true;
		this.untilFire=0;
		this.x=0;
		this.y=0;
		this.pointX = x;
		this.pointY = y;
		this.displacement = displacement;
		this.spd = 1;
		this.rotationSpd = 5;
		this.rotation = rotation;
	}
}

/*
//shoots really fast, essentially limiting the arena
class Beamer extends Enemy {

}

//Explodes on death and a bit tougher
class Demolitioner extends Enemy {

	void specialAbility() {
		//explodes on death
	}
}

*/
